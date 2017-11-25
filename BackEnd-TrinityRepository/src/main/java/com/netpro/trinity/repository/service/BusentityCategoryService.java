package com.netpro.trinity.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.jdbc.dao.BusentityCategoryDao;

@Service
public class BusentityCategoryService {
	
	@Autowired
	private BusentityCategoryDao dao;
	
	public List<String> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public List<String> getByEntityUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Entity UID can not be empty!");
				
		return this.dao.findByEntityUid(uid);
	}
	
	public Boolean exitByEntityUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Entity UID can not be empty!");
				
		return this.dao.exitByEntityUid(uid);
	}
	
	public void deleteByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Job Category Uid can not be empty!");
		
		this.dao.deleteByCategoryUid(uid);
	}
}
