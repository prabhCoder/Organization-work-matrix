package in.co.init.workmatrix.model;

import java.util.Date;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Project.class)
public abstract class Project_ {

	public static volatile SingularAttribute<Project, Date> startdate;
	public static volatile SingularAttribute<Project, String> client;
	public static volatile SingularAttribute<Project, String> status;
	public static volatile SingularAttribute<Project, String> description;
	public static volatile SingularAttribute<Project, Integer> idproject;
	public static volatile SingularAttribute<Project, String> name;
	public static volatile SingularAttribute<Project, Date> enddate;
	public static volatile SetAttribute<Project, Employee> employees;

}

