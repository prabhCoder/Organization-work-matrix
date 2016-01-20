package in.co.init.workmatrix.rest;

import in.co.init.workmatrix.data.WorkMatrixRepository;
import in.co.init.workmatrix.data.WorkMatrixRepository.EmployeeProjectRelationStatus;
import in.co.init.workmatrix.model.Employee;
import in.co.init.workmatrix.model.Project;
import in.co.init.workmatrix.model.result_types.EmployeeResource;
import in.co.init.workmatrix.model.result_types.EmployeeResult;
import in.co.init.workmatrix.model.result_types.ProjectResource;
import in.co.init.workmatrix.model.result_types.ProjectResult;
import in.co.init.workmatrix.model.result_types.StatusResponse;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 * Provides REST services
 * 
 */
@Path("/")
public class WorkMatrixResourceService implements Serializable {
	@Inject
	private Logger log;

	@Context
	UriInfo uriInfo;

	@EJB
	private WorkMatrixRepository wmRepo;

	private static final long serialVersionUID = 1L;

	@GET
	@Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeResource getEmployees(@QueryParam("name") String name,
			@QueryParam("email") String email,
			@QueryParam("department") String department,
			@QueryParam("branch") String branch,
			@QueryParam("position") String position,
			@QueryParam("projectName") String projectName) {
		log.log(Level.INFO, "WMService : Fetching employees");
		List<Employee> employeeList = wmRepo.findEmployeeBy(name, email,
				branch, department, position, projectName);
		EmployeeResource empResource = new EmployeeResource(employeeList,
				uriInfo.getAbsolutePath());
		return empResource;
	}

