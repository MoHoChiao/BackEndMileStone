package com.netpro.trinity.repository.entity.objectalias.jpa;

import java.io.Serializable;

public class ObjectAliasPKs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parentuid;
	private String aliasname;
	
	public ObjectAliasPKs() {}
	
	public ObjectAliasPKs(String parentuid, String aliasname) {
		this.parentuid = parentuid;
		this.aliasname = aliasname;
	}
	
	public void setParentuid(String parentuid) {
		this.parentuid = parentuid;
	}
	public String getParentuid() {
		return parentuid;
	}
	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}
	public String getAliasname() {
		return aliasname;
	}
	
	
	@Override
    public int hashCode() {
		return parentuid.hashCode() + aliasname.hashCode();
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectAliasPKs other = (ObjectAliasPKs) obj;
		if (parentuid == null) {
			if (other.parentuid != null)
				return false;
		} else if (!parentuid.equals(other.parentuid))
			return false;
		if (aliasname == null) {
			if (other.aliasname != null)
				return false;
		} else if (!aliasname.equals(other.aliasname))
			return false;
		return true;
	}
	
}