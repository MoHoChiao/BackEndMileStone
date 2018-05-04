package com.netpro.trinity.repository.service.externalrule;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.externalrule.DmExtJarJDBCDao;
import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtJar;

@Service
public class DmExtJarService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtJarService.class);
	
	@Autowired
	private DmExtJarJDBCDao dao;
	
	@Autowired
	private DmExtPackageService packageService;
	
	public DmExtJar getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		return this.dao.findByUid(uid);
	}
	
	public Boolean existByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");

		return this.dao.existByUid(uid);
	}
	
	public List<DmExtJar> getByPackageUid(String packageUid) throws IllegalArgumentException, Exception{
		if(packageUid == null || packageUid.isEmpty())
			throw new IllegalArgumentException("Package UID can not be empty!");

		return this.dao.findByPackageUid(packageUid);
	}
	
	public DmExtJar add(DmExtJar jar) throws IllegalArgumentException, Exception{
		String extjaruid = jar.getExtjaruid();
		if(null == extjaruid || extjaruid.trim().isEmpty())
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		if(this.dao.existByUid(extjaruid))
			throw new IllegalArgumentException("Duplicate External Jar UID!");
		
		String packageuid = jar.getPackageuid();
		if(null == packageuid || packageuid.trim().isEmpty())
			throw new IllegalArgumentException("Package UID can not be empty!");
		
		if(!this.packageService.existByUid(packageuid))
			throw new IllegalArgumentException("Package UID does not exist!(" + packageuid + ")");
		
		String filename = jar.getFilename();
		if(null == filename || filename.trim().isEmpty())
			throw new IllegalArgumentException("File Name can not be empty!!");
		
		String description = jar.getDescription();
		if(null == description)
			jar.setDescription("");
		
		if(this.dao.save(jar) > 0)
			return jar;
		else
			throw new IllegalArgumentException("Add External Jar Fail!");
	}
	
	public List<DmExtJar> add(String packageUid, List<DmExtJar> jars) throws IllegalArgumentException, Exception{
		List<DmExtJar> new_jars = new ArrayList<DmExtJar>();
		
		if(null == jars)
			return new_jars;
		
		for(DmExtJar jar: jars) {
			try {
				jar.setPackageuid(packageUid);
				this.add(jar);
				new_jars.add(jar);
			}catch(Exception e) {
				DmExtJarService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_jars;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("External Jar UID can not be empty!");
		
		this.dao.deleteByUid(uid);
	}
	
	public void deleteByPackageUid(String packageUid) throws IllegalArgumentException, Exception{
		if(null == packageUid || packageUid.trim().length() <= 0)
			throw new IllegalArgumentException("Package UID UID can not be empty!");
		
		this.dao.deleteByPackageUid(packageUid);
	}
}
