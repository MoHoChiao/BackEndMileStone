package com.netpro.trinity.service.connection.entity;

public class JDBCConnection extends Connection{
	private String jdbc_url;
	private String jdbc_dbType;
	private String jdbc_driver;
	private String jdbc_userid;
	private String jdbc_password;
	
	public String getJdbc_url() {
		return jdbc_url;
	}
	public void setJdbc_url(String jdbc_url) {
		this.jdbc_url = jdbc_url;
	}
	public String getJdbc_dbType() {
		return jdbc_dbType;
	}
	public void setJdbc_dbType(String jdbc_dbType) {
		this.jdbc_dbType = jdbc_dbType;
	}
	public String getJdbc_driver() {
		return jdbc_driver;
	}
	public void setJdbc_driver(String jdbc_driver) {
		this.jdbc_driver = jdbc_driver;
	}
	public String getJdbc_userid() {
		return jdbc_userid;
	}
	public void setJdbc_userid(String jdbc_userid) {
		this.jdbc_userid = jdbc_userid;
	}
	public String getJdbc_password() {
		return jdbc_password;
	}
	public void setJdbc_password(String jdbc_password) {
		this.jdbc_password = jdbc_password;
	}
}
