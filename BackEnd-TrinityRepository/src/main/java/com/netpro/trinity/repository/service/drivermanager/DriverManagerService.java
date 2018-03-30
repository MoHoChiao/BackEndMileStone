package com.netpro.trinity.repository.service.drivermanager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.drivermanager.MetadataDriverMaintain;
import com.netpro.trinity.repository.drivermanager.MetadataDriverManager;
import com.netpro.trinity.repository.dto.drivermanager.DriverInfo;
import com.netpro.trinity.repository.prop.TrinityDataJDBC;
import com.netpro.trinity.repository.prop.TrinitySysSetting;
import com.netpro.trinity.repository.prop.TrinityDataJDBC.JDBCDriverInfo;

@Service
public class DriverManagerService {	
	
	@Autowired
	private TrinityDataJDBC jdbcInfo;
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@Autowired
	MetadataDriverManager manager;
	@Autowired
	MetadataDriverMaintain maintain;
	
	public List<DriverInfo> getDriversInfo(String driverName) throws Exception {
		LinkedList<DriverInfo> systemList = new LinkedList<DriverInfo>();
		LinkedList<DriverInfo> userList = new LinkedList<DriverInfo>();
		
		Map<String, JDBCDriverInfo> driverMap = this.jdbcInfo.getInfo();
		for(String name : driverMap.keySet()) {
			String owner = driverMap.get(name).getOwner();
			if(null == owner)
				continue;
			
			if(null != driverName && !driverName.trim().isEmpty())
				if(name.toUpperCase().indexOf(driverName.toUpperCase()) == -1)
					continue;
			
			JDBCDriverInfo propInfo = driverMap.get(name);
			DriverInfo info = new DriverInfo();
			info.setDriver(propInfo.getDriver());
			info.setJar(propInfo.getJar());
			info.setName(propInfo.getName());
			info.setOwner(propInfo.getOwner());
			info.setUrl(propInfo.getUrl());
			info.setJarFiles(getDriverJarFiles(name));
			
			if("system".equalsIgnoreCase(owner)) {
				systemList.add(info);
			}else {
				userList.add(info);
			}
		}
		systemList.addAll(userList);
		
		return systemList;
	}
	
	public List<String> getDriverJarFiles(String driverName) throws IllegalArgumentException, Exception{
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		List<String> retList = new ArrayList<String>();
		
		String filePath = this.trinitySys.getDir().getJdbc() + System.getProperty("file.separator") + driverName.trim();
		File f = new File(filePath);
		if(f.exists()) {
			String[] files = f.list();
			for(String file : files) {
				retList.add(file);
			}
		}
		
		return retList;
	}
	
	public Boolean addDriverInfo() {
		JDBCDriverInfo info = new JDBCDriverInfo();
		info.setDriver("driver");
		info.setJar("jar");
		info.setName("name");
		info.setOwner("user");
		info.setUrl("url");
		this.jdbcInfo.getInfo().put("test", info);
		
		return true;
	}
	
	public Boolean deleteDriverJarFile(String driverName, String jarName) throws IllegalArgumentException, Exception{
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		if(null == jarName || jarName.trim().isEmpty())
			throw new IllegalArgumentException("Jar File Name can not be empty!");
		jarName = jarName.trim();
		
		if(deleteJarFile(driverName, jarName)) {
			this.maintain.unload(driverName);
			try {
				this.maintain.load(driverName);
			}catch(Exception e) {}
			
			return true;
		}else {
			return false;
		}
	}
	
	private Boolean deleteJarFile(String driverName, String jarName) throws IllegalArgumentException, Exception {
		String filePath = this.trinitySys.getDir().getJdbc() + System.getProperty("file.separator") + driverName + System.getProperty("file.separator");
		
		File dir = new File(filePath);
		if(!dir.exists())
			throw new IllegalArgumentException("Path:"+filePath+" does not exist!");
		
		File file = new File(filePath + jarName);
		if(file.exists()) {
			if(!file.delete())
				throw new IllegalArgumentException("Delete "+jarName+" Fail! "
						+ "Possible Causes : This jar file is in use. Please restart the server and then delete the jar file.");
		}else {
			throw new IllegalArgumentException("Jar File:"+jarName+" does not exist!");
		}
		
		return true;
	}
}
