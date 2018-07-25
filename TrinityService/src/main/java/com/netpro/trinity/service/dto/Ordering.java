package com.netpro.trinity.service.dto;

public class Ordering {
	private String orderType;
	private String orderField;
	
	public Ordering() {}
	
	public Ordering(String orderType, String orderField) {
		this.orderType = orderType;
		this.orderField = orderField;
	}
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderField() {
		return orderField;
	}
	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}
}
