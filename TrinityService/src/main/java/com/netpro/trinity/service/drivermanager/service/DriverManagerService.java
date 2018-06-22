package com.netpro.trinity.service.drivermanager.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Driver;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.netpro.ac.TrinityPrincipal;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;
import com.netpro.trinity.service.drivermanager.dto.DriverInfo;
import com.netpro.trinity.service.drivermanager.dto.JarFileInfo;
import com.netpro.trinity.service.drivermanager.util.JCSServerCommandUtil;
import com.netpro.trinity.service.drivermanager.util.MetadataDriverMaintain;
import com.netpro.trinity.service.drivermanager.util.PublishFileUtil;
import com.netpro.trinity.service.prop.dto.TrinityDataJDBC;
import com.netpro.trinity.service.prop.dto.TrinitySysSetting;
import com.netpro.trinity.service.util.ZipFileUtil;

@Service
public class DriverManagerService {	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerService.class);
	
	//暫時寫死, 以後再來整合
	private static final String JDBC_DRIVER_YML_PATH = "D:\\MyWork\\DataIntegrationService\\BackEndMileStone\\Mircoservice-Properties\\trinity-data-jdbc.yml";
//	private static final String JDBC_DRIVER_YML_PATH = "./Mircoservice-Properties/trinity-data-jdbc.yml";
		
	@Autowired
	private TrinityDataJDBC jdbcInfo;
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@Autowired
	private MetadataDriverMaintain maintain;
	@Autowired
	private ZipFileUtil fileUtil;
	@Autowired
	private PublishFileUtil publishUtil;
	@Autowired
	private JCSServerCommandUtil jcsCmdUtil;
	
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
	
	public List<String> getAllDriverNames() throws Exception {
		LinkedList<String> systemList = new LinkedList<String>();
		LinkedList<String> userList = new LinkedList<String>();
		
		Map<String, DriverInfo> driverMap = this.jdbcInfo.getInfo();
		for(String name : driverMap.keySet()) {
			String owner = driverMap.get(name).getOwner();
			if(null == owner)
				continue;
			
			DriverInfo propInfo = driverMap.get(name);
			
			if("system".equalsIgnoreCase(owner)) {
				systemList.add(propInfo.getName());
			}else {
				userList.add(propInfo.getName());
			}
		}
		systemList.addAll(userList);
		
		return systemList;
	}
	
	public List<String> getJarFilesByDriverName(String driverName) throws IllegalArgumentException, Exception{
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		List<String> retList = new LinkedList<String>();
		
		String dirPath = this.trinitySys.getDir().getJdbc() + File.separator + driverName.trim();
		File dir = new File(dirPath);
		if(dir.exists()) {
			String[] files = dir.list();
			for(String file : files) {
				retList.add(file);
			}
		}
		
		return retList;
	}
	
	public List<String> getDriverClassByDriverName(String driverName) throws IllegalArgumentException, Exception{
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		List<String> retList = new LinkedList<String>();
		
		String dirPath = this.trinitySys.getDir().getJdbc() + File.separator + driverName.trim();
		File dir = new File(dirPath);
		if(dir.exists()) {
			File[] files = dir.listFiles();
			for(File file : files) {
				List<String> driverList = checkHaveDriver(file);
				for(String driverClass : driverList) {
					retList.add(driverClass + " (" + file.getName() + ")");
				}
			}
		}
		return retList;
	}
	
	public DriverInfo addDriverFolderAndProp(String driverName, String driverURL, MultipartFile[] files) throws IllegalArgumentException, Exception {								
		JarFileInfo fileInfo = addJarFileByDriverName(driverName, files);
		
		String driverClass = "";
		if(null != fileInfo.getDriverClassList() && fileInfo.getDriverClassList().size() > 0) {
			driverClass = fileInfo.getDriverClassList().get(0);
		}
		
		DriverInfo driverInfo = new DriverInfo();
		try {
			driverInfo.setName(driverName);
			driverInfo.setUrl(driverURL);
			driverInfo.setDriver(driverClass);
			driverInfo = modifyDriverYAML(driverInfo);
			driverInfo.setJarFiles(getJarFilesByDriverName(driverName));
		}catch(Exception e) {
			DriverManagerService.LOGGER.warn("Exception; reason was:", e);
			try {
				driverInfo = null;
				deleteDriverFolder(driverName);
			} catch (Exception e1) {
				DriverManagerService.LOGGER.warn("Exception; reason was:", e);
			}
		}
		
		if(null == driverInfo)
			throw new Exception("Add Driver Folder And Property Fail! Please look at the error log.");
		else 
			return driverInfo;
	}
	
	public JarFileInfo addJarFileByDriverName(String driverName, MultipartFile[] files) throws IllegalArgumentException, Exception{
		if(null == driverName || driverName.trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		driverName = driverName.trim();
		
		String dirPath = this.trinitySys.getDir().getJdbc() + File.separator + 
				driverName + File.separator;
		
		File dirF = new File(dirPath);
		if(!dirF.exists())
			dirF.mkdir();
		
		List<String> fileNameList = new LinkedList<String>();
		List<String> driverClassList = new LinkedList<String>();
		
		int fileSizeCount = 0;
		for(MultipartFile file : files) {
			try {
				if(null != file) {
					String fileName = file.getOriginalFilename();
					
					if(fileName.toLowerCase().indexOf("jar") == -1)
						throw new Exception(fileName + " is illegal!  Uploaded file can only be jar file.");
					
					int fileSize = Math.round(file.getSize() / 1024L / 1024L);
					if(fileSize > 10)
						throw new Exception(fileName + " must be less than 10MB!");
					
					fileSizeCount += fileSize;
					if(fileSizeCount > 50)
						throw new Exception(fileName + " upload fail! The number of File capacity cannot be greater than 50MB.");
					
					String filePath = dirPath + fileName;
					File fileF = new File(filePath);
					if(fileF.exists())
						fileF.delete();
					
					byte[] bytes = file.getBytes();
					FileCopyUtils.copy(bytes, fileF);
					
					fileNameList.add(fileName);
					driverClassList.addAll(checkHaveDriver(fileF));
				}else {
					throw new IllegalArgumentException("Multipart File is null!");
				}
			}catch(IllegalArgumentException e) {
				DriverManagerService.LOGGER.error("IllegalArgumentException; reason was:", e);
				continue;
			}catch(IOException e) {
				DriverManagerService.LOGGER.error("IOException; reason was:", e);
				continue;
			}catch(Exception e) {
				DriverManagerService.LOGGER.error("Exception; reason was:", e);
				continue;
			}
		}
		
		JarFileInfo info = new JarFileInfo();
		info.setFileNameList(fileNameList);
		info.setDriverClassList(driverClassList);
		
		return info;
	}
	
	public DriverInfo modifyDriverYAML(DriverInfo info) throws IllegalArgumentException, IOException, Exception {
		if(null == info.getName() || info.getName().trim().isEmpty())
			throw new IllegalArgumentException("Driver Name can not be empty!");
		info.setName(info.getName().trim());
		
		if(null == info.getUrl() || info.getUrl().trim().isEmpty())
			throw new IllegalArgumentException("Driver URL can not be empty!");
		
		Map<String, DriverInfo> infos = this.jdbcInfo.getInfo();
		
		if(null == info.getDriver() || info.getDriver().trim().isEmpty())
			info.setDriver("driver.not.found");
		
		if(null == info.getOwner() || info.getOwner().trim().isEmpty()) {
			DriverInfo old_info = infos.get(info.getName());
			if(null != old_info) {
				info.setOwner(old_info.getOwner());
			}else {
				info.setOwner("user");
			}
		}
					
		infos.put(info.getName(), info);
		
		if(writeDriverYAML(infos)) {
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
		
		String dirPath = this.trinitySys.getDir().getJdbc() + File.separator + driverName + File.separator;
		String filePath = dirPath + jarName;
		File file = new File(filePath);
		if(FileSystemUtils.deleteRecursively(file)) {
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
		
		String dirPath = this.trinitySys.getDir().getJdbc() + File.separator + driverName + File.separator;
		File dir = new File(dirPath);
		
		if(!dir.exists())
			return true;
		
		if(FileSystemUtils.deleteRecursively(dir)) {
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
		
		if(writeDriverYAML(infos)) {
			return driverName;
		}else {
			throw new Exception("Write Driver Properties Fail!");
		}
	}
	
	public byte[] exportDriverZIP(HttpServletRequest request) throws IllegalArgumentException, IOException, Exception {
		String sourcePath = this.trinitySys.getDir().getJdbc() + File.separator;
		String dirPath = "";
		String targetPath = "";
		
		try {
			String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
			Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(accessToken);
			if(!"".equals(principal.getName()) && principal instanceof TrinityPrincipal) {
				
				TrinityPrincipal trinityPrinc = (TrinityPrincipal) principal;
				if(trinityPrinc.isPowerUser()) {
					String userInfo = trinityPrinc.getName();
					
					dirPath = ".." + File.separator + userInfo + File.separator;
					File dir = new File(dirPath);
					if(!dir.exists())
						dir.mkdirs();
					
					//把trinity-data-jdbc.yml檔加進來一起打包成zip, 以後import才可以用
					File sourceYML = new File(JDBC_DRIVER_YML_PATH);
					File targetYML = new File(sourcePath + "trinity-data-jdbc.yml");
					Files.copy(Paths.get(sourceYML.getPath()), 
							Paths.get(targetYML.getPath()), StandardCopyOption.REPLACE_EXISTING);
					
					targetPath = dirPath + "jdbc"+ System.currentTimeMillis() +".zip";
					fileUtil.zipFile(sourcePath, targetPath);
				}
			}
			
			if("".equals(dirPath) || "".equals(targetPath)) {
				throw new IllegalArgumentException("Authentication Information error!");
			}else {
				byte[] bFile = Files.readAllBytes(Paths.get(targetPath));
				return bFile;
			}
		}finally {
			try {
				File dir = new File(dirPath);
				if(dir.exists())
					FileSystemUtils.deleteRecursively(dir);
			}catch(Exception e) {
				DriverManagerService.LOGGER.error("Exception; reason was:", e);
			}
		}
	}
	
	public Boolean importDriverZIP(MultipartFile file) throws IllegalArgumentException, IOException, Exception {
		if(null == file)
			throw new IllegalArgumentException("Driver File can not be empty!");
		
		String tempDirPath = "";
		File tempDir = null;
		
		try{
			this.maintain.unload();
			
			String jdbcDirPath = this.trinitySys.getDir().getJdbc() + File.separator;
			File jdbcDir = new File(jdbcDirPath);
			if(!jdbcDir.exists()) jdbcDir.mkdirs();
			String jdbcDirName = jdbcDir.getName();
			String jdbcParentDirPath = jdbcDir.getParent();
			
			String jdbcTempDirPath = jdbcParentDirPath + File.separator + "temp" + File.separator + jdbcDirName;
			File jdbcTempDir = new File(jdbcTempDirPath);
			
			tempDirPath = jdbcParentDirPath + File.separator + "temp";
			tempDir = new File(tempDirPath);
			
			String uploadFilePath = jdbcParentDirPath + File.separator + "temp" + File.separator + jdbcDirName + ".zip";
			File uploadFile = new File(uploadFilePath);
			
			//temp/jdbc目錄先砍掉再建立一個空的, 若有檔案存在會發生java.nio.file.DirectoryNotEmptyException
			FileSystemUtils.deleteRecursively(tempDir);
//			this.fileUtil.deleteFolder(tempDirPath);
			jdbcTempDir.mkdirs();
			
			//先把jdbc目錄下的東西搬到temp/jdbc目錄底下(備份, 以防下面的程式出錯)
			Files.move(Paths.get(jdbcDir.getPath()), 
					Paths.get(jdbcTempDir.getPath()), StandardCopyOption.REPLACE_EXISTING);
			
			try {
				//把上傳的檔案落地在temp目錄底下
				byte[] bytes = file.getBytes();
				FileCopyUtils.copy(bytes, uploadFile);
				
				//把上傳的檔案解壓縮到data目錄底下(也就是替代原本的jdbc目錄, 舊的已經搬到temp/jdbc目錄
				File jdbcParentDir = new File(jdbcParentDirPath);
				this.fileUtil.unZipFile(uploadFilePath, jdbcParentDir + File.separator);
				
				if(jdbcDir.exists()) {//如果新的jdbc目錄存在(即解壓縮後新的jdbc目錄)
					String ymlFilePath = jdbcDirPath + "trinity-data-jdbc.yml";
					File ymlFile = new File(ymlFilePath);
					String propFilePath = jdbcDirPath + "drivers.properties";
					File propFile = new File(propFilePath);
					
					if(propFile.exists()) {	//如果drivers.properties檔案存在, 則一定是以drivers.properties檔案為更新檔
						Map<String, DriverInfo> infos = readDriverProperties(propFilePath);
						this.jdbcInfo.setInfo(infos);
						writeDriverYAML(infos);
					}else if(ymlFile.exists()) {
						Map<String, DriverInfo> new_infos = readDriverYAML(ymlFilePath);
						this.jdbcInfo.setInfo(new_infos);
						writeDriverYAML(new_infos);
					}
//					else {
//						throw new Exception("Import Properties File Error! Property File does not exist.");
//					}
					
					try {
						this.maintain.load(); //我覺得不需要
					}catch(Exception e) {}
					
					return true;
				}else {
					throw new Exception("Unzip jdbc.zip error!");
				}
			}catch(Exception e) {
				try {
					/*
					 * 當try中的程式出錯時
					 */
					
					//如果新的被解壓縮的jdbc目錄已經完成且存在, 則先刪除掉
					if(jdbcDir.exists())
						FileSystemUtils.deleteRecursively(jdbcDir);
//						this.fileUtil.deleteFolder(jdbcDirPath);
					
					//再把temp/jdbc目錄下的東西搬到jdbc目錄底下(復原)
					Files.move(Paths.get(jdbcTempDir.getPath()), 
							Paths.get(jdbcDir.getPath()), StandardCopyOption.REPLACE_EXISTING);
				}catch(Exception ex) {
					DriverManagerService.LOGGER.error("Exception; reason was:", ex);
				}
				throw e;
			}
		} finally {
			try {//刪掉為了import所建立的一切temp
				if(null != tempDir && tempDir.exists())
					FileSystemUtils.deleteRecursively(tempDir);
//					this.fileUtil.deleteFolder(tempDirPath);
			}catch(Exception e) {
				DriverManagerService.LOGGER.error("Exception; reason was:", e);
			}
		}
	}
	
	public Boolean publishDriver(List<String> driverNames) 
				throws UnknownHostException, FileNotFoundException, IOException, Exception{
		Map<String, Map<String, String>> records = new HashMap<String, Map<String,String>>();
		for(String driverName : this.jdbcInfo.getInfo().keySet()) {
			Map<String, String> map = new HashMap<String, String>();
			if(driverNames.contains(driverName)) {
				map.put("publish", "1");
			}else {
				map.put("publish", "0");
			}
			String path = "jdbc" + File.separator + driverName;
			records.put(path, map);
		}
		
		this.publishUtil.genPublishItem(records, true);
		this.jcsCmdUtil.sendCommandToServer("publishjdbcfile");
		return true;
	}
	
	private List<String> checkHaveDriver(File file) {
		ZipFile zipFile = null;
		URLConnection c = null;
		List<String> driverList = new LinkedList<String>();
		
		try {
			zipFile = new ZipFile(file);
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
			DriverManagerService.LOGGER.error("ZipException; reason was:", e);
		} catch (IOException e){
			DriverManagerService.LOGGER.error("IOException; reason was:", e);
		} catch (Exception e){
			DriverManagerService.LOGGER.error("Exception; reason was:", e);
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
	
	private String check(ZipEntry zipEntry, URLClassLoader ucl) {
		try {
			String className = zipEntry.getName().replace("/", ".");
			className = className.substring(0, className.length() - 6);
		
			Class<?> objClass = Class.forName(className, false, ucl);
			if (Driver.class.isAssignableFrom(objClass)) {
				return objClass.getName();
			}
		}catch(ClassNotFoundException e) {
		}catch(Exception e) {
		}catch(Throwable e) {
		}
		return "";
	}
	
	private Boolean writeDriverYAML(Map<String, DriverInfo> infos) throws IOException, Exception {
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
		
		FileWriter writer = null;
		try {
			Yaml yaml = new Yaml(options);
			writer = new FileWriter(JDBC_DRIVER_YML_PATH);
	        yaml.dump(data_L1, writer);
	        return true;
		}finally {
			try {
				if(null != writer)
					writer.close();
			}catch(Exception e) {
				DriverManagerService.LOGGER.error("Exception; reason was:", e);
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, DriverInfo> readDriverYAML(String filePath) throws IOException, Exception {		
		Map<String, DriverInfo> retMap = new TreeMap<String, DriverInfo>(String.CASE_INSENSITIVE_ORDER);
		Yaml yaml = new Yaml();
		FileInputStream ios = null;
		BufferedInputStream bis = null;
	    try {
	        ios = new FileInputStream(new File(filePath));
	        bis = new BufferedInputStream(ios);

	        // Parse the YAML file
	        Map<String, Object> result = (Map<String, Object>) yaml.load(bis);
	        Map<String, Object> level1 = (Map<String, Object>)result.get("trinity-data-jdbc");
	        Map<String, Object> level2 = (Map<String, Object>)level1.get("info");
	        for (String driverName : level2.keySet()) {
	        	System.out.println(driverName);
	        	Map<String, String> level3 = (Map<String, String>)level2.get(driverName);
	        	Iterator<Entry<String, String>> iter = level3.entrySet().iterator();
	        	
	        	DriverInfo info = new DriverInfo();
	        	while (iter.hasNext()) {
	        		info.setDriver(level3.get("driver"));
	        		info.setName(level3.get("name"));
	        		info.setOwner(level3.get("owner"));
	        		info.setUrl(level3.get("url"));
	        		info.setJar(level3.get("jar"));
	        		iter.next();
	        	}
	        	retMap.put(driverName, info);
	        }
	        return retMap;
	    } finally {
	    	try {
	    		if(null != ios)
		    		ios.close();
		    	if(null != bis)
		    		bis.close();
			}catch(Exception e) {
				DriverManagerService.LOGGER.error("Exception; reason was:", e);
			}
	    }
	}
	
	private Map<String, DriverInfo> readDriverProperties(String filePath) throws IOException, Exception {
		Properties props = new Properties();
		FileInputStream inFile = null;
		Map<String, DriverInfo> retMap = new TreeMap<String, DriverInfo>(String.CASE_INSENSITIVE_ORDER);
		try {
			inFile = new FileInputStream(filePath);
			props.load(new BufferedInputStream(inFile));
			Enumeration<?> e = props.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String keyArr[] = key.split("\\.");
				if(keyArr.length != 3)
					continue;
				
				DriverInfo info = retMap.get(keyArr[0]);
				if(null == info) {
					info = new DriverInfo();
					info.setName(keyArr[0]);
					retMap.put(keyArr[0], info);
				}
				
				String value = props.getProperty(key);
				if(keyArr[2].equals("driver")) {
					info.setDriver(value);
				}else if(keyArr[2].equals("url")) {
					info.setUrl(value);
				}else if(keyArr[2].equals("owner")) {
					info.setOwner(value);
				}
			}
		} finally {
			try {
				if (inFile != null)
					inFile.close();
			}catch(Exception e) {
				DriverManagerService.LOGGER.error("Exception; reason was:", e);
			}
			
		}
		return retMap;
	}
}
