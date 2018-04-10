package com.netpro.trinity.repository.service.drivermanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.netpro.trinity.repository.drivermanager.MetadataDriverMaintain;
import com.netpro.trinity.repository.drivermanager.MetadataDriverManager;
import com.netpro.trinity.repository.dto.drivermanager.DriverInfo;
import com.netpro.trinity.repository.prop.TrinityDataJDBC;
import com.netpro.trinity.repository.prop.TrinitySysSetting;

@Service
public class DriverManagerService {	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerService.class);
	
	@Autowired
	private TrinityDataJDBC jdbcInfo;
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@Autowired
	MetadataDriverManager manager;
	@Autowired
	MetadataDriverMaintain maintain;
	
	public List<DriverInfo> getDriversProp(String driverName) throws Exception {
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
			info.setJarFiles(getJarFilesByDriverName(name));
			
			if("system".equalsIgnoreCase(owner)) {
				systemList.add(info);
			}else {
				userList.add(info);
			}
		}
		systemList.addAll(userList);
		
		return systemList;
	}
	
	public List<String> getJarFilesByDriverName(String driverName) throws IllegalArgumentException, Exception{
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
	
	public DriverInfo addDriverFolderAndProp(String driverName, String driverURL, MultipartFile[] files) throws IllegalArgumentException, IOException, Exception {		
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		if(null == driverURL || driverURL.trim().isEmpty())
			throw new IllegalArgumentException("Driver URL can not be empty!");
		
		Map<String, DriverInfo> infos = this.jdbcInfo.getInfo();
		
		if(infos.containsKey(driverName))
			throw new IllegalArgumentException("Duplicate Driver Name!");
		
		String dirPath = this.trinitySys.getDir().getJdbc() + System.getProperty("file.separator") + 
					driverName + System.getProperty("file.separator");
		File dirF = new File(dirPath);
		if(!dirF.exists())
			dirF.mkdir();
				
		List<String> jarFiles = new LinkedList<String>();
		for(MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			String filePath = dirPath + fileName;
			File fileF = new File(filePath);
			if(fileF.exists())
				fileF.delete();
			byte[] bytes = file.getBytes();
			FileCopyUtils.copy(bytes, fileF);
			jarFiles.add(fileName);
		}
		
		DriverInfo info = new DriverInfo();
		info.setName(driverName);
		info.setUrl(driverURL);
		info.setJarFiles(jarFiles);
		info.setDriver("driver");
		info.setOwner("user");
		infos.put(driverName, info);
		
		if(writeProp(infos)) {
			return info;
		}else {
			throw new Exception("Write Driver Properties Fail!");
		}        
	}
	
	public String addJarFileByDriverName(String driverName, MultipartFile file) throws IllegalArgumentException, IOException, Exception {
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		if(null == file)
			throw new IllegalArgumentException("Multipart File can not be empty!");
		
		String dirPath = this.trinitySys.getDir().getJdbc() + System.getProperty("file.separator") + 
					driverName + System.getProperty("file.separator");
		File dirF = new File(dirPath);
		if(!dirF.exists())
			dirF.mkdir();
		
		String fileName = file.getOriginalFilename();
		String filePath = dirPath + fileName;
		File fileF = new File(filePath);
		if(fileF.exists())
			fileF.delete();
		
		byte[] bytes = file.getBytes();
		FileCopyUtils.copy(bytes, fileF);		
        
		return fileName;
	}
	
	public DriverInfo addDriverProp(String driverName, String driverURL) throws IllegalArgumentException, IOException, Exception {
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		if(null == driverURL || driverURL.trim().isEmpty())
			throw new IllegalArgumentException("Driver URL can not be empty!");
		
		Map<String, DriverInfo> infos = this.jdbcInfo.getInfo();
		
		if(infos.containsKey(driverName))
			throw new IllegalArgumentException("Duplicate Driver Name!");		
		
		DriverInfo info = new DriverInfo();
		info.setName(driverName);
		info.setUrl(driverURL);
		info.setDriver("driver");
		info.setOwner("user");
		infos.put(driverName, info);
		
		if(writeProp(infos)) {
			return info;
		}else {
			throw new Exception("Write Driver Properties Fail!");
		}
	}
	
	public Boolean deleteJarFile(String driverName, String jarName) throws IllegalArgumentException, IOException, Exception{
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		if(null == jarName || jarName.trim().isEmpty())
			throw new IllegalArgumentException("Jar File Name can not be empty!");
		jarName = jarName.trim();
		
		if(deleteFile(driverName, jarName)) {
			this.maintain.unload(driverName);
			try {
				this.maintain.load(driverName);
			}catch(Exception e) {}
			
			return true;
		}else {
			return false;
		}
	}
	
	public Boolean deleteDriverFolder(String driverName) throws IllegalArgumentException, IOException, Exception{
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		if(deleteFolder(driverName)) {
			this.maintain.unload(driverName);
			try {
				this.maintain.load(driverName);
			}catch(Exception e) {}
			
			return true;
		}else {
			return false;
		}
	}
	
	public String deleteDriverProp(String driverName) throws IllegalArgumentException, IOException, Exception {
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		Map<String, DriverInfo> infos = this.jdbcInfo.getInfo();
		infos.remove(driverName);
		
		if(writeProp(infos)) {
			return driverName;
		}else {
			throw new Exception("Write Driver Properties Fail!");
		}
	}
	
	
	
	private List<String> checkHaveDriver(String filePath) throws ZipException, IOException, ClassNotFoundException,  Exception, Throwable {
		ZipFile zipFile = null;
		URLConnection c = null;
		List<String> driverList = new ArrayList<String>();
		
		try {
			zipFile = new ZipFile(new File(filePath));
			URL url = new URL("jar", "", -1, new File(zipFile.getName()).toURI().toString() + "!/");
			
			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			
			while (zipEntrys.hasMoreElements()) {
				c = url.openConnection();
				URLClassLoader ucl = new URLClassLoader(new URL[] { url });
				ZipEntry zipEntry = zipEntrys.nextElement();
				if (!zipEntry.getName().endsWith(".class")) {
					continue;
				}
				String driver = "";
				if ((driver = check(zipEntry, ucl)) != "") {
					driverList.add(driver);
				}
			}
		} catch (ZipException e){
			throw e;
		} catch (IOException e){
			throw e;
		} catch (ClassNotFoundException e){
			throw e;
		} catch (Exception e){
			throw e;
		} catch (Throwable e){
			throw e;
		} finally {
			try {
				if (zipFile != null){
					zipFile.close();
				}
				if (c != null){
					((JarURLConnection)c).getJarFile().close();
				}
			} catch (Exception e) {
				DriverManagerService.LOGGER.warn("Exception; reason was:", e);
			}
		}
		return driverList;
	}
	
	private String check(ZipEntry zipEntry, URLClassLoader ucl) throws ClassNotFoundException, Exception, Throwable {
		
		String className = zipEntry.getName().replace("/", ".");
		className = className.substring(0, className.length() - 6);
	
		Class<?> objClass = Class.forName(className, false, ucl);
		if (Driver.class.isAssignableFrom(objClass)) {
			return objClass.getName();
		}else {
			return "";
		}
	}
	
	private Boolean deleteFile(String driverName, String jarName) throws IllegalArgumentException, IOException, Exception {
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
	
	private Boolean deleteFolder(String driverName) throws IllegalArgumentException, IOException, Exception {
		String filePath = this.trinitySys.getDir().getJdbc() + System.getProperty("file.separator") + driverName + System.getProperty("file.separator");
		
		File dir = new File(filePath);
		if(!dir.exists())
			throw new IllegalArgumentException("Path:"+filePath+" does not exist!");
		
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.exists()) {
				if(!file.delete())
					throw new IllegalArgumentException("Delete "+file.getAbsolutePath()+" Fail! "
							+ "Possible Causes : This jar file is in use. Please restart the server and then delete the jar file.");
			}else {
				throw new IllegalArgumentException("Jar File:"+file.getAbsolutePath()+" does not exist!");
			}
		}
		
		return dir.delete();
	}
	
	private Boolean writeProp(Map<String, DriverInfo> infos) throws IOException, Exception {
		Map<String, Map<String, Map<String, Map<String, String>>>> data_L1 = new TreeMap<String, Map<String, Map<String, Map<String, String>>>>();
		Map<String, Map<String, Map<String, String>>> data_L2 = new TreeMap<String, Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> data_L3 = new TreeMap<String, Map<String, String>>();
		
		for(String key : infos.keySet()) {
			DriverInfo each_info = infos.get(key);
			if(null != each_info) {
				String name = each_info.getName();
				String driver = each_info.getDriver();
				String url = each_info.getUrl();
				String jar = each_info.getJar();
				String owner = each_info.getOwner();
				
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
        yaml.dump(data_L1, writer);
        
        return true;
	}
}
