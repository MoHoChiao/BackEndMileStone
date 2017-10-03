package com.netpro.trinity.repository.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="Accessright")
@IdClass(AccessrightPKs.class)
public class Accessright {
	@Id
	private String peopleuid;
	@Id
	private String objectuid;
	@Column
	private String flag1;
	@Column
	private String flag2;
	@Column
	private String flag3;
	@Column
	private String flag4;
	@Column
	private String flag5;
	@Column
	private String flag6;
	@Column
	private String flag7;
	@Column
	private String flag8;
	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
  	
	public String getPeopleuid() {
		return peopleuid;
	}
	public void setPeopleuid(String peopleuid) {
		this.peopleuid = peopleuid;
	}
	public String getObjectuid() {
		return objectuid;
	}
	public void setObjectuid(String objectuid) {
		this.objectuid = objectuid;
	}
	public String getFlag1() {
		return flag1;
	}
	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}
	public String getFlag2() {
		return flag2;
	}
	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}
	public String getFlag3() {
		return flag3;
	}
	public void setFlag3(String flag3) {
		this.flag3 = flag3;
	}
	public String getFlag4() {
		return flag4;
	}
	public void setFlag4(String flag4) {
		this.flag4 = flag4;
	}
	public String getFlag5() {
		return flag5;
	}
	public void setFlag5(String flag5) {
		this.flag5 = flag5;
	}
	public String getFlag6() {
		return flag6;
	}
	public void setFlag6(String flag6) {
		this.flag6 = flag6;
	}
	public String getFlag7() {
		return flag7;
	}
	public void setFlag7(String flag7) {
		this.flag7 = flag7;
	}
	public String getFlag8() {
		return flag8;
	}
	public void setFlag8(String flag8) {
		this.flag8 = flag8;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}