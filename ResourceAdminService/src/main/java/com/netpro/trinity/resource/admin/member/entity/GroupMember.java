package com.netpro.trinity.resource.admin.member.entity;

public class GroupMember {
	private String groupuid;
	private String useruid;
	
	private String username;
	private String userid;
	
	public String getGroupuid() {
		return groupuid;
	}

	public void setGroupuid(String groupuid) {
		this.groupuid = groupuid;
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
		return "GroupMember{" + "groupuid='" + groupuid + '\'' + ", "
				+ "useruid='" + useruid + '\'' + '}';
	}
}