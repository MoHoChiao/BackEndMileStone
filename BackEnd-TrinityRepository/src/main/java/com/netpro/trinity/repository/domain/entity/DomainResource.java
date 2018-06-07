package com.netpro.trinity.repository.domain.entity;

public class DomainResource {
	private String domainuid;
	private String resourcename;
	private String resourcevalue;

	public String getDomainuid() {
		return domainuid;
	}

	public void setDomainuid(String domainuid) {
		this.domainuid = domainuid;
	}

	public String getResourcename() {
		return resourcename;
	}

	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}

	public String getResourcevalue() {
		return resourcevalue;
	}

	public void setResourcevalue(String resourcevalue) {
		this.resourcevalue = resourcevalue;
	}
	
	@Override
	public String toString() {
		return "DomainResource{" + "domainuid='" + domainuid + '\'' + ", "
				+ "resourcename='" + resourcename + '\'' + ", "
				+ "resourcevalue='" + resourcevalue + '\'' + '}';
	}
}