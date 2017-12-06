package com.netpro.trinity.repository.connection.entity.jdbc;

public class ConnectionRelation {
	private String conncategoryuid;
	private String connectionuid;

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
	
	@Override
	public String toString() {
		return "ConnectionRelation{" + "conncategoryuid='" + conncategoryuid + '\'' + ", "
				+ "connectionuid='" + connectionuid + '\'' + '}';
	}



	
}