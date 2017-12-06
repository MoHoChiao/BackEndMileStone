package com.netpro.trinity.repository.jcsagent.entity.jdbc;

public class VRAgentList {
	private String virtualagentuid;
	private String agentuid;
	private String activate;
	private String description;
	private Integer seq;
	private String agentname;

	public String getVirtualagentuid() {
		return virtualagentuid;
	}

	public void setVirtualagentuid(String virtualagentuid) {
		this.virtualagentuid = virtualagentuid;
	}

	public String getAgentuid() {
		return agentuid;
	}

	public void setAgentuid(String agentuid) {
		this.agentuid = agentuid;
	}

	public String getActivate() {
		return activate;
	}

	public void setActivate(String activate) {
		this.activate = activate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	
	@Override
	public String toString() {
		return "VRAgentList{" + "virtualagentuid='" + virtualagentuid + '\'' + ", "
				+ "agentuid='" + agentuid + '\'' + ", "
				+ "activate='" + activate + '\'' + ", "
				+ "description='" + description + '\'' + ", "
				+ "seq=" + seq + ", "
				+ "agentname='" + agentname + '\'' + '}';
	}
}