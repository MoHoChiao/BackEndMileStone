package com.netpro.trinity.service.member.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="role")  //宣告這是一個實體role的類別
public class Role {
	@Id
  	private String roleuid;
	@Column(nullable=false)
  	private String rolename;
	@Column
  	private String description;
	@Column
  	private String homedir;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
	public String getRoleuid() {
		return roleuid;
	}
	public void setRoleuid(String roleuid) {
		this.roleuid = roleuid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHomedir() {
		return homedir;
	}
	public void setHomedir(String homedir) {
		this.homedir = homedir;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}
