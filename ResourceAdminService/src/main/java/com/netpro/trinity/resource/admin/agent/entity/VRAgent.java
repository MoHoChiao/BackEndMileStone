package com.netpro.trinity.resource.admin.agent.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="jcsvirtualagent")  //宣告這是一個實體JCSVirtualAgent的類別
public class VRAgent {	
	@Id
	private String virtualagentuid;
	@Column(nullable=false)
  	private String virtualagentname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private Integer maximumjob;
  	@Column(nullable=false)
  	private String activate;
  	@Column
  	private String mode;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	@Transient
  	private List<VRAgentList> agentlist;
  	
  	public String getVirtualagentuid() {
		return virtualagentuid;
	}
	public void setVirtualagentuid(String virtualagentuid) {
		this.virtualagentuid = virtualagentuid;
	}
	public String getVirtualagentname() {
		return virtualagentname;
	}
	public void setVirtualagentname(String virtualagentname) {
		this.virtualagentname = virtualagentname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
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
	
	
	public List<VRAgentList> getAgentlist() {
		return agentlist;
	}
	public void setAgentlist(List<VRAgentList> agentlist) {
		this.agentlist = agentlist;
	}
}
