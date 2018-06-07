package com.netpro.trinity.repository.filesource.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="Frequencycategory")  //宣告這是一個實體Frequencycategory的類別
public class FrequencyCategory {	
	@Id
  	private String freqcategoryuid;
  	@Column(nullable=false)
  	private String freqcategoryname;
  	@Column
  	private String description;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  
  	public String getFreqcategoryuid() {
		return freqcategoryuid;
	}
	public void setFreqcategoryuid(String freqcategoryuid) {
		this.freqcategoryuid = freqcategoryuid;
	}
	public String getFreqcategoryname() {
		return freqcategoryname;
	}
	public void setFreqcategoryname(String freqcategoryname) {
		this.freqcategoryname = freqcategoryname;
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
