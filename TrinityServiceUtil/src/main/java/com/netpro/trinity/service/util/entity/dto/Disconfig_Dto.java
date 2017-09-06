package com.netpro.trinity.service.util.entity.dto;

public class Disconfig_Dto {
	private String module;
	private String configname;
	private String value;
	private String instance;
	
	public Disconfig_Dto() {}
	
	public Disconfig_Dto(String module, String configname) {
		this.module = module;
		this.configname = configname;
	}
	
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getConfigname() {
		return configname;
	}
	public void setConfigname(String configname) {
		this.configname = configname;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
}