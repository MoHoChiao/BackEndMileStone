package com.netpro.trinity.repository.connection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="Connectioncategory")  //宣告這是一個實體ConnectionCategory的類別
public class ConnectionCategory {	
	@Id
  	private String conncategoryuid;
  	@Column(nullable=false)
  	private String conncategoryname;
  	@Column
  	private String description;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	public String getConncategoryuid() {
		return conncategoryuid;
	}
	public void setConncategoryuid(String conncategoryuid) {
		this.conncategoryuid = conncategoryuid;
	}
	public String getConncategoryname() {
		return conncategoryname;
	}
	public void setConncategoryname(String conncategoryname) {
		this.conncategoryname = conncategoryname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}
