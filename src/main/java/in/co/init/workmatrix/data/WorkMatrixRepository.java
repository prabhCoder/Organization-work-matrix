package in.co.init.workmatrix.data;

import in.co.init.workmatrix.model.Employee;
import in.co.init.workmatrix.model.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class WorkMatrixRepository implements Serializable {
	@Inject
	EntityManager em;

	@Inject
	Logger log;
	private static final long serialVersionUID = 1L;

	public enum EmployeeProjectRelationStatus {
		EMPLOYEE_NOTEXIST, PROJECT_NOTEXIST, EMPLOYEE_AND_PROJECT_NOTEXIST, EXISTS, SUCCESS
	}

	public List<Employee> findEmployeeAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
		Root<Employee> employee = criteria.from(Employee.class);
		criteria.select(employee).orderBy(cb.asc(employee.get("idemployee")));
		List<Employee> employees = em.createQuery(criteria).getResultList();
		log.log(Level.INFO,
				"WorkMatrixRepository :Employees:" + employees.size());
		return employees;
	}

	private List<Project> findProjectAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> criteria = cb.createQuery(Project.class);
		Root<Project> project = criteria.from(Project.class);
		criteria.select(project).orderBy(cb.asc(project.get("idproject")));
		List<Project> projects = em.createQuery(criteria).getResultList();
		log.log(Level.INFO, "WorkMatrixRepository :Projects:" + projects.size());
		return projects;
	}

	public Employee findEmployeeById(int id) {
		Employee e = em.find(Employee.class, id);
		return e;
	}

	public Project findProjectById(int id) {
		Project p = em.find(Project.class, id);
		return p;
	}

	@SuppressWarnings("unchecked")
	public List<Project> findProjectsBy(String name, String status,
			String client, String employeeName) {
		boolean criteriaFlag = false;
		List<Project> projectResultList = null;
		String searchCriteriaString = "";
		if (name != null && !name.equals("")) {
			criteriaFlag = true;
			searchCriteriaString += " p.name like \'%" + name + "%\'";
		}
		if (status != null && !status.equals("")) {
			if (criteriaFlag) {
				searchCriteriaString += " and p.status=\'" + status + "\'";
			} else {
				searchCriteriaString += " p.status=\'" + status + "\'";
			}
			criteriaFlag = true;
		}

		if (client != null && !client.equals("")) {
			if (criteriaFlag) {
				searchCriteriaString += " and p.client like \'%" + client
						+ "%\'";
			} else {
				searchCriteriaString += " p.client like \'%" + client + "%\'";
			}
			criteriaFlag = true;
		}

		if (employeeName != null && !employeeName.equals("")) {
			if (criteriaFlag) {
				searchCriteriaString += " and e.name like \'%" + employeeName
						+ "%\'";
			} else {
				searchCriteriaString += " e.name like \'%" + employeeName
						+ "%\'";
			}
			criteriaFlag = true;
		}

		log.log(Level.INFO,
				"WorkMatrixRepository :findProjectsBy :CriteriaBuild: "
						+ searchCriteriaString);

		if (criteriaFlag) {
			String searchQueryString = null;
			if (employeeName != null && !employeeName.equals(""))

				searchQueryString = "Select distinct p from Project p join p.employees e where"
						+ searchCriteriaString;
			else
				searchQueryString = "Select distinct p from Project p where"
						+ searchCriteriaString;
			projectResultList = em.createQuery(searchQueryString)
					.getResultList();
			log.log(Level.INFO,
					"WorkMatrixRepository :findProjectsBy :Projects: "
							+ projectResultList.size());
		} else
			projectResultList = findProjectAll();

		return projectResultList == null ? new ArrayList<Project>()
				: projectResultList;
	}

	@SuppressWarnings("unchecked")
	public List<Employee> findEmployeeBy(String name, String email,
			String branch, String department, String position,
			String projectName) {
		boolean criteriaFlag = false;
		List<Employee> employeeResultList = null;
		String searchCriteriaString = "";
		if (name != null && name.length() > 0) {
			if (criteriaFlag)
				searchCriteriaString += " and e.name like \'%" + name + "%\'";
			else
				searchCriteriaString += "e.name like \'%" + name + "%\'";
			criteriaFlag = true;
		}
		if (email != null && email.length() > 0) {
			if (criteriaFlag)
				searchCriteriaString += " and e.email like \'%" + email + "%\'";
			else
				searchCriteriaString += "e.email like \'%" + email + "%\'";
			criteriaFlag = true;
		}
		if (branch != null && branch.length() > 0) {
			if (criteriaFlag)
				searchCriteriaString += " and e.branch like \'%" + branch
						+ "%\'";
			else
				searchCriteriaString += "e.branch like \'%" + branch + "%\'";
			criteriaFlag = true;
		}
		if (department != null && department.length() > 0) {
			if (criteriaFlag)
				searchCriteriaString += " and e.department like \'%"
						+ department + "%\'";
			else
				searchCriteriaString += "e.department like \'%" + department
						+ "%\'";
			criteriaFlag = true;
		}
		if (position != null && position.length() > 0) {
			if (criteriaFlag)
				searchCriteriaString += " and e.position like \'%" + position
						+ "%\'";
			else
				searchCriteriaString += "e.position like \'%" + position
						+ "%\'";
			criteriaFlag = true;
		}
		if (projectName != null && projectName.length() > 0) {
			if (criteriaFlag)
				searchCriteriaString += " and p.name like \'%" + projectName
						+ "%\'";
			else
				searchCriteriaString += "p.name like \'%" + projectName + "%\'";
			criteriaFlag = true;
		}
		if (criteriaFlag) {
			String searchQueryString = null;
			if (projectName != null && projectName.length() > 0) {
				searchQueryString = "select distinct e from Employee e join e.projects p where "
						+ searchCriteriaString;
			} else {
				searchQueryString = "select distinct e from Employee e where "
						+ searchCriteriaString;

			}
			employeeResultList = em.createQuery(searchQueryString)
					.getResultList();
		} else {
			employeeResultList = findEmployeeAll();
		}

		return employeeResultList;

	}

	public Employee addEmployee(Employee emp) {
		log.log(Level.INFO,
				"WorkMatrixRepository :addEmployee: Adding " + emp.getName());
		Employee empPersisted = em.merge(emp);
		log.log(Level.INFO, "WorkMatrixRepository :addEmployee: added"
				+ (empPersisted != null ? empPersisted.getIdemployee()
						: empPersisted));
		return empPersisted;
	}

	public Project addProject(Project project) {
		log.log(Level.INFO, "WorkMatrixRepository :addProject: Adding "
				+ project.getName());
		Project projectPersisted = em.merge(project);
		log.log(Level.INFO, "WorkMatrixRepository :addProject: added"
				+ (projectPersisted != null ? projectPersisted.getIdproject()
						: projectPersisted));
		return projectPersisted;
	}

	public EmployeeProjectRelationStatus addProjectByEmployee(int projectId,
			int employeeId) {

		log.log(Level.INFO, "WorkMatrixRepository :addProjectByEmployee");
		Employee employee = findEmployeeById(employeeId);
		Project project = findProjectById(projectId);

		log.log(Level.INFO,
				"WorkMatrixRepository :addProjectByEmployee employee : "
						+ employee);
		log.log(Level.INFO,
				"WorkMatrixRepository :addProjectByEmployee project : "
						+ project);
		if (employee == null && project == null)
			return EmployeeProjectRelationStatus.EMPLOYEE_AND_PROJECT_NOTEXIST;
		if (employee == null)
			return EmployeeProjectRelationStatus.EMPLOYEE_NOTEXIST;
		if (project == null)
			return EmployeeProjectRelationStatus.PROJECT_NOTEXIST;
		if (employee.getProjects().contains(project))
			return EmployeeProjectRelationStatus.EXISTS;
		else {
			employee.getProjects().add(project);
			em.persist(employee);
			return EmployeeProjectRelationStatus.SUCCESS;
		}
	}

	public boolean deleteEmployee(int id) {
		Employee emp = findEmployeeById(id);
		if (emp != null) {
			List<Project> projects = findProjectAll();
			log.log(Level.INFO, "WorkMatrixRepository :deleteEmployee :");

			for (Project project : projects) {
				for (Employee employee : project.getEmployees())
					log.log(Level.INFO, "project :" + project.getIdproject()
							+ " employee :" + employee.getIdemployee());
				project.getEmployees().remove(emp);
			}
			em.remove(emp);
			return true;
		}
		return false;
	}

	public boolean deleteProject(int id) {
		Project prj = findProjectById(id);
		if (prj != null) {
			List<Employee> employees = findEmployeeAll();
			log.log(Level.INFO, "WorkMatrixRepository :deleteproject :");
			for (Employee employee : employees) {
				for (Project project : employee.getProjects())
					log.log(Level.INFO, "Employee :" + employee.getIdemployee()
							+ " project :" + project.getIdproject());
				employee.getProjects().remove(prj);
			}
			em.remove(prj);
			return true;
		}
		return false;
	}

}
