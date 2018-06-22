package com.netpro.trinity.service.notification.entity;

public class NotificationList {
	private String notificationuid;
	private String destinationuid;
	private String destinationtype;
	private String activate;
	private String destinationname;
	private String destinationid;
	
	public String getNotificationuid() {
		return notificationuid;
	}

	public void setNotificationuid(String notificationuid) {
		this.notificationuid = notificationuid;
	}

	public String getDestinationuid() {
		return destinationuid;
	}

	public void setDestinationuid(String destinationuid) {
		this.destinationuid = destinationuid;
	}

	public String getDestinationtype() {
		return destinationtype;
	}

	public void setDestinationtype(String destinationtype) {
		this.destinationtype = destinationtype;
	}

	public String getActivate() {
		return activate;
	}

	public void setActivate(String activate) {
		this.activate = activate;
	}
	
	public String getDestinationname() {
		return destinationname;
	}

	public void setDestinationname(String destinationname) {
		this.destinationname = destinationname;
	}
	
	public String getDestinationid() {
		return destinationid;
	}

	public void setDestinationid(String destinationid) {
		this.destinationid = destinationid;
	}
	
	@Override
	public String toString() {
		return "NotificationList{" + "notificationuid='" + notificationuid + '\'' + ", "
				+ "destinationuid='" + destinationuid + '\'' + ", "
				+ "destinationtype='" + destinationtype + '\'' + ", "
				+ "activate='" + activate + '\'' + ", "
				+ "destinationname='" + destinationname + '\'' + ", "
				+ "destinationid='" + destinationid + '\'' + '}';
	}
}