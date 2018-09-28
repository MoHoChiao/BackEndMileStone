package com.netpro.trinity.resource.admin.authz.dto;

public interface IFuncPermission {
	public Boolean getView();
	public void setView(Boolean view);
	public Boolean getAdd();
	public void setAdd(Boolean add);
	public Boolean getModify();
	public void setModify(Boolean modify);
	public Boolean getDelete();
	public void setDelete(Boolean delete);
}
