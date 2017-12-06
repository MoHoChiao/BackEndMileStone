package com.netpro.trinity.repository.connection.entity;

import com.netpro.trinity.repository.connection.entity.jpa.Connection;

public class MailConnection extends Connection{
	private String userid;
	private String server;
	private String password;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
