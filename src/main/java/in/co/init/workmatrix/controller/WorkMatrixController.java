package in.co.init.workmatrix.controller;

import in.co.init.workmatrix.data.WorkMatrixRepository;
import in.co.init.workmatrix.model.Employee;
import in.co.init.workmatrix.model.Project;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

@Named
@SessionScoped
public class WorkMatrixController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private WorkMatrixRepository wmRepository;

	@Inject
	private Event<Employee> employeeEvent;

	private List<Employee> employeeList;
	private List<Employee> filteredEmployeeList;

	private List<Project> projectList;
	private List<Project> filteredProjectList;

	private OperationType operation;

	private Employee selectedEmployee;
	private Project selectedProject;
	private char maleChar = 'M';
	private char femaleChar = 'F';
	private List<String> branchList;
	private List<String> departmentList;
	private List<String> projectStatusList;
	private List<Project> selectedProjectsToBeAdded;
	private Project selectedProjectToBeDeleted;

	/*
	 * Event Observer method is called whenever event is fired on object
	 * Employee
	 */
	public void onEmployeeListChanged(@Observes final Employee emp) {
		System.out.println("onEmployeeListChanged calling init() again");
		init();
	}

	public void init() {
		System.out.println("WMController : PostContruct init()");

		employeeList = wmRepository.findEmployeeAll();
		projectList = wmRepository.findProjectAll();

		branchList = wmRepository.getBranchList();
		departmentList = wmRepository.getDepartmentList();
		projectStatusList = wmRepository.getProjectStatusList();

	}

	public void isSelectedEmployeeNull(ComponentSystemEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();
		if (selectedEmployee == null) {

			try {
				fc.getExternalContext().redirect("listView.xhtml");
			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void refreshList(ComponentSystemEvent event) {

		init();
	}

	public void isSelectedProjectNull(ComponentSystemEvent event) {

		FacesContext fc = FacesContext.getCurrentInstance();
		if (selectedProject == null) {

			try {
				fc.getExternalContext().redirect("listView.xhtml");
			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public boolean filterByName(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}

		String name = value.toString().toUpperCase();
		filterText = filterText.toUpperCase();

		if (name.contains(filterText)) {
			return true;
		} else {
			return false;
		}
	}

	public void handleAddEmployee(ActionEvent event) {
		System.out.println("WMController :  handleAddEmployee ");
		selectedEmployee = new Employee();
	}

	public void handleAddProject(ActionEvent event) {
		System.out.println("WMController :  handleAddProject :");
		selectedProject = new Project();
	}

	public String saveEmployee() {
		System.out.println("WMController :  saveEmployee :"
				+ selectedEmployee.getName() + " Projects No: "
				+ selectedEmployee.getProjects().size());
		Employee empMerged = wmRepository.mergeEmployee(selectedEmployee);
		if (empMerged != null) {
			addMessage(
					FacesMessage.SEVERITY_INFO,
					"Employee : "
							+ selectedEmployee.getName()
							+ (operation == OperationType.UPDATE ? " successfully updated."
									: " successfully added."));
		}
		init();
		return "listView?faces-redirect=true";

	}

	public void validateDateOfBirth(FacesContext context,
			UIComponent component, Object value) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal1.add(Calendar.YEAR, -60);
		Date dateBefore60Years = cal1.getTime();
		System.out.println(dateFormat.format(cal1.getTime()));

		cal2.add(Calendar.YEAR, -20);
		Date datebefore20years = cal2.getTime();
		System.out.println(dateFormat.format(cal2.getTime()));

		Date dob = (Date) value;

		if ((!dob.before(datebefore20years)) || (!dob.after(dateBefore60Years))) {
			FacesContext.getCurrentInstance().addMessage(
					component.getClientId(context),
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"invalid dob", "invalid dob"));
			FacesContext.getCurrentInstance().renderResponse();
		}

	}

	public void validateContactNumber(FacesContext context,
			UIComponent component, Object value) {
		String contactno = value.toString();
		contactno = contactno.trim();
		if (contactno != null && !contactno.isEmpty()) {
			Pattern pattern = Pattern.compile("^\\d{10,14}$");
			Matcher matcher = pattern.matcher(contactno);

			if (matcher.matches()) {
				System.out.println("Phone Number Valid");
			} else {
				FacesContext.getCurrentInstance().addMessage(
						component.getClientId(context),
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"invalid contact number",
								"invalid contact number"));
				System.out
						.println("Phone Number must be in the form XXXXXXXXXX");
				FacesContext.getCurrentInstance().renderResponse();
			}
		} else {
			return;
		}
	}

	public void dateRangeValidator(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {
			return;
		}
		Object startDateValue = component.getAttributes().get("startdate");
		if (startDateValue == null) {
			value = null;
			return;
		}
		Date startDate = (Date) startDateValue;
		Date endDate = (Date) value;
		if (endDate.before(startDate)) {
			FacesContext.getCurrentInstance().addMessage(
					component.getClientId(context),
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"invalid end date", "invalid end date"));
			FacesContext.getCurrentInstance().renderResponse();

		}
	}

	public String saveProject() {

		System.out.println("WMController :  saveProject :"
				+ selectedProject.getName());
		Project projectMerged = wmRepository.addProject(selectedProject);
		if (projectMerged != null) {
			addMessage(
					FacesMessage.SEVERITY_INFO,
					"Project : "
							+ selectedProject.getName()
							+ (operation == OperationType.UPDATE ? " successfully updated."
									: " successfully added."));

		}

		init();
		return "listView?faces-redirect=true";

	}

	public String deleteEmployee() {
		int id = selectedEmployee.getIdemployee();
		boolean success = wmRepository.deleteEmployee(id);
		if (success) {
			addMessage(FacesMessage.SEVERITY_INFO, selectedEmployee.getName()
					+ " successfully deleted.");
			employeeList.remove(selectedEmployee);
			// employeeEvent.fire(selectedEmployee);
		}
		return "listView?faces-redirect=true";
	}

	public String deleteProject() {
		int id = selectedProject.getIdproject();
		boolean success = wmRepository.deleteProject(id);
		if (success) {
			addMessage(FacesMessage.SEVERITY_INFO, selectedProject.getName()
					+ " successfully deleted.");
			projectList.remove(selectedProject);
		}
		return "listView?faces-redirect=true";
	}

	public List<Project> getProjectsToBeAdded() {
		List<Project> projectsToBeAdded = new ArrayList<Project>(projectList);
		System.out.println("WMController : getProjectsToBeAdded"
				+ projectsToBeAdded.size());
		if (selectedEmployee != null) {
			projectsToBeAdded.removeAll(selectedEmployee.getProjects());

			System.out
					.println("WMController : getProjectsToBeAdded : removed : new size="
							+ projectsToBeAdded.size());
		}
		return projectsToBeAdded;
	}

	public void handleAddProjectListChange(AjaxBehaviorEvent event) {
		System.out
				.println("WMController : handleProjectListChange: selectedProjectstobeadded: "
						+ selectedProjectsToBeAdded);

		if (selectedProjectsToBeAdded != null) {
			selectedEmployee.getProjects().addAll(selectedProjectsToBeAdded);
			selectedProjectsToBeAdded.clear();
		}

	}

	public void deleteProjectFromEmployee() {
		System.out
				.println("WMController : handleDeleteProjectFromEmployee: selectedProjecttobeDeleted : "
						+ selectedProjectToBeDeleted);

		if (selectedProjectToBeDeleted != null) {
			selectedEmployee.getProjects().remove(selectedProjectToBeDeleted);
			if (selectedProjectsToBeAdded != null)
				selectedProjectsToBeAdded.clear();
		}

	}

	public void handleBackAction(ActionEvent event) {
		System.out.println("WMController :handleBackAction");
		/*
		 * selectedEmployee = null; selectedProject = null;
		 */
		init();
	}

	public void addMessage(Severity severity, String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severity, msg, null));
		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.setKeepMessages(true);
	}

	public Project getSelectedProjectToBeDeleted() {
		return selectedProjectToBeDeleted;
	}

	public void setSelectedProjectToBeDeleted(Project selectedProjectToBeDeleted) {
		this.selectedProjectToBeDeleted = selectedProjectToBeDeleted;
	}

	public List<Project> getSelectedProjectsToBeAdded() {
		System.out.println("GET selectedProjectsToBeAdded"
				+ selectedProjectsToBeAdded);
		return selectedProjectsToBeAdded;
	}

	public void setSelectedProjectsToBeAdded(
			List<Project> selectedProjectsToBeAdded) {
		System.out.println("SET selectedProjectsToBeAdded"
				+ selectedProjectsToBeAdded);
		this.selectedProjectsToBeAdded = selectedProjectsToBeAdded;
	}

	public OperationType getOperation() {
		return operation;
	}

	public void setOperation(OperationType operation) {
		this.operation = operation;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public Employee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public char getMaleChar() {
		return maleChar;
	}

	public char getFemaleChar() {
		return femaleChar;
	}

	public List<String> getBranchList() {
		return branchList;
	}

	public List<String> getDepartmentList() {
		return departmentList;
	}

	public List<String> getProjectStatusList() {
		return projectStatusList;
	}

	public List<Employee> getFilteredEmployeeList() {
		return filteredEmployeeList;
	}

	public void setFilteredEmployeeList(List<Employee> filteredEmployeeList) {
		this.filteredEmployeeList = filteredEmployeeList;
	}

	public List<Project> getFilteredProjectList() {
		return filteredProjectList;
	}

	public void setFilteredProjectList(List<Project> filteredProjectList) {
		this.filteredProjectList = filteredProjectList;
	}

	public void validateEmail(FacesContext context, UIComponent component,
			Object value) {
		String email = value.toString();
		Employee empByEmail = wmRepository.findEmployeeByEmail(email);
		if (empByEmail != null) {

			if (empByEmail.getIdemployee() != selectedEmployee.getIdemployee()
					|| operation == OperationType.ADD) {
				System.out.println("operation :" + operation);
				FacesContext.getCurrentInstance().addMessage(
						component.getClientId(context),
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Email exists", "Email exists"));
				FacesContext.getCurrentInstance().renderResponse();
			}
		}

	}

	public void validateName(FacesContext context, UIComponent component,
			Object value) {
		String name = value.toString();
		Project project = wmRepository.findProjectByName(name);
		if (project != null) {

			if (!(project.getIdproject() == selectedProject.getIdproject())
					|| operation == OperationType.ADD) {
				FacesContext.getCurrentInstance().addMessage(
						component.getClientId(context),
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								" project Name exists", "Project Name exists"));
				FacesContext.getCurrentInstance().renderResponse();
			}

		}
	}

	private void resetProjectEndDate() {
		if (selectedProject.getStartdate() == null
				|| selectedProject.getEnddate() != null
				&& selectedProject.getEnddate().before(
						selectedProject.getStartdate())) {
			selectedProject.setEnddate(null);
		}
	}

	public void handleStartDateSelect(SelectEvent event) {
		resetProjectEndDate();
	}

	public void handleStartDateChange(AjaxBehaviorEvent event) {

		resetProjectEndDate();
	}

	public void handleEndDateChange(AjaxBehaviorEvent event) {
		resetProjectEndDate();
	}
}
