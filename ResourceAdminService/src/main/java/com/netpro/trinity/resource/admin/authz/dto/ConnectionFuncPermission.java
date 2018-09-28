package com.netpro.trinity.resource.admin.authz.dto;

public class ConnectionFuncPermission implements IFuncPermission{
	private Boolean view;
	private Boolean add;
	private Boolean modify;
	private Boolean delete;
	
	public ConnectionFuncPermission() {
		this.view = false;
		this.add = false;
		this.modify = false;
		this.delete = false;
	}
	
	@Override
	public Boolean getView() {
		return view;
	}

	@Override
	public void setView(Boolean view) {
		this.view = view;
	}

	@Override
	public Boolean getAdd() {
		return add;
	}

	@Override
	public void setAdd(Boolean add) {
		this.add = add;
	}

	@Override
	public Boolean getModify() {
		return modify;
	}
	
	@Override
	public void setModify(Boolean modify) {
		this.modify = modify;
	}

	@Override
	public Boolean getDelete() {
		return delete;
	}

	@Override
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	
	@Override
	public String toString() {
		return "ConnectionFunc{" + "view='" + view + '\'' + ", "
				+ "add='" + add + '\'' + ", "
				+ "modify='" + modify + '\'' + ", "
				+ "delete='" + delete + '\'' + '}';
	}
}