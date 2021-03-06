package com.netpro.trinity.resource.admin.configuration.entity;

import java.io.Serializable;

public class DisconfigPKs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String module;
	private String configname;
	
	public DisconfigPKs() {}
	
	public DisconfigPKs(String module, String configname) {
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
		DisconfigPKs other = (DisconfigPKs) obj;
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