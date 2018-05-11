package com.netpro.trinity.repository.service.externalrule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import com.netpro.trinity.repository.dao.jdbc.externalrule.DmExtJarJDBCDao;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtJar;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtRule;
import com.netpro.trinity.repository.entity.externalrule.jpa.Dmextpackage;
import com.netpro.trinity.repository.prop.TrinitySysSetting;
import com.netpro.trinity.repository.service.resdoc.ResdocService;
import com.netpro.trinity.repository.service.transformrule.TransformruleService;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.XMLUtil;
import com.netpro.trinity.repository.util.drivermanager.FileDetailUtil;
import com.netpro.trinity.repository.util.drivermanager.JCSServerCommandUtil;
import com.netpro.trinity.repository.util.externalrule.SettingRuleFileUtil;

@Service
public class DmExtJarService {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
	
	@Autowired
	private DmExtJarJDBCDao dao;
	
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@Autowired
	private DmExtPackageService packageService;
	@Autowired
	private DmExtRuleService ruleService;
	@Autowired
	private JCSServerCommandUtil jcsCmdUtil;
	
	public DmExtJar getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		DmExtJar jar = this.dao.findByUid(uid);
		if(null == jar)
			throw new IllegalArgumentException("External Jar UID does not exist!(" + uid + ")");
		
