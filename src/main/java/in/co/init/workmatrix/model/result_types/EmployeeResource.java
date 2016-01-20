package in.co.init.workmatrix.model.result_types;

import in.co.init.workmatrix.model.Employee;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents JSON result for list of employee resource URLS
 * 
 */

public class EmployeeResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private Collection<ResourceLink> links;

	public EmployeeResource(Collection<Employee> employeeList, URI absoluteURI) {
		this.links = new ArrayList<ResourceLink>();
		mapEmployeesToResourceLinks(employeeList, absoluteURI);
	}

	public Collection<ResourceLink> getLinks() {
		return links;
	}

	private void mapEmployeesToResourceLinks(Collection<Employee> employeeList,
			URI absoluteURI) {
		try {
			if (employeeList != null && employeeList.size() > 0) {
				for (Employee emp : employeeList) {
					ResourceLink link = new ResourceLink(
							Entity.EMPLOYEE.getEntityName(), emp.getName(),
							new URI(
									absoluteURI.toString()
											+ (absoluteURI.toString().endsWith(
													"/") ? "" : "/")
											+ emp.getIdemployee()));
					getLinks().add(link);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
