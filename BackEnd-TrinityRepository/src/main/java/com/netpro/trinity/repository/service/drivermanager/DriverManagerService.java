package com.netpro.trinity.repository.service.drivermanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.netpro.trinity.repository.drivermanager.MetadataDriverMaintain;
import com.netpro.trinity.repository.drivermanager.MetadataDriverManager;
import com.netpro.trinity.repository.dto.drivermanager.DriverInfo;
import com.netpro.trinity.repository.prop.TrinityDataJDBC;
import com.netpro.trinity.repository.prop.TrinitySysSetting;

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
		
		Map<String, DriverInfo> driverMap = this.jdbcInfo.getInfo();
		for(String name : driverMap.keySet()) {
			String owner = driverMap.get(name).getOwner();
			if(null == owner)
				continue;
			
			if(null != driverName && !driverName.trim().isEmpty())
				if(name.toUpperCase().indexOf(driverName.toUpperCase()) == -1)
					continue;
			
			DriverInfo propInfo = driverMap.get(name);
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
	
	public Boolean addDriverInfo() throws IOException {
		DriverInfo new_info = new DriverInfo();
		new_info.setDriver("driver");
		new_info.setJar("jar");
		new_info.setName("name");
		new_info.setOwner("user");
		new_info.setUrl("url");
		Map<String, DriverInfo> infos = this.jdbcInfo.getInfo();
		infos.put("test", new_info);
		
		Map<String, Map<String, Map<String, Map<String, String>>>> data_L1 = new TreeMap<String, Map<String, Map<String, Map<String, String>>>>();
		Map<String, Map<String, Map<String, String>>> data_L2 = new TreeMap<String, Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> data_L3 = new TreeMap<String, Map<String, String>>();
		
		for(String key : infos.keySet()) {
			DriverInfo info = infos.get(key);
			if(null != info) {
				String name = info.getName();
				String driver = info.getDriver();
				String url = info.getUrl();
				String jar = info.getJar();
				String owner = info.getOwner();
				
				Map<String, String> data_L4 = new TreeMap<String, String>();
				if(null != name)
					data_L4.put("name", name);
				if(null != driver)
					data_L4.put("driver", driver);
				if(null != url)
					data_L4.put("url", url);
				if(null != owner)
					data_L4.put("owner", owner);
				if(null != jar)
					data_L4.put("jar", jar);
				
				data_L3.put(key, data_L4);
			}
		}
		
		data_L2.put("info", data_L3);
		data_L1.put("trinity-data-jdbc", data_L2);
		
		
		DumperOptions options = new DumperOptions();
	     options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	     options.setPrettyFlow(true);
		
		Yaml yaml = new Yaml(options);
		FileWriter writer = new FileWriter("D:\\MyWork\\DataIntegrationService\\BackEndMileStone\\Mircoservice-Properties\\trinity-data-jdbc.yml");
//		StringWriter writer = new StringWriter();
        yaml.dump(data_L1, writer);
        System.out.println(writer.toString());
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
