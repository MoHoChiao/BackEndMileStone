package com.netpro.trinity.service.util.entity;

//宣告一個POJO
public class LoginInfo{
	private String account;
	private String psw;
	private String model;
	private String remoteip;
	
	public String getAccount() {
	  return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getRemoteip() {
		return remoteip;
	}
	public void setRemoteip(String remoteip) {
		this.remoteip = remoteip;
	}
}
