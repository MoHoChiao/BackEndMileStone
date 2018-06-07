package com.netpro.trinity.repository.notification.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="Notification")  //宣告這是一個實體notification的類別
public class Notification {	
	@Id
  	private String notificationuid;
  	@Column(nullable=false)
  	private String notificationname;
  	@Column
  	private String description;
  	@Column(nullable=false)
  	private String targetuid;
  	@Column(nullable=false)
  	private String notificationtype;
  	@Column(nullable=false)
  	private String notificationtiming;
  	@Column(nullable=false)
  	private String targettype;
  	@Column(nullable=false)
  	private String attachlog;
  	@Column
  	private String xmldata;
  	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
  	@Transient
  	private List<NotificationList> userlist;
  	@Transient
  	private List<NotificationList> grouplist;
  	@Transient
  	private String content;
  	@Transient
  	private String subject;
  	
	public String getNotificationuid() {
		return notificationuid;
	}
	public void setNotificationuid(String notificationuid) {
		this.notificationuid = notificationuid;
	}
	public String getNotificationname() {
		return notificationname;
	}
	public void setNotificationname(String notificationname) {
		this.notificationname = notificationname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTargetuid() {
		return targetuid;
	}
	public void setTargetuid(String targetuid) {
		this.targetuid = targetuid;
	}
	public String getNotificationtype() {
		return notificationtype;
	}
	public void setNotificationtype(String notificationtype) {
		this.notificationtype = notificationtype;
	}
	public String getNotificationtiming() {
		return notificationtiming;
	}
	public void setNotificationtiming(String notificationtiming) {
		this.notificationtiming = notificationtiming;
	}
	public String getTargettype() {
		return targettype;
	}
	public void setTargettype(String targettype) {
		this.targettype = targettype;
	}
	public String getAttachlog() {
		return attachlog;
	}
	public void setAttachlog(String attachlog) {
		this.attachlog = attachlog;
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
	public List<NotificationList> getUserlist() {
		return userlist;
	}
	public void setUserlist(List<NotificationList> userlist) {
		this.userlist = userlist;
	}
	public List<NotificationList> getGrouplist() {
		return grouplist;
	}
	public void setGrouplist(List<NotificationList> grouplist) {
		this.grouplist = grouplist;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
