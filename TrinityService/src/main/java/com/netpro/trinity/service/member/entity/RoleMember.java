package com.netpro.trinity.service.member.entity;

public class RoleMember {
	private String roleuid;
	private String useruid;
	
	private String username;
	private String userid;
	
	public String getRoleuid() {
		return roleuid;
	}

	public void setRoleuid(String roleuid) {
		this.roleuid = roleuid;
	}

	public String getUseruid() {
		return useruid;
	}

	public void setUseruid(String useruid) {
		this.useruid = useruid;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Override
	public String toString() {
		return "RoleMember{" + "roleuid='" + roleuid + '\'' + ", "
				+ "useruid='" + useruid + '\'' + '}';
	}
}