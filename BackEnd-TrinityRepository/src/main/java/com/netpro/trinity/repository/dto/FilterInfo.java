package com.netpro.trinity.repository.dto;

public class FilterInfo {
	private Paging paging;
	private Ordering ordering;
	private Querying querying;
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
	
	
}
