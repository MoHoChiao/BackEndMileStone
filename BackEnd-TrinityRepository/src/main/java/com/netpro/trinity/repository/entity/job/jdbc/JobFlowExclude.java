package com.netpro.trinity.repository.entity.job.jdbc;

public class JobFlowExclude {
	private String jobflowuid;
	private String excludefrequencyuid;
	
	private String flowname;
	private String categoryname;
	private String busentityname;
	
	public String getJobflowuid() {
		return jobflowuid;
	}

	public void setJobflowuid(String jobflowuid) {
		this.jobflowuid = jobflowuid;
	}

	public String getExcludefrequencyuid() {
		return excludefrequencyuid;
	}

	public void setExcludefrequencyuid(String excludefrequencyuid) {
		this.excludefrequencyuid = excludefrequencyuid;
	}
	
	public String getFlowname() {
		return flowname;
	}

	public void setFlowname(String flowname) {
		this.flowname = flowname;
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
		return "JobExclude{" + "jobuid='" + jobflowuid + '\'' + ", "
				+ "excludefrequencyuid='" + excludefrequencyuid + '\'' + ", "
				+ "jobname='" + flowname + '\'' + ", "
				+ "categoryname='" + categoryname + '\'' + ", "
				+ "busentityname='" + busentityname + '\'' + '}';
	}
}