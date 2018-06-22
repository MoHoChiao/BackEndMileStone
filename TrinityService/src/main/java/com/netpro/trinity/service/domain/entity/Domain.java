package com.netpro.trinity.service.domain.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="domain")  //宣告這是一個實體domain的類別
public class Domain {	
	@Id
  	private String domainuid;
  	@Column(nullable=false)
  	private String name;
  	@Column
  	private String description;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	@Transient
  	private List<DomainVariable> domainVars;
  	@Transient
  	private List<DomainResource> domainResources;
  	
  	public String getDomainuid() {
		return domainuid;
	}
	public void setDomainuid(String domainuid) {
		this.domainuid = domainuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public List<DomainVariable> getDomainVars() {
		return domainVars;
	}
	public void setDomainVars(List<DomainVariable> domainVars) {
		this.domainVars = domainVars;
	}
	public List<DomainResource> getDomainResources() {
		return domainResources;
	}
	public void setDomainResources(List<DomainResource> domainResources) {
		this.domainResources = domainResources;
	}
}
