package com.netpro.trinity.client.service.entity;

//宣告一個POJO
public class LoginInfo {
	private String account;
	private String psw;
	private String model;
	private String remoteip;
	private String flag;
	private String info;
	private String userinfo;
	
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(String userinfo) {
		this.userinfo = userinfo;
	}
	public String getRemoteip() {
		return remoteip;
	}
	public void setRemoteip(String remoteip) {
		this.remoteip = remoteip;
	}
}
