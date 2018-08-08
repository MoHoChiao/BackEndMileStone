package com.netpro.trinity.service.connection.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.connection.dao.ConnectionRelationJDBCDao;
import com.netpro.trinity.service.connection.entity.ConnectionCategory;
import com.netpro.trinity.service.connection.entity.ConnectionRelation;

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
	
	public ConnectionCategory getCategoryByConnectionUid(String uid) throws IllegalArgumentException, Exception{
		return this.dao.findCategoryByConnectionUid(uid);
	}
	
	public Map<String, String> getConnectionUidAndCategoryNameMap() throws IllegalArgumentException, Exception{
		return this.dao.findConnectionUidAndCategoryNameMap();
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
	
	public int deleteByConnectionUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
		return this.dao.deleteByConnectionUid(uid);
	}
	
	public Boolean existByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Connection Category UID can not be empty!");
				
		return this.dao.existByCategoryUid(uid);
	}
}
