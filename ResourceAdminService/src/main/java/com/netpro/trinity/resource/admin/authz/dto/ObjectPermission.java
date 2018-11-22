package com.netpro.trinity.resource.admin.authz.dto;

public class ObjectPermission {
	private String objuid;
	private Boolean view;
	private Boolean add;
	private Boolean modify;
	private Boolean delete;
	private Boolean run;
	private Boolean rerun;
	private Boolean grant;
	
	public ObjectPermission() {
		this.view = false;
		this.add = false;
		this.modify = false;
		this.delete = false;
		this.run = false;
		this.rerun = false;
		this.grant = false;
	}
	
	public String getObjuid() {
		return objuid;
	}

	public void setObjuid(String objuid) {
		this.objuid = objuid;
	}
	
	public Boolean getView() {
		return view;
	}

	public void setView(Boolean view) {
		this.view = view;
	}

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	public Boolean getModify() {
		return modify;
	}

	public void setModify(Boolean modify) {
		this.modify = modify;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	
	public Boolean getRun() {
		return run;
	}

	public void setRun(Boolean run) {
		this.run = run;
	}

	public Boolean getRerun() {
		return rerun;
	}

	public void setRerun(Boolean rerun) {
		this.rerun = rerun;
	}

	public Boolean getGrant() {
		return grant;
	}

	public void setGrant(Boolean grant) {
		this.grant = grant;
	}
	
	@Override
	public String toString() {
		return "{" + "objuid='" + objuid + '\'' + ", "
				+ "view='" + add + '\'' + ", "
				+ "add='" + add + '\'' + ", "
				+ "modify='" + modify + '\'' + ", "
				+ "delete='" + delete + '\'' + ", "
				+ "run='" + run + '\'' + ", "
				+ "rerun='" + rerun + '\'' + ", "
				+ "grant='" + grant + '\'' + '}';
	}
}