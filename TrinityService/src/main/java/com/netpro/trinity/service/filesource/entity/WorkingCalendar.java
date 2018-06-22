package com.netpro.trinity.service.filesource.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.netpro.trinity.service.frequency.entity.WorkingCalendarList;

@Entity(name="workingcalendar")  //宣告這是一個實體workingcalendar的類別
public class WorkingCalendar {	
	@Id
	private String wcalendaruid;
	@Column(nullable=false)
  	private String wcalendarname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String activate;
  	
  	@Transient
  	private List<WorkingCalendarList> wcalendarlist;
  	
  	public String getWcalendaruid() {
		return wcalendaruid;
	}
	public void setWcalendaruid(String wcalendaruid) {
		this.wcalendaruid = wcalendaruid;
	}
	public String getWcalendarname() {
		return wcalendarname;
	}
	public void setWcalendarname(String wcalendarname) {
		this.wcalendarname = wcalendarname;
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
	public List<WorkingCalendarList> getWcalendarlist() {
		return wcalendarlist;
	}
	public void setWcalendarlist(List<WorkingCalendarList> wcalendarlist) {
		this.wcalendarlist = wcalendarlist;
	}
}
