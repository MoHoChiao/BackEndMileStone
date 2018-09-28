package com.netpro.trinity.resource.admin.pluginlicense.entity;

public class PluginLicenseStatus {

	private String pluginid;
	private String pluginname;
	private String licensekey;
	
	public PluginLicenseStatus(String pluginid, String pluginname, String licensekey) {
		super();
		this.pluginid = pluginid;
		this.pluginname = pluginname;
		this.licensekey = licensekey;
	}
	
	public String getPluginid() {
		return pluginid;
	}
	public void setPluginid(String pluginid) {
		this.pluginid = pluginid;
	}
	public String getPluginname() {
		return pluginname;
	}
	public void setPluginname(String pluginname) {
		this.pluginname = pluginname;
	}
	public String getLicensekey() {
		return licensekey;
	}
	public void setLicensekey(String licensekey) {
		this.licensekey = licensekey;
	}
	
}
