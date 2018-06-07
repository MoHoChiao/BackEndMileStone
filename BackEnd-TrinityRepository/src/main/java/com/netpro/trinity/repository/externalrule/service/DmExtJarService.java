package com.netpro.trinity.repository.externalrule.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.repository.drivermanager.lib.FileDetailUtil;
import com.netpro.trinity.repository.drivermanager.lib.JCSServerCommandUtil;
import com.netpro.trinity.repository.externalrule.dao.DmExtJarJDBCDao;
import com.netpro.trinity.repository.externalrule.entity.DmExtJar;
import com.netpro.trinity.repository.externalrule.entity.Dmextpackage;
import com.netpro.trinity.repository.externalrule.lib.ExernalRuleUtil;
import com.netpro.trinity.repository.prop.TrinitySysSetting;

@Service
public class DmExtJarService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtJarService.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
	
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
	@Autowired
	private ExernalRuleUtil ruleFileUtil;
	
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
	
	public DmExtJar addJar(String packageUid, String description, MultipartFile file) throws IllegalArgumentException, Exception {
		if(null == packageUid || packageUid.trim().isEmpty())
			throw new IllegalArgumentException("Package Uid can not be empty!");
		
		Dmextpackage p = this.packageService.getByUid(true, packageUid);
		
		if(null == file)
			throw new IllegalArgumentException("Library File can not be empty!");
		
		if(null == description)
			description = "";
		
		String fileName = file.getOriginalFilename();
		fileName = ruleFileUtil.fullFileName(p.getPackagename(), fileName);
		if(this.dao.existByFileName(fileName))
			throw new IllegalArgumentException("Duplicate File Name! ("+fileName+")");
		
		ruleFileUtil.checkDir(fileName);
		
		FileOutputStream fos = null;
		try {
			byte[] bytes = file.getBytes();
			
			File filePath = new File(this.trinitySys.getDir().getExtlib(), fileName);
			fos = new FileOutputStream(filePath);
			fos.write(bytes);
			fos.flush();
			String md5 = FileDetailUtil.getFileMD5(filePath);
			String fileType = ruleFileUtil.checkFileType(filePath);
			
			DmExtJar jar = this.uploadToDb(fileName, packageUid, bytes, md5, fileType, description);
			jar.setData(null);	//不需要回傳data資料
			return jar;
		}finally {
			try {
				if(null != fos)
					fos.close();
			}catch(Exception e) {}
		}
	}
	
	//此方法不對外提供, 因為單單只改變file name的值, 會造成其它錯誤, external rule模組, 可謂牽一髮而動全身啊@@"
	public String editFileNameOnly(String extJarUid, String newFileName) throws IllegalArgumentException, Exception {
		if(null == extJarUid || extJarUid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(null == newFileName || newFileName.trim().length() <= 0)
			throw new IllegalArgumentException("New File Name can not be empty!");
		
		this.dao.updateFileNameOnly(extJarUid, newFileName);
		
		return newFileName;
	}
	
	public String editDescriptionOnly(String extJarUid, String description) throws IllegalArgumentException, Exception {
		if(null == extJarUid || extJarUid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(null == description || description.trim().length() <= 0)
			description = "";
		
		this.dao.updateDescriptionOnly(extJarUid, description);
		
		return description;
	}
	
	public void deleteByPackageUid(String packageUid) throws IllegalArgumentException, IOException, Exception{
		if(null == packageUid || packageUid.trim().length() <= 0)
			throw new IllegalArgumentException("Package UID can not be empty!");
		
		Dmextpackage p = packageService.getByUid(true, packageUid);
				
		List<DmExtJar> jars = this.dao.findByPackageUid(packageUid);

		//刪除package底下的全部jar files資料
		for(DmExtJar jar : jars)
			this.deleteByUid(jar.getExtjaruid());
		
		//刪除該package資料夾
		File dir = new File(this.trinitySys.getDir().getExtlib(), p.getPackagename());
		FileSystemUtils.deleteRecursively(dir);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, IOException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		//刪除jar底下的全部rule, resdoc, transformrule等資料
		this.ruleService.deleteByExtJarUid(uid);
		
		//刪除該檔案
		String fileName = this.getByUid(uid).getFilename();
		File file = new File(this.trinitySys.getDir().getExtlib(), fileName);
		file.delete();
		
		//最後刪除自己在DmExtJar表中的紀錄
		this.dao.deleteByUid(uid);
	}
	
	public Boolean existByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		return this.dao.existByUid(uid);
	}
	
	public DmExtJar uploadToDb(String fileName, String pUID, byte[] bytes, String md5, String fileType, String des) throws Exception{
		DmExtJar dej = this.dao.findByFileName(fileName);
		
		if (dej != null){ // update
			dej.setData(bytes);
			dej.setMd5(md5);
			dej.setFiletype(fileType);
			dej.setDescription(des);
			dej.setUploadtime(sdf.format(new Date()));
			this.dao.update(dej);
		} else { // insert
			dej = new DmExtJar();
			dej.setExtjaruid(UUID.randomUUID().toString());
			dej.setFilename(fileName);
			dej.setPackageuid(pUID);
			dej.setData(bytes);
			dej.setMd5(md5);
			dej.setUploadtime(sdf.format(new Date()));
			dej.setFiletype(fileType);
			dej.setDescription(des);
			this.dao.save(dej);
		}
		
		try {
			jcsCmdUtil.sendCommandToServer("publishexternallib");
		} catch (UnknownHostException e) {
			DmExtJarService.LOGGER.error("UnknownHostException; reason was:", e);
		} catch (IOException e) {
			DmExtJarService.LOGGER.error("IOException; reason was:", e);
		} catch (Exception e) {
			DmExtJarService.LOGGER.error("Exception; reason was:", e);
		}
		
		return dej;
	}
}
