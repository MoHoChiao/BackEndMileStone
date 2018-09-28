package com.netpro.trinity.resource.admin.configuration.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="trinityconfig")  //宣告這是一個實體trinityconfig的類別
public class Trinityconfig {
	@Id
	private String versionid;
	@Column(nullable=false)
	private String dualservermode;
	@Column
	private String primaryhost;
	@Column
	private Integer primaryport;
	@Column
	private String standbyhost;
	@Column
	private Integer standbyport;
	@Column
	private String smtppassword;
	@Column
	private String smtpserver;
	@Column
	private String smtpuser;
	@Column(nullable=false)
	private String authmode;
	@Column
	private String ldapserver;
	@Column
	private String defaultdomain;
	@Column
	private String metaautocompare;
	@Column
	private Integer metacomparekeepperiod;
	@Column
	private Integer metacomparetime;
	@Column
	private String metadocumenttype;
	@Column
	private String pwdenforce;
	@Column
	private String pwdlevel;
	@Column
	private Integer pwdminlen;
	@Column
	private Integer pwdcycle;
	@Column
	private Integer pwdexpireday;
	@Column
  	@Temporal(TemporalType.TIMESTAMP)
	private Date lastupdatetime;

	public String getVersionid() {
		return versionid;
	}

	public void setVersionid(String versionid) {
		this.versionid = versionid;
	}

	public String getDualservermode() {
		return dualservermode;
	}

	public void setDualservermode(String dualservermode) {
		this.dualservermode = dualservermode;
	}

	public String getPrimaryhost() {
		return primaryhost;
	}

	public void setPrimaryhost(String primaryhost) {
		this.primaryhost = primaryhost;
	}

	public Integer getPrimaryport() {
		return primaryport;
	}

	public void setPrimaryport(Integer primaryport) {
		this.primaryport = primaryport;
	}

	public String getStandbyhost() {
		return standbyhost;
	}

	public void setStandbyhost(String standbyhost) {
		this.standbyhost = standbyhost;
	}

	public Integer getStandbyport() {
		return standbyport;
	}

	public void setStandbyport(Integer standbyport) {
		this.standbyport = standbyport;
	}

	public String getSmtppassword() {
		return smtppassword;
	}

	public void setSmtppassword(String smtppassword) {
		this.smtppassword = smtppassword;
	}

	public String getSmtpserver() {
		return smtpserver;
	}

	public void setSmtpserver(String smtpserver) {
		this.smtpserver = smtpserver;
	}

	public String getSmtpuser() {
		return smtpuser;
	}

	public void setSmtpuser(String smtpuser) {
		this.smtpuser = smtpuser;
	}

	public String getAuthmode() {
		return authmode;
	}

	public void setAuthmode(String authmode) {
		this.authmode = authmode;
	}

	public String getLdapserver() {
		return ldapserver;
	}

	public void setLdapserver(String ldapserver) {
		this.ldapserver = ldapserver;
	}

	public String getDefaultdomain() {
		return defaultdomain;
	}

	public void setDefaultdomain(String defaultdomain) {
		this.defaultdomain = defaultdomain;
	}

	public String getMetaautocompare() {
		return metaautocompare;
	}

	public void setMetaautocompare(String metaautocompare) {
		this.metaautocompare = metaautocompare;
	}

	public Integer getMetacomparekeepperiod() {
		return metacomparekeepperiod;
	}

	public void setMetacomparekeepperiod(Integer metacomparekeepperiod) {
		this.metacomparekeepperiod = metacomparekeepperiod;
	}

	public Integer getMetacomparetime() {
		return metacomparetime;
	}

	public void setMetacomparetime(Integer metacomparetime) {
		this.metacomparetime = metacomparetime;
	}

	public String getMetadocumenttype() {
		return metadocumenttype;
	}

	public void setMetadocumenttype(String metadocumenttype) {
		this.metadocumenttype = metadocumenttype;
	}

	public String getPwdenforce() {
		return pwdenforce;
	}

	public void setPwdenforce(String pwdenforce) {
		this.pwdenforce = pwdenforce;
	}

	public String getPwdlevel() {
		return pwdlevel;
	}

	public void setPwdlevel(String pwdlevel) {
		this.pwdlevel = pwdlevel;
	}

	public Integer getPwdminlen() {
		return pwdminlen;
	}

	public void setPwdminlen(Integer pwdminlen) {
		this.pwdminlen = pwdminlen;
	}

	public Integer getPwdcycle() {
		return pwdcycle;
	}

	public void setPwdcycle(Integer pwdcycle) {
		this.pwdcycle = pwdcycle;
	}

	public Integer getPwdexpireday() {
		return pwdexpireday;
	}

	public void setPwdexpireday(Integer pwdexpireday) {
		this.pwdexpireday = pwdexpireday;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}
