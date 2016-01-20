package in.co.init.workmatrix.model;

import java.util.Date;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Employee.class)
public abstract class Employee_ {

	public static volatile SingularAttribute<Employee, String> position;
	public static volatile SetAttribute<Employee, Project> projects;
	public static volatile SingularAttribute<Employee, String> nationality;
	public static volatile SingularAttribute<Employee, String> email;
	public static volatile SingularAttribute<Employee, String> address;
	public static volatile SingularAttribute<Employee, String> department;
	public static volatile SingularAttribute<Employee, Date> dob;
	public static volatile SingularAttribute<Employee, String> name;
	public static volatile SingularAttribute<Employee, String> branch;
	public static volatile SingularAttribute<Employee, Character> gender;
	public static volatile SingularAttribute<Employee, String> contactno;
	public static volatile SingularAttribute<Employee, Integer> idemployee;

}

