package com.netpro.trinity.service.connection.entity;

public class FTPConnection extends Connection{
	private String userid;
	private String targetdir;
	private String server;
	private String password;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTargetdir() {
		return targetdir;
	}
	public void setTargetdir(String targetdir) {
		this.targetdir = targetdir;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
