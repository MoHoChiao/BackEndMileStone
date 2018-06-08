package com.netpro.trinity.repository.filter.dto;

public class PermissionTable {
	private String virtualagentuid;
	private String agentuid;
	private String activate;
	private String description;
	private boolean isRoot;
	private boolean isAdmin;
	private boolean isAccountManager;
	private boolean isAdminEditMode;

	public String getVirtualagentuid() {
		return virtualagentuid;
	}

	public void setVirtualagentuid(String virtualagentuid) {
		this.virtualagentuid = virtualagentuid;
	}

	public String getAgentuid() {
		return agentuid;
	}

	public void setAgentuid(String agentuid) {
		this.agentuid = agentuid;
	}

	public String getActivate() {
		return activate;
	}

	public void setActivate(String activate) {
		this.activate = activate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAccountManager() {
		return isAccountManager;
	}

	public void setAccountManager(boolean isAccountManager) {
		this.isAccountManager = isAccountManager;
	}

	public boolean isAdminEditMode() {
		return isAdminEditMode;
	}

	public void setAdminEditMode(boolean isAdminEditMode) {
		this.isAdminEditMode = isAdminEditMode;
	}
	
	@Override
	public String toString() {
		return "VRAgentList{" + "virtualagentuid='" + virtualagentuid + '\'' + ", "
				+ "agentuid='" + agentuid + '\'' + ", "
				+ "activate='" + activate + '\'' + ", "
				+ "description='" + description + '\'' + ", "
				+ "isRoot=" + isRoot + ", "
				+ "isAdmin=" + isAdmin + ", "
				+ "isAccountManager=" + isAccountManager + ", "
				+ "isAdminEditMode=" + isAdminEditMode + ", "
				+ "isAdminEditMode='" + isAdminEditMode + '\'' + '}';
	}
}