package com.netpro.trinity.service.pluginlicense.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity(name="plugin")
public class Plugin {
	@Id
	private String pluginid;
  	@Column(nullable=false)
  	private String plugintype;
  	@Column(nullable=false)
  	private String pluginname;
  	@Column(nullable=false)
  	private String classname;
  	@Column
  	private String jarname;
  	@Column
  	private String options;
  	@Column
  	private String licensegroup;
  	
  	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
  	@JoinColumn(name="licensegroup", referencedColumnName="pluginid", insertable=false, updatable=false)
    private PluginLicense pluginlicense;
  	
  	@Transient
  	private String expireddate;
  	
  	@Transient
  	private boolean status;
  	
	public String getPluginid() {
		return pluginid;
	}
	public void setPluginid(String pluginid) {
		this.pluginid = pluginid;
	}
	public String getPlugintype() {
		return plugintype;
	}
	public void setPlugintype(String plugintype) {
		this.plugintype = plugintype;
	}
	public String getPluginname() {
		return pluginname;
	}
	public void setPluginname(String pluginname) {
		this.pluginname = pluginname;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getJarname() {
		return jarname;
	}
	public void setJarname(String jarname) {
		this.jarname = jarname;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getLicensegroup() {
		return licensegroup;
	}
	public void setLicensegroup(String licensegroup) {
		this.licensegroup = licensegroup;
	}
	public PluginLicense getPluginlicense() {
		return pluginlicense;
	}
	public void setPluginlicense(PluginLicense pluginlicense) {
		this.pluginlicense = pluginlicense;
	}
	public String getExpireddate() {
		return expireddate;
	}
	public void setExpireddate(String expireddate) {
		this.expireddate = expireddate;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
  	
}
