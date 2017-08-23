package com.netpro.trinity.service.entity;

public class TrinityServiceError {
	private Integer code;
	private String msg;
	
	public TrinityServiceError() {}
	
	public TrinityServiceError(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
}
