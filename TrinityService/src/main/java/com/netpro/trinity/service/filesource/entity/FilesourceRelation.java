package com.netpro.trinity.service.filesource.entity;

public class FilesourceRelation {
	private String fscategoryuid;
	private String filesourceuid;

	private String fscategoryname;
	
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
	public String getFscategoryname() {
		return fscategoryname;
	}
	public void setFscategoryname(String fscategoryname) {
		this.fscategoryname = fscategoryname;
	}
	
	@Override
	public String toString() {
		return "FilesourceRelation{" + "fscategoryuid='" + fscategoryuid + '\'' + ", "
				+ "filesourceuid='" + filesourceuid + '\'' + '}';
	}
}