package com.netpro.trinity.repository.entity.externalrule.jdbc;

public class DmExtJar {
	private String extjaruid;
	private String packageuid;
	private String filename;
	private String md5;
	private String uploadtime;
	private String filetype;
	private String description;
	
	public String getExtjaruid() {
		return extjaruid;
	}

	public void setExtjaruid(String extjaruid) {
		this.extjaruid = extjaruid;
	}
	
	public String getPackageuid() {
		return packageuid;
	}

	public void setPackageuid(String packageuid) {
		this.packageuid = packageuid;
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

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
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
				+ "packageuid='" + packageuid + '\'' + ", "
				+ "md5='" + md5 + '\'' + ", "
				+ "uploadtime='" + uploadtime + '\'' + ", "
				+ "filetype='" + filetype + '\'' + ", "
				+ "description='" + description + '\'' + '}';
	}
}