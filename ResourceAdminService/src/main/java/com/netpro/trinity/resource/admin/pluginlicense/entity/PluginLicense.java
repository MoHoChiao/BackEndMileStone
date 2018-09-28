package com.netpro.trinity.resource.admin.pluginlicense.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="pluginlicense")
public class PluginLicense {
	@Id
	private String pluginid;
  	@Column(nullable=false)
  	private String licensekey;
  	
//  	@OneToMany(fetch = FetchType.LAZY)
//  	@JoinColumn(name="licensegroup", referencedColumnName="pluginid")
//  	private List<Plugin> plugins;
  	
	public String getPluginid() {
		return pluginid;
	}
	public void setPluginid(String pluginid) {
		this.pluginid = pluginid;
	}
	public String getLicensekey() {
		return licensekey;
	}
	public void setLicensekey(String licensekey) {
		this.licensekey = licensekey;
	}
//	public List<Plugin> getPlugins() {
//		return plugins;
//	}
//	public void setPlugins(List<Plugin> plugins) {
//		this.plugins = plugins;
//	}
	
}
