package com.netpro.trinity.service.dto;

public class Paging {
	private Integer number;
	private Integer size;
	
	public Paging() {}
	
	public Paging(Integer number, Integer size) {
		this.number = number;
		this.size = size;
	}
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	
}
