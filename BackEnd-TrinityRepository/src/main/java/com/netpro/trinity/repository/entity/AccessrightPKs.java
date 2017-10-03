package com.netpro.trinity.repository.entity;

import java.io.Serializable;

public class AccessrightPKs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String peopleuid;
	private String objectuid;
	
	public AccessrightPKs() {}
	
	public AccessrightPKs(String peopleuid, String objectuid) {
		this.setPeopleuid(peopleuid);
		this.setObjectuid(objectuid);
	}
	
	public String getPeopleuid() {
		return peopleuid;
	}

	public void setPeopleuid(String peopleuid) {
		this.peopleuid = peopleuid;
	}

	public String getObjectuid() {
		return objectuid;
	}

	public void setObjectuid(String objectuid) {
		this.objectuid = objectuid;
	}
	
	
	@Override
    public int hashCode() {
		return peopleuid.hashCode() + objectuid.hashCode();
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessrightPKs other = (AccessrightPKs) obj;
		if (peopleuid == null) {
			if (other.peopleuid != null)
				return false;
		} else if (!peopleuid.equals(other.peopleuid))
			return false;
		if (objectuid == null) {
			if (other.objectuid != null)
				return false;
		} else if (!objectuid.equals(other.objectuid))
			return false;
		return true;
	}

	
}