		return jar;
	}
	
	public List<DmExtJar> getByPackageUid(String packageUid) throws IllegalArgumentException, Exception{
		if(packageUid == null || packageUid.isEmpty())
			throw new IllegalArgumentException("Package UID can not be empty!");

		return this.dao.findByPackageUid(packageUid);
	}
	
	public DmExtJar getByFileName(String fileName) throws IllegalArgumentException, Exception{
		if(fileName == null || fileName.isEmpty())
			throw new IllegalArgumentException("File Name can not be empty!");
		
		DmExtJar jar = this.dao.findByFileName(fileName);
		if(null == jar)
			throw new IllegalArgumentException("File Name does not exist!(" + fileName + ")");
				
		return jar;
	}
	
	public DmExtJar modify(String packageUid, String description, MultipartFile file) throws UnknownHostException, IOException, Exception{
		if(null == packageUid || packageUid.trim().isEmpty())
			throw new IllegalArgumentException("Package Uid can not be empty!");
		
		Dmextpackage p = this.packageService.getByUid(true, packageUid);
		
		if(null == file)
			throw new IllegalArgumentException("Constant.LIBRARY File can not be empty!");
		
		if(null == description)
			description = "";
		
		String fileName = file.getOriginalFilename();
		fileName = fullFileName(p.getPackagename(), fileName);
		if(this.dao.existByFileName(fileName))
			throw new IllegalArgumentException("Duplicate File Name! ("+fileName+")");
		
		checkDir(fileName);
		
		FileOutputStream fos = null;
		try {
			byte[] bytes = file.getBytes();
			
			File filePath = new File(this.trinitySys.getDir().getExtlib(), fileName);
			fos = new FileOutputStream(filePath);
			fos.write(bytes);
			fos.flush();
			String md5 = FileDetailUtil.getFileMD5(filePath);
			String fileType = checkFileType(filePath);
			
			DmExtJar jar = this.dao.findByFileName(fileName);
			if (null != jar){ // update
				jar.setData(bytes);
				jar.setMd5(md5);
				jar.setFiletype(fileType);
				jar.setDescription(description);
				jar.setUploadtime(sdf.format(new Date()));
				this.dao.update(jar);
			} else { // insert
				jar = new DmExtJar();
				jar.setExtjaruid(UUID.randomUUID().toString());
				jar.setFilename(fileName);
				jar.setPackageuid(packageUid);
				jar.setData(bytes);
				jar.setMd5(md5);
				jar.setUploadtime(sdf.format(new Date()));
				jar.setFiletype(fileType);
				jar.setDescription(description);
				this.dao.save(jar);
			}
			jcsCmdUtil.sendCommandToServer("publishexternallib");
			
			jar.setData(null);	//不需要回傳data資料
			
			return jar;
		}finally {
			try {
				if(null != fos)
					fos.close();
			}catch(Exception e) {}
		}
	}
	
	public void deleteByPackageUid(String packageUid) throws IllegalArgumentException, IOException, Exception{
		if(null == packageUid || packageUid.trim().length() <= 0)
			throw new IllegalArgumentException("Package UID can not be empty!");
		
		Dmextpackage p = packageService.getByUid(true, packageUid);
		
//		List<String> jarUids = new ArrayList<String>();
		
		List<DmExtJar> jars = this.dao.findByPackageUid(packageUid);
//		for(DmExtJar jar : jars) 
//			jarUids.add(jar.getExtjaruid());
//		
//		if(SettingRuleFileUtil.haveRulePublishByPackage(jarUids))
//			throw new IllegalArgumentException("External Rule(s) have been published to JCS Agnet!");
		
		//刪除package底下的全部jar files資料
		for(DmExtJar jar : jars)
			this.deleteByUid(jar.getExtjaruid());
		
		//刪除該package資料夾
		File dir = new File(this.trinitySys.getDir().getExtlib(), p.getPackagename());
		FileSystemUtils.deleteRecursively(dir);
		
		//最後刪除
		this.dao.deleteByPackageUid(packageUid);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, IOException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		//刪除jar底下的全部rule資料
		this.ruleService.deleteByExtJarUid(uid);
		
		//刪除該檔案
		String fileName = this.getByUid(uid).getFilename();
		File file = new File(this.trinitySys.getDir().getExtlib(), fileName);
		file.delete();
		
		this.dao.deleteByUid(uid);
	}
	
	public Boolean existByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		return this.dao.existByUid(uid);
	}
	
	private String fullFileName(String pName, String fileName){
		String ret = "";
		String[] temps = fileName.split(Constant.SEPARATOR);
		int len = temps.length;
		if (len == 1){
			if (fileName.endsWith(Constant.CLASSES)){
				ret = pName + Constant.SEPARATOR + Constant.FOLDER_CLASS + Constant.SEPARATOR + fileName;
			} else {
				ret = pName + Constant.SEPARATOR + fileName;
			}
		} else {
			ret = fileName;
		}
		
		return ret;
	}
	
	private void checkDir(String filePath){
		String[] files = filePath.split(Constant.SEPARATOR);
		int size = files.length;
		if (size == 1)
			return;
		
		String filename = files[size - 1];
		String dirPath = filePath.substring(0, filePath.lastIndexOf(filename));
		
		File dir = new File(this.trinitySys.getDir().getExtlib(), dirPath);
		if (!dir.exists())
			dir.mkdirs();
	}
	
	private String checkFileType(File file) throws Exception {
		ZipFile zipFile = null;
		URLConnection c = null;
		try {
			if (file.getName().endsWith(Constant.CLASSES))
				return Constant.RULE_CLASS;

			if (!file.getName().endsWith(".jar"))
				return Constant.LIBRARY;

			zipFile = new ZipFile(file);
			URL url = new URL("jar", "", -1, new File(zipFile.getName()).toURI().toString() + "!/");

			Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
			while (zipEntrys.hasMoreElements()) {
				c = url.openConnection();
				URLClassLoader ucl = new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
				ZipEntry zipEntry = zipEntrys.nextElement();
				if (!zipEntry.getName().endsWith(Constant.CLASSES))
					continue;

				if (check(zipEntry, ucl) != "")
					return Constant.RULE_JAR;
			}
			return Constant.LIBRARY;
		} catch (Exception e){
			throw e;
		} finally {
			try {
				if (zipFile != null)
					zipFile.close();
				if (c != null)
					((JarURLConnection)c).getJarFile().close();
			} catch (Exception e) {}
		}
	}
	
	private String check(ZipEntry zipEntry, URLClassLoader ucl) {
		String className = "";
		try {
			className = zipEntry.getName().replace("/", ".");
			className = className.substring(0, className.length() - 6);

			Class<?> objClass = Class.forName(className, false, ucl);

			if (isImplementer(objClass, "com.netpro.dm.core.ITransformRule")) {
				return objClass.getName();
			}
		} catch (Throwable e){}
		return "";
	}
	
	private boolean isImplementer(Class<?> cls, String interfaze){
		Class<?> []ints = cls.getInterfaces();
		for(Class<?> intf : ints) {
			if(intf.getName().equals(interfaze)){
				return true;
			}
		}
		return false;
	}
}
