package com.netpro.trinity.repository.objectalias.entity;

public class ObjectAlias {
	private String parentuid;
	private String aliasname;
	private String aliastype;
	private String objectuid;
	private String description;
	private String objectname;
	
	public String getParentuid() {
		return parentuid;
	}

	public void setParentuid(String parentuid) {
		this.parentuid = parentuid;
	}

	public String getAliasname() {
		return aliasname;
	}

	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}

	public String getAliastype() {
		return aliastype;
	}

	public void setAliastype(String aliastype) {
		this.aliastype = aliastype;
	}

	public String getObjectuid() {
		return objectuid;
	}

	public void setObjectuid(String objectuid) {
		this.objectuid = objectuid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getObjectname() {
		return objectname;
	}

	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}
	
	@Override
	public String toString() {
		return "ObjectAlias{" + "parentuid='" + parentuid + '\'' + ", "
				+ "aliasname='" + aliasname + '\'' + ", "
				+ "aliastype='" + aliastype + '\'' + ", "
				+ "objectuid='" + objectuid + '\'' + ", "
				+ "description=" + description + ", "
				+ "objectname='" + objectname + '\'' + '}';
	}
}