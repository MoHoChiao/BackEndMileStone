package com.netpro.trinity.repository.configuration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name="Disconfig")
@IdClass(DisconfigPKs.class)
public class Disconfig {
	@Id
	private String module;
	@Id
	private String configname;
	@Column
	private String value;
	@Column
	private String instance;
	
	public void setModule(String module) {
		this.module = module;
	}
	public String getModule() {
		return module;
	}
	public void setConfigname(String configname) {
		this.configname = configname;
	}
	public String getConfigname() {
		return configname;
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