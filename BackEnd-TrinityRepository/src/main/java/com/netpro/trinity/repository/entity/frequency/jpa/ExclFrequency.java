package com.netpro.trinity.repository.entity.frequency.jpa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.netpro.trinity.repository.entity.frequency.jdbc.ExclFrequencyList;

@Entity(name="excludefrequency")  //宣告這是一個實體excludefrequency的類別
public class ExclFrequency {	
	@Id
	private String excludefrequencyuid;
	@Column(nullable=false)
  	private String excludefrequencyname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String activate;
  	
  	@Transient
  	private List<ExclFrequencyList> excludefrequencylist;
  	
  	public String getExcludefrequencyuid() {
		return excludefrequencyuid;
	}
	public void setExcludefrequencyuid(String excludefrequencyuid) {
		this.excludefrequencyuid = excludefrequencyuid;
	}
	public String getExcludefrequencyname() {
		return excludefrequencyname;
	}
	public void setExcludefrequencyname(String excludefrequencyname) {
		this.excludefrequencyname = excludefrequencyname;
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
	public List<ExclFrequencyList> getExcludefrequencylist() {
		return excludefrequencylist;
	}
	public void setExcludefrequencylist(List<ExclFrequencyList> excludefrequencylist) {
		this.excludefrequencylist = excludefrequencylist;
	}
}
