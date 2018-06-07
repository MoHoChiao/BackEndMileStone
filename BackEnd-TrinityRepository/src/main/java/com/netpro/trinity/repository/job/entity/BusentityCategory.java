package com.netpro.trinity.repository.job.entity;

public class BusentityCategory {
	private String busentityuid;
	private String categoryuid;

	public String getBusentityuid() {
		return busentityuid;
	}
	public void setBusentityuid(String busentityuid) {
		this.busentityuid = busentityuid;
	}
	public String getCategoryuid() {
		return categoryuid;
	}
	public void setCategoryuid(String categoryuid) {
		this.categoryuid = categoryuid;
	}
	
	@Override
	public String toString() {
		return "BusentityCategory{" + "busentityuid='" + busentityuid + '\'' + ", "
				+ "categoryuid='" + categoryuid + '\'' + '}';
	}
}