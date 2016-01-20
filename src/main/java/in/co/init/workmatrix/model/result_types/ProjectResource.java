package in.co.init.workmatrix.model.result_types;

import in.co.init.workmatrix.model.Project;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents JSON result for list of project resource URLS
 * 
 */
public class ProjectResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ResourceLink> links;

	public ProjectResource(Collection<Project> projectList, URI absoluteURI) {
		this.links = new ArrayList<ResourceLink>();
		mapProjectsToResourceLinks(projectList, absoluteURI);
	}

	public List<ResourceLink> getLinks() {
		return links;
	}

	private void mapProjectsToResourceLinks(Collection<Project> projectList,
			URI absoluteURI) {
		try {
			if (projectList != null && projectList.size() > 0) {
				for (Project prj : projectList) {
					ResourceLink link = new ResourceLink(
							Entity.PROJECT.getEntityName(), prj.getName(),
							new URI(
									absoluteURI.toString()
											+ (absoluteURI.toString().endsWith(
													"/") ? "" : "/")
											+ prj.getIdproject()));
					getLinks().add(link);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
