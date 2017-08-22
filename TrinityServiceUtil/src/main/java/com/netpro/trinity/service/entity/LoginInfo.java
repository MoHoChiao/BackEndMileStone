package com.netpro.trinity.service.entity;

import com.netpro.trinity.service.entity.TrinityServiceEntity;

//宣告一個POJO
public class LoginInfo extends TrinityServiceEntity{
	private String account;
	private String psw;
	private String model;
	private String remoteip;
	private String userinfo;
	private String tokenstr;
	
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
	public String getTokenstr() {
		return tokenstr;
	}
	public void setTokenstr(String tokenstr) {
		this.tokenstr = tokenstr;
	}
}
