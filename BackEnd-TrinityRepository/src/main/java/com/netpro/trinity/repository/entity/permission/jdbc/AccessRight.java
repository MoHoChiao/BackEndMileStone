package com.netpro.trinity.repository.entity.permission.jdbc;

public class AccessRight {
	private String peopleuid;
	private String objectuid;
	private String view;
	private String add;
	private String delete;
	private String edit;
	private String run;
	private String reRun;
	private String grant;
	private String import_export;
	
	public String getPeopleuid() {
		return peopleuid;
	}

	public void setPeopleuid(String peopleuid) {
		this.peopleuid = peopleuid;
	}

	public String getObjectuid() {
		return objectuid;
	}

	public void setObjectuid(String objectuid) {
		this.objectuid = objectuid;
	}
	
	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getEdit() {
		return edit;
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public String getReRun() {
		return reRun;
	}

	public void setReRun(String reRun) {
		this.reRun = reRun;
	}

	public String getGrant() {
		return grant;
	}

	public void setGrant(String grant) {
		this.grant = grant;
	}

	public String getImport_export() {
		return import_export;
	}

	public void setImport_export(String import_export) {
		this.import_export = import_export;
	}
	
	@Override
	public String toString() {
		return "AccessRight{" + "peopleuid='" + peopleuid + '\'' + ", "
				+ "objectuid='" + objectuid + '\'' + ", "
				+ "flag='" + view+add+delete+edit+run+reRun+grant+import_export + '\'' + '}';
	}
}