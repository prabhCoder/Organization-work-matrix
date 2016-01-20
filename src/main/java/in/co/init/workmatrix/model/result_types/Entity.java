package in.co.init.workmatrix.model.result_types;

public enum Entity {

	PROJECT("project"), EMPLOYEE("employee");

	private String entityName;

	private Entity(String e) {
		entityName = e;
	}

	public String getEntityName() {
		return entityName;
	}
}
