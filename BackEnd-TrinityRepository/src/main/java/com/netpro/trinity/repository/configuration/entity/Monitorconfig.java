package com.netpro.trinity.repository.configuration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="monitorconfig")  //宣告這是一個實體monitorconfig的類別
public class Monitorconfig {
	@Id
  	private String uid;
	@Column
  	private Boolean resourcemonitor;
	@Column
  	private Boolean processmonitor;
	@Column
  	private String xml;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	@Transient
  	private Boolean SuspendJob;
  	@Transient
  	private Integer cpu;
  	@Transient
  	private Integer memory;
  	@Transient
  	private List<MonitorDisk> disk;
  	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Boolean getResourcemonitor() {
		return resourcemonitor;
	}
	public void setResourcemonitor(Boolean resourcemonitor) {
		this.resourcemonitor = resourcemonitor;
	}
	public Boolean getProcessmonitor() {
		return processmonitor;
	}
	public void setProcessmonitor(Boolean processmonitor) {
		this.processmonitor = processmonitor;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	
	
	public Boolean getSuspendJob() {
		return SuspendJob;
	}
	public void setSuspendJob(Boolean suspendJob) {
		SuspendJob = suspendJob;
	}
	public Integer getCpu() {
		return cpu;
	}
	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public List<MonitorDisk> getDisk() {
		return disk;
	}
	public void setDisk(List<MonitorDisk> disk) {
		this.disk = disk;
	}
}
