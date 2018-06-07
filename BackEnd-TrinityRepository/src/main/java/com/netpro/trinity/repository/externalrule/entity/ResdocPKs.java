package com.netpro.trinity.repository.externalrule.entity;

import java.io.Serializable;

public class ResdocPKs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String module;
	private String resname;
	private String langcode;
	
	public ResdocPKs() {}
	
	public ResdocPKs(String module, String resname) {
		this.module = module;
		this.resname = resname;
	}
	
	public void setModule(String module) {
		this.module = module;
	}
	public String getModule() {
		return module;
	}
	public void setResname(String resname) {
		this.resname = resname;
	}
	public String getResname() {
		return resname;
	}
	public String getLangcode() {
		return langcode;
	}
	public void setLangcode(String langcode) {
		this.langcode = langcode;
	}
	
	@Override
    public int hashCode() {
		return module.hashCode() + resname.hashCode() + langcode.hashCode();
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResdocPKs other = (ResdocPKs) obj;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		if (resname == null) {
			if (other.resname != null)
				return false;
		} else if (!resname.equals(other.resname))
			return false;
		if (langcode == null) {
			if (other.langcode != null)
				return false;
		} else if (!langcode.equals(other.langcode))
			return false;
		return true;
	}
}