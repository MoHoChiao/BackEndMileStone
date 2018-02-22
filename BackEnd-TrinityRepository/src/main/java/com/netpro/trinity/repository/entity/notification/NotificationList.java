package com.netpro.trinity.repository.entity.notification;

public class NotificationList {
	private String notificationuid;
	private String destinationuid;
	private String destinationtype;
	private String activate;
	
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
	
	@Override
	public String toString() {
		return "NotificationList{" + "notificationuid='" + notificationuid + '\'' + ", "
				+ "destinationuid='" + destinationuid + '\'' + ", "
				+ "destinationtype='" + destinationtype + '\'' + ", "
				+ "activate='" + activate + '\'' + '}';
	}
}