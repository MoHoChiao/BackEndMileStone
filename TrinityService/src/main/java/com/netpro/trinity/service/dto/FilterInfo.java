package com.netpro.trinity.service.dto;

public class FilterInfo {
	private Paging paging;
	private Ordering ordering;
	private Querying querying;
	private String param;
	
	public Paging getPaging() {
		return paging;
	}
	public void setPaging(Paging paging) {
		this.paging = paging;
	}
	public Ordering getOrdering() {
		return ordering;
	}
	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}
	public Querying getQuerying() {
		return querying;
	}
	public void setQuerying(Querying querying) {
		this.querying = querying;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
	
}
