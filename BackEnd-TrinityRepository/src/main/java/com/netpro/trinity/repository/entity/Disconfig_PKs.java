package com.netpro.trinity.repository.entity;

import java.io.Serializable;

public class Disconfig_PKs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String module;
	private String configname;
	
	public Disconfig_PKs() {}
	
	public Disconfig_PKs(String module, String configname) {
		this.module = module;
		this.configname = configname;
	}
	
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
	
	
	@Override
    public int hashCode() {
		return module.hashCode() + configname.hashCode();
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Disconfig_PKs other = (Disconfig_PKs) obj;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		if (configname == null) {
			if (other.configname != null)
				return false;
		} else if (!configname.equals(other.configname))
			return false;
		return true;
	}
	
}