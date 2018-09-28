package com.netpro.trinity.resource.admin.frequency.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="Frequency")  //宣告這是一個實體Frequency的類別
public class Frequency {	
	@Id
  	private String frequencyuid;
  	@Column(nullable=false)
  	private String frequencyname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String activate;
  	@Column(nullable=false)
  	private String lastcheckdatetime;
  	@Column(nullable=false)
  	private String manuallyedit;
  	@Column(nullable=false)
  	private String bywcalendar;
  	@Column(nullable=false)
  	private String wcalendaruid;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	@Transient
  	private List<FrequencyList> freqlist;
  	@Transient
  	private String categoryname;
  	@Transient
  	private String categoryuid;
  	
	public String getFrequencyuid() {
		return frequencyuid;
	}
	public void setFrequencyuid(String frequencyuid) {
		this.frequencyuid = frequencyuid;
	}
	public String getFrequencyname() {
		return frequencyname;
	}
	public void setFrequencyname(String frequencyname) {
		this.frequencyname = frequencyname;
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
	public String getLastcheckdatetime() {
		return lastcheckdatetime;
	}
	public void setLastcheckdatetime(String lastcheckdatetime) {
		this.lastcheckdatetime = lastcheckdatetime;
	}
	public String getManuallyedit() {
		return manuallyedit;
	}
	public void setManuallyedit(String manuallyedit) {
		this.manuallyedit = manuallyedit;
	}
	public String getBywcalendar() {
		return bywcalendar;
	}
	public void setBywcalendar(String bywcalendar) {
		this.bywcalendar = bywcalendar;
	}
	public String getWcalendaruid() {
		return wcalendaruid;
	}
	public void setWcalendaruid(String wcalendaruid) {
		this.wcalendaruid = wcalendaruid;
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
	public List<FrequencyList> getFreqlist() {
		return freqlist;
	}
	public void setFreqlist(List<FrequencyList> freqlist) {
		this.freqlist = freqlist;
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
