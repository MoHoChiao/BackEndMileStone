package com.netpro.trinity.resource.admin.connection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="Connection")  //宣告這是一個實體JCSAgent的類別
public class Connection {	
	@Id
  	private String connectionuid;
  	@Column(nullable=false)
  	private String connectionname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String connectiontype;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	@Transient
  	private String pimendpointtype;
  	@Transient
  	private String pimendpointname;
  	@Transient
  	private String pimaccountcontainer;
  	@Transient
  	private String pimaccountname;
  	@Transient
  	private String withpim;
  	
  	@Transient
  	private String categoryname;
  	@Transient
  	private String categoryuid;
  	
  	public String getConnectionuid() {
		return connectionuid;
	}
	public void setConnectionuid(String connectionuid) {
		this.connectionuid = connectionuid;
	}
	public String getConnectionname() {
		return connectionname;
	}
	public void setConnectionname(String connectionname) {
		this.connectionname = connectionname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getConnectiontype() {
		return connectiontype;
	}
	public void setConnectiontype(String connectiontype) {
		this.connectiontype = connectiontype;
	}
	public String getXmldata() {
		return xmldata;
	}
	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	
	
	public String getPimendpointtype() {
		return pimendpointtype;
	}
	public void setPimendpointtype(String pimendpointtype) {
		this.pimendpointtype = pimendpointtype;
	}
	public String getPimendpointname() {
		return pimendpointname;
	}
	public void setPimendpointname(String pimendpointname) {
		this.pimendpointname = pimendpointname;
	}
	public String getPimaccountcontainer() {
		return pimaccountcontainer;
	}
	public void setPimaccountcontainer(String pimaccountcontainer) {
		this.pimaccountcontainer = pimaccountcontainer;
	}
	public String getPimaccountname() {
		return pimaccountname;
	}
	public void setPimaccountname(String pimaccountname) {
		this.pimaccountname = pimaccountname;
	}
	public String getWithpim() {
		return withpim;
	}
	public void setWithpim(String withpim) {
		this.withpim = withpim;
	}
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public String getCategoryuid() {
		return categoryuid;
	}
	public void setCategoryuid(String categoryuid) {
		this.categoryuid = categoryuid;
	}
}
