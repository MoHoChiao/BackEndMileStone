package com.netpro.trinity.repository.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="JCSAgent")  //宣告這是一個實體JCSAgent的類別
public class JCSAgent {	
	@Id
  	private String agentuid;
  	@Column
  	private String agentname;
  	@Column
  	private String description;
  	@Column
  	private String host;
  	@Column
  	private Integer port;
  	@Column
  	private Integer maximumjob;
  	@Column
  	private String activate;
  	@Column
  	private String ostype;
  	@Column
  	private String osname;
  	@Column
  	private String agentstatus;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lostcontact;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	@Transient
  	private String deadperiod;
  	@Transient
  	private String monitortime;
  	@Transient
  	private String cpuweight;
  	@Transient
  	private String memweight;
  	@Transient
  	private String encoding;
  	@Transient
  	private String compresstransfer;
  	
  
  	public String getAgentuid() {
		return agentuid;
	}
	public void setAgentuid(String agentuid) {
		this.agentuid = agentuid;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getMaximumjob() {
		return maximumjob;
	}
	public void setMaximumjob(Integer maximumjob) {
		this.maximumjob = maximumjob;
	}
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public String getOstype() {
		return ostype;
	}
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}
	public String getOsname() {
		return osname;
	}
	public void setOsname(String osname) {
		this.osname = osname;
	}
	public String getAgentstatus() {
		return agentstatus;
	}
	public void setAgentstatus(String agentstatus) {
		this.agentstatus = agentstatus;
	}
	public String getXmldata() {
		return xmldata;
	}
	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}
	public Date getLostcontact() {
		return lostcontact;
	}
	public void setLostcontact(Date lostcontact) {
		this.lostcontact = lostcontact;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	
	public String getDeadperiod() {
		return deadperiod;
	}
	public void setDeadperiod(String deadperiod) {
		this.deadperiod = deadperiod;
	}
	public String getMonitortime() {
		return monitortime;
	}
	public void setMonitortime(String monitortime) {
		this.monitortime = monitortime;
	}
	public String getCpuweight() {
		return cpuweight;
	}
	public void setCpuweight(String cpuweight) {
		this.cpuweight = cpuweight;
	}
	public String getMemweight() {
		return memweight;
	}
	public void setMemweight(String memweight) {
		this.memweight = memweight;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getCompresstransfer() {
		return compresstransfer;
	}
	public void setCompresstransfer(String compresstransfer) {
		this.compresstransfer = compresstransfer;
	}
}
