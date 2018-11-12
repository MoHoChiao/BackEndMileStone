package com.netpro.trinity.resource.admin.job.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.job.dao.BusentityCategoryJDBCDao;
import com.netpro.trinity.resource.admin.job.entity.JobFullPath;

@Service
public class BusentityCategoryService {
	
	@Autowired
	private BusentityCategoryJDBCDao dao;
	
	public List<String> getAllCategoryUids() throws Exception{
		return this.dao.findAllCategoryUids();
	}
	
	public List<String> getCategoryUidsByEntityUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Entity UID can not be empty!");
				
		return this.dao.findCategoryUidsByEntityUid(uid);
	}
	
	public List<String> getEntityUidsByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Job Category UID can not be empty!");
				
		return this.dao.findEntityUidsByCategoryUid(uid);
	}
	
	public List<JobFullPath> getViewEntityCategoryByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Job Category UID can not be empty!");
				
		return this.dao.findViewEntityCategoryByCategoryUid(uid);
	}
	
	public Map<String, JobFullPath> getAllViewEntityCategory() throws IllegalArgumentException, Exception {
		return this.dao.findAllViewEntityCategory();
	}
	
	public Boolean existByEntityUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Entity UID can not be empty!");
				
		return this.dao.existByEntityUid(uid);
	}
	
	public void deleteByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Job Category Uid can not be empty!");
		
		this.dao.deleteByCategoryUid(uid);
	}
}
