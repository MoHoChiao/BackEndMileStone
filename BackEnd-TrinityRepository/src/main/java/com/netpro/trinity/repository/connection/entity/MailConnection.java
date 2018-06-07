package com.netpro.trinity.repository.connection.entity;

public class MailConnection extends Connection{
	private String host;
	private Integer port;
	private String mailssl;
	private String mailtls;
	private String user;
	private String password;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getMailssl() {
		return mailssl;
	}
	public void setMailssl(String mailssl) {
		this.mailssl = mailssl;
	}
	public String getMailtls() {
		return mailtls;
	}
	public void setMailtls(String mailtls) {
		this.mailtls = mailtls;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