	@GET
	@Path("/employees/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeResult getEmployeeById(@PathParam("id") String empID) {
		log.log(Level.INFO, "WMService : Fetching employee " + empID);
		EmployeeResult empResult = null;
		try {
			int iEmpID = Integer.parseInt(empID);
			Employee empModel = wmRepo.findEmployeeById(iEmpID);
			if (empModel != null) {
				empResult = new EmployeeResult(empModel.getIdemployee(),
						empModel.getName(), empModel.getAddress(),
						empModel.getContactno(), empModel.getEmail(),
						empModel.getDob(), empModel.getGender(),
						empModel.getNationality(), empModel.getDepartment(),
						empModel.getBranch(), empModel.getPosition(),
						empModel.getProjects(), uriInfo.getBaseUri());
			} else
				throw new WebApplicationException(Response
						.status(Status.NOT_FOUND)
						.entity(new StatusResponse(
								"No Employee found with Id :" + iEmpID))
						.type(MediaType.APPLICATION_JSON).build());

		} catch (NumberFormatException ex) {
			throw new WebApplicationException(Response
					.status(Status.BAD_REQUEST)
					.entity(new StatusResponse("NumberFormatException:"
							+ ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		}
		return empResult;
	}

	@GET
	@Path("/projects")
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectResource getProjects(@QueryParam("name") String name,
			@QueryParam("status") String status,
			@QueryParam("client") String client,
			@QueryParam("employeeName") String employeeName) {
		log.log(Level.INFO, "WMService : Fetching prjects");
		log.log(Level.INFO, "WMService : absolute URL"
				+ uriInfo.getAbsolutePath().toString());
		List<Project> projectList = wmRepo.findProjectsBy(name, status, client,
				employeeName);
		ProjectResource prjResource = new ProjectResource(projectList,
				uriInfo.getAbsolutePath());
		return prjResource;
	}

	@GET
	@Path("/projects/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectResult getProjectById(@PathParam("id") String projectID) {
		log.log(Level.INFO, "WMService : Fetching project " + projectID);
		log.log(Level.INFO, "WMService : absolute URL"
				+ uriInfo.getAbsolutePath().toString());

		ProjectResult projectResult = null;
		try {
			int iProjectID = Integer.parseInt(projectID);

			Project prjModel = wmRepo.findProjectById(iProjectID);
			if (prjModel != null) {
				projectResult = new ProjectResult(prjModel.getIdproject(),
						prjModel.getName(), prjModel.getStartdate(),
						prjModel.getEnddate(), prjModel.getStatus(),
						prjModel.getClient(), prjModel.getDescription(),
						prjModel.getEmployees(), uriInfo.getBaseUri());
			} else
				throw new WebApplicationException(Response
						.status(Status.NOT_FOUND)
						.entity(new StatusResponse("No project found with Id :"
								+ iProjectID)).type(MediaType.APPLICATION_JSON)
						.build());
		} catch (NumberFormatException ex) {
			throw new WebApplicationException(Response
					.status(Status.BAD_REQUEST)
					.entity(new StatusResponse("NumberFormatException:"
							+ ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		}
		return projectResult;
	}

	@POST
	@Path("/employees")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Employee addEmployee(Employee emp) {
		log.log(Level.INFO, "WMService : Adding employee name " + emp.getName());
		try {
			Employee employeePersisted = wmRepo.addEmployee(emp);
			return wmRepo.findEmployeeById(employeePersisted.getIdemployee());
		} catch (Exception ex) {
			throw new WebApplicationException(Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new StatusResponse(ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		}
	}

	@POST
	@Path("/projects")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Project addProject(Project project) {
		log.log(Level.INFO,
				"WMService : Adding project name " + project.getName());
		try {
			Project projectPersisted = wmRepo.addProject(project);
			return wmRepo.findProjectById(projectPersisted.getIdproject());
		} catch (Exception ex) {
			throw new WebApplicationException(Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new StatusResponse(ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@DELETE
	@Path("/employees/{id}")
	public Response deleteEmployee(@PathParam("id") int id) {
		log.log(Level.INFO, "WMService : Deleting Employee with id " + id);

		try {
			boolean successFlag = wmRepo.deleteEmployee(id);
			log.log(Level.INFO, "WMService : Deletion status " + successFlag);
			if (!successFlag) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new StatusResponse(
								"No project Id exists with Id :" + id))
						.type(MediaType.APPLICATION_JSON).build();
			}
			return Response.noContent().build();
		} catch (Exception ex) {
			throw new WebApplicationException(Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new StatusResponse(ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		}
	}

	@DELETE
	@Path("/projects/{id}")
	public Response deleteProject(@PathParam("id") int id) {
		log.log(Level.INFO, "WMService : Deleting Project with id " + id);

		try {
			boolean successFlag = wmRepo.deleteProject(id);
			log.log(Level.INFO, "WMService : Deletion status " + successFlag);
			if (!successFlag) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new StatusResponse(
								"No project Id exists with Id :" + id))
						.type(MediaType.APPLICATION_JSON).build();
			}
			return Response.noContent().build();
		}

		catch (Exception ex) {
			throw new WebApplicationException(Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new StatusResponse(ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		}
	}

	@POST
	@Path("/employee_project")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addEmployeeProjectAssociation(
			@FormParam("projectId") String projectId,
			@FormParam("employeeId") String employeeId) {
		log.log(Level.INFO,
				"WMService : addEmployeeProjectAssociation project Id  :"
						+ projectId + " employeeId :" + employeeId);
		try {
			int iProjectId = Integer.parseInt(projectId);
			int iEmployeeId = Integer.parseInt(employeeId);
			EmployeeProjectRelationStatus status = wmRepo.addProjectByEmployee(
					iProjectId, iEmployeeId);
			log.log(Level.INFO,
					"WMService : addEmployeeProjectAssociation Added : "
							+ status.toString());
			if (status == EmployeeProjectRelationStatus.SUCCESS) {
				return Response.status(Status.CREATED)
						.entity(new StatusResponse(status.toString()))
						.type(MediaType.APPLICATION_JSON).build();
			}
			return Response.status(Status.BAD_REQUEST)
					.entity(new StatusResponse(status.toString()))
					.type(MediaType.APPLICATION_JSON).build();
		} catch (NumberFormatException ex) {
			throw new WebApplicationException(Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new StatusResponse("NumberFormatException: "
							+ ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		} catch (Exception ex) {
			throw new WebApplicationException(Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new StatusResponse(ex.getMessage()))
					.type(MediaType.APPLICATION_JSON).build());
		}

	}
}
