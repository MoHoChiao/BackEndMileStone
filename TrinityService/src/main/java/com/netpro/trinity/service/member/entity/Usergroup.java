package com.netpro.trinity.service.member.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="usergroup")  //宣告這是一個實體usergroup的類別
public class Usergroup {
	@Id
  	private String groupuid;
	@Column(nullable=false)
  	private String groupname;
	@Column
  	private String description;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
	public String getGroupuid() {
		return groupuid;
	}
	public void setGroupuid(String groupuid) {
		this.groupuid = groupuid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}
