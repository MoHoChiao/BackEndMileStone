package com.netpro.trinity.repository.connection.entity;

public class NotesConnection extends Connection{
	private String notesHostIP;
	private String notesIor;
	private String userid;
	private String notesServerName;
	private String password;
	private String notesDBName;
	
	public String getNotesHostIP() {
		return notesHostIP;
	}
	public void setNotesHostIP(String notesHostIP) {
		this.notesHostIP = notesHostIP;
	}
	public String getNotesIor() {
		return notesIor;
	}
	public void setNotesIor(String notesIor) {
		this.notesIor = notesIor;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getNotesServerName() {
		return notesServerName;
	}
	public void setNotesServerName(String notesServerName) {
		this.notesServerName = notesServerName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNotesDBName() {
		return notesDBName;
	}
	public void setNotesDBName(String notesDBName) {
		this.notesDBName = notesDBName;
	}
	
}
