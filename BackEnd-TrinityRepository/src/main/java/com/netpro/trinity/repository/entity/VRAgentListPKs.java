package com.netpro.trinity.repository.entity;

import java.io.Serializable;

public class VRAgentListPKs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String virtualagentuid;
	private String agentuid;
	
	public VRAgentListPKs() {}
	
	public VRAgentListPKs(String virtualagentuid, String agentuid) {
		this.setVirtualagentuid(virtualagentuid);
		this.setAgentuid(agentuid);
	}
	
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
	
	@Override
    public int hashCode() {
		return virtualagentuid.hashCode() + agentuid.hashCode();
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VRAgentListPKs other = (VRAgentListPKs) obj;
		if (virtualagentuid == null) {
			if (other.virtualagentuid != null)
				return false;
		} else if (!virtualagentuid.equals(other.virtualagentuid))
			return false;
		if (agentuid == null) {
			if (other.agentuid != null)
				return false;
		} else if (!agentuid.equals(other.agentuid))
			return false;
		return true;
	}
}