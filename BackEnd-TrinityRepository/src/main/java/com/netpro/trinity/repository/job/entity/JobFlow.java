package com.netpro.trinity.repository.job.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="jobflow")  //宣告這是一個實體jobflow的類別
public class JobFlow {	
	@Id
  	private String jobflowuid;
  	@Column(nullable=false)
  	private String flowname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String activate;
  	@Column
  	private String domainuid;
  	@Column
  	private String categoryuid;
  	@Column
  	private String frequencyuid;
  	@Column
  	private String suppressjobdomain;
  	@Column(nullable=false)
  	private String txdaterule;
  	@Column
  	private Integer txoffsetday;
  	@Column
  	private String bypasserror;
  	@Column(nullable=false)
  	private String status;
  	@Column(nullable=false)
  	private String createuseruid;
  	@Column
  	private String xmldata;
  	@Column
  	private String onlinedatetime;
  	@Column
  	private String offlinedatetime;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	public String getJobflowuid() {
		return jobflowuid;
	}
	public void setJobflowuid(String jobflowuid) {
		this.jobflowuid = jobflowuid;
	}
	public String getFlowname() {
		return flowname;
	}
	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public String getDomainuid() {
		return domainuid;
	}
	public void setDomainuid(String domainuid) {
		this.domainuid = domainuid;
	}
	public String getCategoryuid() {
		return categoryuid;
	}
	public void setCategoryuid(String categoryuid) {
		this.categoryuid = categoryuid;
	}
	public String getFrequencyuid() {
		return frequencyuid;
	}
	public void setFrequencyuid(String frequencyuid) {
		this.frequencyuid = frequencyuid;
	}
	public String getTxdaterule() {
		return txdaterule;
	}
	public void setTxdaterule(String txdaterule) {
		this.txdaterule = txdaterule;
	}
	public Integer getTxoffsetday() {
		return txoffsetday;
	}
	public void setTxoffsetday(Integer txoffsetday) {
		this.txoffsetday = txoffsetday;
	}
	public String getBypasserror() {
		return bypasserror;
	}
	public void setBypasserror(String bypasserror) {
		this.bypasserror = bypasserror;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateuseruid() {
		return createuseruid;
	}
	public void setCreateuseruid(String createuseruid) {
		this.createuseruid = createuseruid;
	}
	public String getXmldata() {
		return xmldata;
	}
	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}
	public String getOnlinedatetime() {
		return onlinedatetime;
	}
	public void setOnlinedatetime(String onlinedatetime) {
		this.onlinedatetime = onlinedatetime;
	}
	public String getOfflinedatetime() {
		return offlinedatetime;
	}
	public void setOfflinedatetime(String offlinedatetime) {
		this.offlinedatetime = offlinedatetime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}
