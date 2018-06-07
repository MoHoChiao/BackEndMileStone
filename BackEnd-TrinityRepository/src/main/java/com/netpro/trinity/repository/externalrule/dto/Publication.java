package com.netpro.trinity.repository.externalrule.dto;

import java.util.List;

public class Publication {
	private String agentuid;
	private String agentname;
	private List<PublishRule> publishRule;
	
	public String getAgentuid() {
		return agentuid;
	}
	public void setAgentuid(String agentuid) {
		this.agentuid = agentuid;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public List<PublishRule> getPublishRule() {
		return publishRule;
	}
	public void setPublishRule(List<PublishRule> publishRule) {
		this.publishRule = publishRule;
	}
	
	@Override
	public String toString() {
		return "Publication{" + "agentuid='" + agentuid + '\'' + ", "
				+ "agentname='" + agentname + '\'' + ", "
				+ "publishRule='" + publishRule + '\'' + '}';
	}
}