package in.co.init.workmatrix.model.result_types;

import in.co.init.workmatrix.model.Project;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

/**
 * Represents JSON result for specific Employee and associated projects resource
 * URLS
 * 
 */
public class EmployeeResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idemployee;
	private String name;
	private String address;
	private String contactno;
	private String email;
	private Date dob;
	private char gender;
	private String nationality;
	private String department;
	private String branch;
	private String position;
	private ProjectResource projectResource;

	public EmployeeResult(Integer idemployee, String name, String address,
			String contactno, String email, Date dob, char gender,
			String nationality, String department, String branch,
			String position, Set<Project> projects, URI baseURI) {
		super();
		this.idemployee = idemployee;
		this.name = name;
		this.address = address;
		this.contactno = contactno;
		this.email = email;
		this.dob = dob;
		this.gender = gender;
		this.nationality = nationality;
		this.department = department;
		this.branch = branch;
		this.position = position;
		this.projectResource = mapProjectsToProjectResource(projects, baseURI);
	}

	public Integer getIdemployee() {
		return idemployee;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getContactno() {
		return contactno;
	}

	public String getEmail() {
		return email;
	}

	public Date getDob() {
		return dob;
	}

	public char getGender() {
		return gender;
	}

	public String getNationality() {
		return nationality;
	}

	public String getDepartment() {
		return department;
	}

	public String getBranch() {
		return branch;
	}

	public String getPosition() {
		return position;
	}

	public ProjectResource getProjectResource() {
		return projectResource;
	}

	private ProjectResource mapProjectsToProjectResource(Set<Project> projects,
			URI baseURI) {
		ProjectResource prjResource = null;
		try {
			prjResource = new ProjectResource(projects, new URI(
					baseURI.toString() + Entity.PROJECT.getEntityName()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return prjResource;
	}
}
