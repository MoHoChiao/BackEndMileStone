package com.netpro.trinity.auth.authz.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Trinityuser {
  	private String useruid;
  	private String userid;
  	private String username;
  	private String description;
  	private String usertype;
  	private String password;
  	private String activate;
  	private String email;
  	private String mobile;
  	private String defaultlang;
  	private String createduseruid;
  	private String ssoid;
  	private String localaccount;
  	private String onlyforexecution;
  	private String homedir;
  	private String pwdchangetime;
  	private String xmldata;
  	private Date lastupdatetime;
  	
	public String getUseruid() {
		return useruid;
	}
	public void setUseruid(String useruid) {
		this.useruid = useruid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDefaultlang() {
		return defaultlang;
	}
	public void setDefaultlang(String defaultlang) {
		this.defaultlang = defaultlang;
	}
	public String getCreateduseruid() {
		return createduseruid;
	}
	public void setCreateduseruid(String createduseruid) {
		this.createduseruid = createduseruid;
	}
	public String getSsoid() {
		return ssoid;
	}
	public void setSsoid(String ssoid) {
		this.ssoid = ssoid;
	}
	public String getLocalaccount() {
		return localaccount;
	}
	public void setLocalaccount(String localaccount) {
		this.localaccount = localaccount;
	}
	public String getOnlyforexecution() {
		return onlyforexecution;
	}
	public void setOnlyforexecution(String onlyforexecution) {
		this.onlyforexecution = onlyforexecution;
	}
	public String getHomedir() {
		return homedir;
	}
	public void setHomedir(String homedir) {
		this.homedir = homedir;
	}
	public String getPwdchangetime() {
		return pwdchangetime;
	}
	public void setPwdchangetime(String pwdchangetime) {
		this.pwdchangetime = pwdchangetime;
	}
	public String getXmldata() {
		return xmldata;
	}
	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}
