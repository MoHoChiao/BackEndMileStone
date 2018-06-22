package com.netpro.trinity.service.job.entity;

public class JobExclude {
	private String jobuid;
	private String excludefrequencyuid;
	
	private String jobname;
	private String categoryname;
	private String busentityname;
	
	public String getJobuid() {
		return jobuid;
	}

	public void setJobuid(String jobuid) {
		this.jobuid = jobuid;
	}

	public String getExcludefrequencyuid() {
		return excludefrequencyuid;
	}

	public void setExcludefrequencyuid(String excludefrequencyuid) {
		this.excludefrequencyuid = excludefrequencyuid;
	}
	
	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getBusentityname() {
		return busentityname;
	}

	public void setBusentityname(String busentityname) {
		this.busentityname = busentityname;
	}
	
	@Override
	public String toString() {
		return "JobExclude{" + "jobuid='" + jobuid + '\'' + ", "
				+ "excludefrequencyuid='" + excludefrequencyuid + '\'' + ", "
				+ "jobname='" + jobname + '\'' + ", "
				+ "categoryname='" + categoryname + '\'' + ", "
				+ "busentityname='" + busentityname + '\'' + '}';
	}
}