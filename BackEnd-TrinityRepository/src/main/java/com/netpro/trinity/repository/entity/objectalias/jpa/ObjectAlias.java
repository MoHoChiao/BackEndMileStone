package com.netpro.trinity.repository.entity.objectalias.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name="objectalias")
@IdClass(ObjectAliasPKs.class)
public class ObjectAlias {
	@Id
	private String parentuid;
	@Id
	private String aliasname;
	@Column
	private String aliastype;
	@Column
	private String objectuid;
	@Column
	private String description;
	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
	
	public void setParentuid(String parentuid) {
		this.parentuid = parentuid;
	}
	public String getParentuid() {
		return parentuid;
	}
	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}
	public String getAliasname() {
		return aliasname;
	}
	public String getAliastype() {
		return aliastype;
	}
	public void setAliastype(String aliastype) {
		this.aliastype = aliastype;
	}
	public String getObjectuid() {
		return objectuid;
	}
	public void setObjectuid(String objectuid) {
		this.objectuid = objectuid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
}