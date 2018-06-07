package com.netpro.trinity.repository.drivermanager.dto;

import java.util.List;

public class JarFileInfo {
	private List<String> fileNameList;
    private List<String> driverClassList;
    private List<String> nameClassList;
	
    public List<String> getFileNameList() {
		return fileNameList;
	}
	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}
	public List<String> getDriverClassList() {
		return driverClassList;
	}
	public void setDriverClassList(List<String> driverClassList) {
		this.driverClassList = driverClassList;
	}
	public List<String> getNameClassList() {
		return nameClassList;
	}
	public void setNameClassList(List<String> nameClassList) {
		this.nameClassList = nameClassList;
	}
	
	@Override
	public String toString() {
		return "DriverInfo{" + "fileNameList='" + fileNameList.toString() + '\'' + ", "
				+ "driverClassList='" + driverClassList.toString() + '\'' + '}';
	}
}