package com.netpro.trinity.repository.filesource.entity.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="Filesourcecategory")  //宣告這是一個實體FileSourceCategory的類別
public class FileSourceCategory {	
	@Id
  	private String fscategoryuid;
  	@Column(nullable=false)
  	private String fscategoryname;
  	@Column
  	private String description;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	public String getFscategoryuid() {
		return fscategoryuid;
	}
	public void setFscategoryuid(String fscategoryuid) {
		this.fscategoryuid = fscategoryuid;
	}
	public String getFscategoryname() {
		return fscategoryname;
	}
	public void setFscategoryname(String fscategoryname) {
		this.fscategoryname = fscategoryname;
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
