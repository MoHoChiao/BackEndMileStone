package com.netpro.trinity.repository.job.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="Job")  //宣告這是一個實體Job的類別
public class Job {	
	@Id
  	private String jobuid;
  	@Column(nullable=false)
  	private String jobname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String activate;
  	@Column
  	private String domainuid;
  	@Column
  	private String categoryuid;
  	@Column
  	private String agentuid;
  	@Column
  	private String frequencyuid;
  	@Column
  	private String filesourceuid;
  	@Column(nullable=false)
  	private String jobtype;
  	@Column(nullable=false)
  	private String retry;
  	@Column
  	private Integer retryinterval;
  	@Column
  	private Integer maxretrytime;
  	@Column(nullable=false)
  	private String retrymode;
  	@Column
  	private Integer priority;
  	@Column
  	private Integer timewindowbegin;
  	@Column
  	private Integer timewindowend;
  	@Column(nullable=false)
  	private String txdaterule;
  	@Column
  	private Integer txoffsetday;
  	@Column
  	private String bypasserror;
  	@Column(nullable=false)
  	private String status;
  	@Column(nullable=false)
  	private String criticaljob;
  	@Column(nullable=false)
  	private String createuseruid;
  	@Column
  	private String xmldata;
  	@Column
  	private String onlinedatetime;
  	@Column
  	private String offlinedatetime;
  	@Column
  	private String refjobuid;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	@Transient
  	private Boolean rerunfromfs;
  	@Transient
  	private Boolean applycompletedtask;
  	@Transient
  	private String waitingtime;
  	@Transient
  	private Boolean dontsavehistory;
  	@Transient
  	private Boolean usestepcondi;
  	@Transient
  	private Boolean skipmissingtask;
  	
  	public String getJobuid() {
		return jobuid;
	}
	public void setJobuid(String jobuid) {
		this.jobuid = jobuid;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
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
	public String getAgentuid() {
		return agentuid;
	}
	public void setAgentuid(String agentuid) {
		this.agentuid = agentuid;
	}
	public String getFrequencyuid() {
		return frequencyuid;
	}
	public void setFrequencyuid(String frequencyuid) {
		this.frequencyuid = frequencyuid;
	}
	public String getFilesourceuid() {
		return filesourceuid;
	}
	public void setFilesourceuid(String filesourceuid) {
		this.filesourceuid = filesourceuid;
	}
	public String getJobtype() {
		return jobtype;
	}
	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}
	public String getRetry() {
		return retry;
	}
	public void setRetry(String retry) {
		this.retry = retry;
	}
	public Integer getRetryinterval() {
		return retryinterval;
	}
	public void setRetryinterval(Integer retryinterval) {
		this.retryinterval = retryinterval;
	}
	public Integer getMaxretrytime() {
		return maxretrytime;
	}
	public void setMaxretrytime(Integer maxretrytime) {
		this.maxretrytime = maxretrytime;
	}
	public String getRetrymode() {
		return retrymode;
	}
	public void setRetrymode(String retrymode) {
		this.retrymode = retrymode;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getTimewindowbegin() {
		return timewindowbegin;
	}
	public void setTimewindowbegin(Integer timewindowbegin) {
		this.timewindowbegin = timewindowbegin;
	}
	public Integer getTimewindowend() {
		return timewindowend;
	}
	public void setTimewindowend(Integer timewindowend) {
		this.timewindowend = timewindowend;
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
	public String getCriticaljob() {
		return criticaljob;
	}
	public void setCriticaljob(String criticaljob) {
		this.criticaljob = criticaljob;
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
	public String getRefjobuid() {
		return refjobuid;
	}
	public void setRefjobuid(String refjobuid) {
		this.refjobuid = refjobuid;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	
	
	//@Transient setter/getter
	public Boolean getRerunfromfs() {
		return rerunfromfs;
	}
	public void setRerunfromfs(Boolean rerunfromfs) {
		this.rerunfromfs = rerunfromfs;
	}
	public Boolean getApplycompletedtask() {
		return applycompletedtask;
	}
	public void setApplycompletedtask(Boolean applycompletedtask) {
		this.applycompletedtask = applycompletedtask;
	}
	public String getWaitingtime() {
		return waitingtime;
	}
	public void setWaitingtime(String waitingtime) {
		this.waitingtime = waitingtime;
	}
	public Boolean getDontsavehistory() {
		return dontsavehistory;
	}
	public void setDontsavehistory(Boolean dontsavehistory) {
		this.dontsavehistory = dontsavehistory;
	}
	public Boolean getUsestepcondi() {
		return usestepcondi;
	}
	public void setUsestepcondi(Boolean usestepcondi) {
		this.usestepcondi = usestepcondi;
	}
	public Boolean getSkipmissingtask() {
		return skipmissingtask;
	}
	public void setSkipmissingtask(Boolean skipmissingtask) {
		this.skipmissingtask = skipmissingtask;
	}
}
