package com.netpro.trinity.repository.job.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="Jobstep")  //宣告這是一個實體JCSAgent的類別
public class Jobstep {	
	@Id
  	private String stepuid;
  	@Column(nullable=false)
  	private String stepname;
  	@Column
  	private String description;
  	@Column
  	private Integer stepseq;
  	@Column(nullable=false)
  	private String activate;
  	@Column(nullable=false)
  	private String jobuid;
  	@Column(nullable=false)
  	private String steptype;
  	@Column(nullable=false)
  	private String successrule;
  	@Column
  	private Integer successvalue1;
  	@Column
  	private Integer successvalue2;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
	public String getStepuid() {
		return stepuid;
	}
	public void setStepuid(String stepuid) {
		this.stepuid = stepuid;
	}
	public String getStepname() {
		return stepname;
	}
	public void setStepname(String stepname) {
		this.stepname = stepname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStepseq() {
		return stepseq;
	}
	public void setStepseq(Integer stepseq) {
		this.stepseq = stepseq;
	}
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public String getJobuid() {
		return jobuid;
	}
	public void setJobuid(String jobuid) {
		this.jobuid = jobuid;
	}
	public String getSteptype() {
		return steptype;
	}
	public void setSteptype(String steptype) {
		this.steptype = steptype;
	}
	public String getSuccessrule() {
		return successrule;
	}
	public void setSuccessrule(String successrule) {
		this.successrule = successrule;
	}
	public Integer getSuccessvalue1() {
		return successvalue1;
	}
	public void setSuccessvalue1(Integer successvalue1) {
		this.successvalue1 = successvalue1;
	}
	public Integer getSuccessvalue2() {
		return successvalue2;
	}
	public void setSuccessvalue2(Integer successvalue2) {
		this.successvalue2 = successvalue2;
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
}
