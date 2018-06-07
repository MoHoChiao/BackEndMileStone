package com.netpro.trinity.repository.externalrule.dto;

public class PublishRule {
	private String rulename;
	private String fullclasspath;
	private String active;
	private String description;
	
	private String extjaruid;
	private String packageuid;
	private String packagename;
	private String filename;
	private String md5;
	
	//for publish only
	private Boolean published;
	
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
	
	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public String getPackageuid() {
		return packageuid;
	}

	public void setPackageuid(String packageuid) {
		this.packageuid = packageuid;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	@Override
	public String toString() {
		return "PublishRule{" + "rulename='" + rulename + '\'' + ", "
				+ "fullclasspath='" + fullclasspath + '\'' + ", "
				+ "active='" + active + '\'' + ", "
				+ "description='" + description + '\'' + ", "
				+ "extjaruid='" + extjaruid + '\'' + ", "
				+ "packageuid='" + packageuid + '\'' + ", "
				+ "packagename='" + packagename + '\'' + ", "
				+ "filename='" + filename + '\'' + ", "
				+ "md5='" + md5 + '\'' + ", "
				+ "published='" + published + '\'' + '}';
	}
}