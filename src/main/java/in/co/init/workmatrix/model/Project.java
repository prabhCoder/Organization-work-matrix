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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "project")
public class Project implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idproject;
	private String name;
	private Date startdate;
	private Date enddate;
	private String status;
	private String client;
	private String description;
	private Set<Employee> employees = new HashSet<Employee>(0);

	public Project() {
	}

	public Project(String name, String status, String client) {
		this.name = name;
		this.status = status;
		this.client = client;
	}

	public Project(String name, Date startdate, Date enddate, String status,
			String client, String description, Set<Employee> employees) {
		this.name = name;
		this.startdate = startdate;
		this.enddate = enddate;
		this.status = status;
		this.client = client;
		this.description = description;
		this.employees = employees;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idproject", unique = true, nullable = false)
	public Integer getIdproject() {
		return this.idproject;
	}

	public void setIdproject(Integer idproject) {
		this.idproject = idproject;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "startdate", length = 10)
	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "enddate", length = 10)
	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	@Column(name = "status", nullable = false, length = 45)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "client", nullable = false)
	public String getClient() {
		return this.client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.PERSIST }, fetch = FetchType.EAGER, mappedBy = "projects")
	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

}
