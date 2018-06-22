package com.netpro.trinity.service.member.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name="trinityuser")  //宣告這是一個實體trinityuser的類別
public class Trinityuser {
	@Id
  	private String useruid;
	@Column(nullable=false)
  	private String userid;
	@Column(nullable=false)
  	private String username;
	@Column
  	private String description;
	@Column(nullable=false)
  	private String usertype;
	@Column(nullable=false)
  	private String password;
	@Column(nullable=false)
  	private String activate;
	@Column
  	private String email;
	@Column
  	private String mobile;
	@Column
  	private String defaultlang;
	@Column(nullable=false)
  	private String createduseruid;
	@Column
  	private String ssoid;
	@Column
  	private String localaccount;
	@Column
  	private String onlyforexecution;
	@Column
  	private String homedir;
	@Column
  	private String pwdchangetime;
	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	@Transient
  	private String lock;
  	@Transient
  	private String resetCred;
  	
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
	public String getLock() {
		return lock;
	}
	public void setLock(String lock) {
		this.lock = lock;
	}
	public String getResetCred() {
		return resetCred;
	}
	public void setResetCred(String resetCred) {
		this.resetCred = resetCred;
	}
}
