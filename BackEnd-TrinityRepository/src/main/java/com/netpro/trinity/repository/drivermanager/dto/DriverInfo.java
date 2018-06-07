package com.netpro.trinity.repository.drivermanager.dto;

import java.util.List;

public class DriverInfo {
	private String jar;
    private String name;
    private String driver;
    private String url;
    private String owner;
    
    private List<String> jarFiles;
	
    public String getJar() {
		return jar;
	}
	public void setJar(String jar) {
		this.jar = jar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public List<String> getJarFiles() {
		return jarFiles;
	}
	public void setJarFiles(List<String> jarFiles) {
		this.jarFiles = jarFiles;
	}
	
	@Override
	public String toString() {
		return "DriverInfo{" + "jar='" + jar + '\'' + ", "
				+ "name='" + name + '\'' + ", "
				+ "driver='" + driver + '\'' + ", "
				+ "url='" + url + '\'' + ", "
				+ "owner='" + owner + '\'' + '}';
	}
}