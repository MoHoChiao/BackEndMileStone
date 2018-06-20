package com.netpro.trinity.auth.authz.dto;

public class ExternalRuleFuncPermission {
	private boolean view;
	private boolean add;
	private boolean modify;
	private boolean delete;
	
	public ExternalRuleFuncPermission() {
		this.view = false;
		this.add = false;
		this.modify = false;
		this.delete = false;
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
	
	@Override
	public String toString() {
		return "ExternalRuleFunc{" + "view='" + view + '\'' + ", "
				+ "add='" + add + '\'' + ", "
				+ "modify='" + modify + '\'' + ", "
				+ "delete='" + delete + '\'' + '}';
	}
}