package com.netpro.trinity.repository.entity.externalrule.jdbc;

public class DmExtRule {
	private String extjaruid;
	private String rulename;
	private String fullclasspath;
	private String active;
	private String description;
	
	public String getExtjaruid() {
		return extjaruid;
	}

	public void setExtjaruid(String extjaruid) {
		this.extjaruid = extjaruid;
	}

	public String getRulename() {
		return rulename;
	}

	public void setRulename(String rulename) {
		this.rulename = rulename;
	}

	public String getFullclasspath() {
		return fullclasspath;
	}

	public void setFullclasspath(String fullclasspath) {
		this.fullclasspath = fullclasspath;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "DomainVariable{" + "extjaruid='" + extjaruid + '\'' + ", "
				+ "rulename='" + rulename + '\'' + ", "
				+ "fullclasspath='" + fullclasspath + '\'' + ", "
				+ "active='" + active + '\'' + ", "
				+ "description='" + description + '\'' + '}';
	}
}