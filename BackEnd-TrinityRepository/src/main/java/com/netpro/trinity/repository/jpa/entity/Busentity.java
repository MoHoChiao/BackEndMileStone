package com.netpro.trinity.repository.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="Busentity")  //宣告這是一個實體Busentity的類別
public class Busentity {	
	@Id
  	private String busentityuid;
  	@Column(nullable=false)
  	private String busentityname;
  	@Column
  	private String description;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	public String getBusentityuid() {
		return busentityuid;
	}
	public void setBusentityuid(String busentityuid) {
		this.busentityuid = busentityuid;
	}
	public String getBusentityname() {
		return busentityname;
	}
	public void setBusentityname(String busentityname) {
		this.busentityname = busentityname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
