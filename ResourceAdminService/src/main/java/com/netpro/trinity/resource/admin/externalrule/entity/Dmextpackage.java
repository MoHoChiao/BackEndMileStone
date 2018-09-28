package com.netpro.trinity.resource.admin.externalrule.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="dmextpackage")  //宣告這是一個實體dmextpackage的類別
public class Dmextpackage {	
	@Id
  	private String packageuid;
  	@Column(nullable=false)
  	private String packagename;
  	@Column
  	private String description;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	@Transient
  	private List<DmExtJar> files;
  	
  	public String getPackageuid() {
		return packageuid;
	}
	public void setPackageuid(String packageuid) {
		this.packageuid = packageuid;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
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
	public List<DmExtJar> getFiles() {
		return files;
	}
	public void setFiles(List<DmExtJar> files) {
		this.files = files;
	}
}
