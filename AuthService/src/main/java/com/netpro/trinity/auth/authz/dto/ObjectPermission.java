package com.netpro.trinity.auth.authz.dto;

public class ObjectPermission {
	private String objuid;
	private boolean view;
	private boolean add;
	private boolean modify;
	private boolean delete;
	private boolean run;
	private boolean rerun;
	private boolean grant;
	
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
	
	public boolean getView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public boolean getAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean getModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	public boolean getDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public boolean isRerun() {
		return rerun;
	}

	public void setRerun(boolean rerun) {
		this.rerun = rerun;
	}

	public boolean isGrant() {
		return grant;
	}

	public void setGrant(boolean grant) {
		this.grant = grant;
	}
	
	@Override
	public String toString() {
		return "AgentFunc{" + "objuid='" + objuid + '\'' + ", "
				+ "view='" + add + '\'' + ", "
				+ "add='" + add + '\'' + ", "
				+ "modify='" + modify + '\'' + ", "
				+ "delete='" + delete + '\'' + ", "
				+ "run='" + run + '\'' + ", "
				+ "rerun='" + rerun + '\'' + ", "
				+ "grant='" + grant + '\'' + '}';
	}
}