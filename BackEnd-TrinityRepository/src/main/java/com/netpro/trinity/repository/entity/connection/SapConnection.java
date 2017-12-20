package com.netpro.trinity.repository.entity.connection;

import com.netpro.trinity.repository.entity.connection.jpa.Connection;

public class SapConnection extends Connection{
	private String SAPLANGUAGE;
	private String sapSystemNumber;
	private String sapSystemName;
	private String userid;
	private String sapHostIP;
	private String sapCodePage;
	private String sapClient;
	private String password;
	
	public String getSAPLANGUAGE() {
		return SAPLANGUAGE;
	}
	public void setSAPLANGUAGE(String sAPLANGUAGE) {
		SAPLANGUAGE = sAPLANGUAGE;
	}
	public String getSapSystemNumber() {
		return sapSystemNumber;
	}
	public void setSapSystemNumber(String sapSystemNumber) {
		this.sapSystemNumber = sapSystemNumber;
	}
	public String getSapSystemName() {
		return sapSystemName;
	}
	public void setSapSystemName(String sapSystemName) {
		this.sapSystemName = sapSystemName;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSapHostIP() {
		return sapHostIP;
	}
	public void setSapHostIP(String sapHostIP) {
		this.sapHostIP = sapHostIP;
	}
	public String getSapCodePage() {
		return sapCodePage;
	}
	public void setSapCodePage(String sapCodePage) {
		this.sapCodePage = sapCodePage;
	}
	public String getSapClient() {
		return sapClient;
	}
	public void setSapClient(String sapClient) {
		this.sapClient = sapClient;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
