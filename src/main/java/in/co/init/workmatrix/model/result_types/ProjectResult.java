package in.co.init.workmatrix.model.result_types;

import in.co.init.workmatrix.model.Employee;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

/**
 * Represents JSON result for specific project and associated employees resource
 * URLS
 * 
 */
public class ProjectResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idproject;
	private String name;
	private Date startdate;
	private Date enddate;
	private String status;
	private String client;
	private String description;
	private EmployeeResource employeeResource;

	public ProjectResult(Integer idproject, String name, Date startdate,
			Date enddate, String status, String client, String description,
			Set<Employee> employees, URI baseURI) {
		super();
		this.idproject = idproject;
		this.name = name;
		this.startdate = startdate;
		this.enddate = enddate;
		this.status = status;
		this.client = client;
		this.description = description;
		this.employeeResource = mapEmployeesToEmployeeResource(employees,
				baseURI);
	}

	public Integer getIdproject() {
		return idproject;
	}

	public String getName() {
		return name;
	}

	public Date getStartdate() {
		return startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public String getStatus() {
		return status;
	}

	public String getClient() {
		return client;
	}

	public String getDescription() {
		return description;
	}

	public EmployeeResource getEmployeeResource() {
		return employeeResource;
	}

	private EmployeeResource mapEmployeesToEmployeeResource(
			Set<Employee> employee, URI baseURI) {
		EmployeeResource employeeResource = null;
		try {
			employeeResource = new EmployeeResource(employee, new URI(
					baseURI.toString() + Entity.EMPLOYEE.getEntityName()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return employeeResource;
	}

}
