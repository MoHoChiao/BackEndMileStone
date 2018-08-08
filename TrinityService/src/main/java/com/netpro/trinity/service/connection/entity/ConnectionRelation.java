package com.netpro.trinity.service.connection.entity;

public class ConnectionRelation {
	private String conncategoryuid;
	private String connectionuid;

	private String conncategoryname;
	
	public String getConncategoryuid() {
		return conncategoryuid;
	}
	public void setConncategoryuid(String conncategoryuid) {
		this.conncategoryuid = conncategoryuid;
	}
	public String getConnectionuid() {
		return connectionuid;
	}
	public void setConnectionuid(String connectionuid) {
		this.connectionuid = connectionuid;
	}
	public String getConncategoryname() {
		return conncategoryname;
	}
	public void setConncategoryname(String conncategoryname) {
		this.conncategoryname = conncategoryname;
	}
	
	@Override
	public String toString() {
		return "ConnectionRelation{" + "conncategoryuid='" + conncategoryuid + '\'' + ", "
				+ "connectionuid='" + connectionuid + '\'' + '}';
	}
}