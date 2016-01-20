package in.co.init.workmatrix.controller;

import in.co.init.workmatrix.data.WorkMatrixRepository;
import in.co.init.workmatrix.model.Project;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean
@RequestScoped
public class ProjectConvertor implements Converter {

	@EJB
	private WorkMatrixRepository wmRepo;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 == null || arg2.isEmpty()) {
			return null;
		}
		System.out.println("ProjectConvertor :getAsObject :id"
				+ Integer.valueOf(arg2));
		Project project = wmRepo.findProjectById(Integer.valueOf(arg2));
		System.out.println("ProjectConvertor :getAsObject :" + project);
		return wmRepo.findProjectById(Integer.valueOf(arg2));

	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		System.out.println("ProjectConvertor :getAsString :" + arg2);
		String projectId = "";
		if (arg2 instanceof Project) {

			projectId = String.valueOf(((Project) arg2).getIdproject());
		} else if (arg2 instanceof Integer) {
			projectId = String.valueOf((Integer) arg2);
		}
		System.out.println("ProjectConvertor :getAsString :" + projectId);
		return projectId;
	}
}
