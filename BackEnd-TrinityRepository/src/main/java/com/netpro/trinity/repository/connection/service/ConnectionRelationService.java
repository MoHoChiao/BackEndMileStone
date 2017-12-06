package com.netpro.trinity.repository.connection.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.connection.dao.jdbc.ConnectionRelationJDBCDao;
import com.netpro.trinity.repository.connection.entity.jdbc.ConnectionRelation;

@Service
public class ConnectionRelationService {
	
	@Autowired
	private ConnectionRelationJDBCDao dao;
	
	@Autowired
	private ConnectionCategoryService categoryService;
	@Autowired
	private ConnectionService connService;
	
	public List<String> getAllConnectionUids() throws Exception{
		return this.dao.findAllConnectionUids();
	}
	
	public List<String> getConnectionUidsByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Connection Category UID can not be empty!");
				
		return this.dao.findConnectionUidsByCategoryUid(uid);
	}
	
	public ConnectionRelation add(ConnectionRelation rel) throws IllegalArgumentException, Exception{
		String conncategoryuid = rel.getConncategoryuid();
		if(null == conncategoryuid || conncategoryuid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Category UID can not be empty!");
		
		if(!this.categoryService.existByUid(conncategoryuid))
			throw new IllegalArgumentException("Connection Category UID does not exist!(" + conncategoryuid + ")");
		
		String connectionuid = rel.getConnectionuid();
		if(null == connectionuid || connectionuid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection UID can not be empty!");
	
		if(!this.connService.existByUid(connectionuid))
			throw new IllegalArgumentException("Connection UID does not exist!(" + connectionuid + ")");
		
		if(this.dao.save(rel) > 0)
			return rel;
		else
			throw new IllegalArgumentException("Add Connection Relation Fail!");
	}
	
	public void deleteByConnectionUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
		this.dao.deleteByConnectionUid(uid);
	}
	
	public Boolean exitByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Connection Category UID can not be empty!");
				
		return this.dao.exitByCategoryUid(uid);
	}
}
