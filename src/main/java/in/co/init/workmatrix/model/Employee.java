package in.co.init.workmatrix.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "employee", uniqueConstraints = {
		@UniqueConstraint(columnNames = "contactno"),
		@UniqueConstraint(columnNames = "email") })
public class Employee implements java.io.Serializable {

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
	private Set<Project> projects = new HashSet<Project>(0);

	public Employee() {
	}

	public Employee(String name, String address, String email, Date dob,
			char gender, String department, String branch, String position) {
		this.name = name;
		this.address = address;
		this.email = email;
		this.dob = dob;
		this.gender = gender;
		this.department = department;
		this.branch = branch;
		this.position = position;
	}

	public Employee(String name, String address, String contactno,
			String email, Date dob, char gender, String nationality,
			String department, String branch, String position,
			Set<Project> projects) {
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
		this.projects = projects;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idemployee", unique = true, nullable = false)
	public Integer getIdemployee() {
		return this.idemployee;
	}

	public void setIdemployee(Integer idemployee) {
		this.idemployee = idemployee;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "address", nullable = false)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "contactno", unique = true, length = 20)
	@Size(min = 1, max = 20)
	// @Pattern(regexp = "(?<!\\s+)", message = "Contact No can't be empty")
	public String getContactno() {
		return this.contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	@Column(name = "email", unique = true, nullable = false)
	@NotEmpty(message = "EmailID can't be empty or null")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dob", nullable = false, length = 10)
	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Column(name = "gender", nullable = false, length = 1)
	public char getGender() {
		return this.gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	@Column(name = "nationality")
	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Column(name = "department", nullable = false)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "branch", nullable = false)
	public String getBranch() {
		return this.branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Column(name = "position", nullable = false)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH,
			CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "employee_project", joinColumns = { @JoinColumn(name = "idemployee", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "idproject", nullable = false, updatable = false) })
	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

}
