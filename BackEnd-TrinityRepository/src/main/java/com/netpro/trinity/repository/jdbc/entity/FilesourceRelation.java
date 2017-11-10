package com.netpro.trinity.repository.jdbc.entity;

public class FilesourceRelation {
	private String fscategoryuid;
	private String filesourceuid;

	public String getFscategoryuid() {
		return fscategoryuid;
	}
	public void setFscategoryuid(String fscategoryuid) {
		this.fscategoryuid = fscategoryuid;
	}
	public String getFilesourceuid() {
		return filesourceuid;
	}
	public void setFilesourceuid(String filesourceuid) {
		this.filesourceuid = filesourceuid;
	}
	
	@Override
	public String toString() {
		return "FilesourceRelation{" + "fscategoryuid='" + fscategoryuid + '\'' + ", "
				+ "filesourceuid='" + filesourceuid + '\'' + '}';
	}
}