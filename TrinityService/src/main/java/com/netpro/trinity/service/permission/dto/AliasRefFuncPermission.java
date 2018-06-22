package com.netpro.trinity.service.permission.dto;

public class AliasRefFuncPermission {
	private boolean view;
	private boolean add;
	private boolean modify;
	private boolean delete;
	
	public AliasRefFuncPermission() {
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
		return "AliasRefFunc{" + "view='" + view + '\'' + ", "
				+ "add='" + add + '\'' + ", "
				+ "modify='" + modify + '\'' + ", "
				+ "delete='" + delete + '\'' + '}';
	}
}