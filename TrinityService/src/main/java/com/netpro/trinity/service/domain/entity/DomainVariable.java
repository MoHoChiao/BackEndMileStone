package com.netpro.trinity.service.domain.entity;

public class DomainVariable {
	private String domainuid;
	private String variablename;
	private String variablevalue;

	public String getDomainuid() {
		return domainuid;
	}

	public void setDomainuid(String domainuid) {
		this.domainuid = domainuid;
	}

	public String getVariablename() {
		return variablename;
	}

	public void setVariablename(String variablename) {
		this.variablename = variablename;
	}

	public String getVariablevalue() {
		return variablevalue;
	}

	public void setVariablevalue(String variablevalue) {
		this.variablevalue = variablevalue;
	}
	
	@Override
	public String toString() {
		return "DomainVariable{" + "domainuid='" + domainuid + '\'' + ", "
				+ "variablename='" + variablename + '\'' + ", "
				+ "variablevalue='" + variablevalue + '\'' + '}';
	}
}