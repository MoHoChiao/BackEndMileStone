package com.netpro.trinity.repository.externalrule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="transformrule")  //宣告這是一個實體transformrule的類別
public class Transformrule {
	@Id
  	private String rule;
	@Column(nullable=false)
  	private String needargument;
	@Column
  	private String argtemplate;
	@Column
  	private String ruledescription;
  	
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getNeedargument() {
		return needargument;
	}
	public void setNeedargument(String needargument) {
		this.needargument = needargument;
	}
	public String getArgtemplate() {
		return argtemplate;
	}
	public void setArgtemplate(String argtemplate) {
		this.argtemplate = argtemplate;
	}
	public String getRuledescription() {
		return ruledescription;
	}
	public void setRuledescription(String ruledescription) {
		this.ruledescription = ruledescription;
	}
}
