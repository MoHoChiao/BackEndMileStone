package com.netpro.trinity.resource.admin.externalrule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name="resdoc")
@IdClass(ResdocPKs.class)
public class Resdoc {
	@Id
	private String module;
	@Id
	private String resname;
	@Id
	private String langcode;
	@Column
	private String document;
	
	public void setModule(String module) {
		this.module = module;
	}
	public String getModule() {
		return module;
	}
	public void setResname(String resname) {
		this.resname = resname;
	}
	public String getResname() {
		return resname;
	}
	public String getLangcode() {
		return langcode;
	}
	public void setLangcode(String langcode) {
		this.langcode = langcode;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
}