package com.netpro.trinity.repository.service.filesource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.filesource.FilesourceRelationJDBCDao;
import com.netpro.trinity.repository.entity.filesource.jdbc.FilesourceRelation;

@Service
public class FilesourceRelationService {
	
	@Autowired
	private FilesourceRelationJDBCDao dao;
	
	@Autowired
	private FileSourceCategoryService categoryService;
	@Autowired
	private FileSourceService filesourceService;
	
	public List<String> getAllFileSourceUids() throws Exception{
		return this.dao.findAllFileSourceUids();
	}
	
	public List<String> getFileSourceUidsByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("File Source Category UID can not be empty!");
				
		return this.dao.findFileSourceUidsByCategoryUid(uid);
	}
	
	public FilesourceRelation add(FilesourceRelation rel) throws IllegalArgumentException, Exception{
		String fscategoryuid = rel.getFscategoryuid();
		if(null == fscategoryuid || fscategoryuid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Category UID can not be empty!");
		
		if(!this.categoryService.existByUid(fscategoryuid))
			throw new IllegalArgumentException("File Source Category UID does not exist!(" + fscategoryuid + ")");
		
		String filesourceuid = rel.getFilesourceuid();
		if(null == filesourceuid || filesourceuid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source UID can not be empty!");
	
		if(!this.filesourceService.existByUid(filesourceuid))
			throw new IllegalArgumentException("File Source UID does not exist!(" + filesourceuid + ")");
		
		if(this.dao.save(rel) > 0)
			return rel;
		else
			throw new IllegalArgumentException("Add File Source Relation Fail!");
	}
	
	public void deleteByFileSourceUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Uid can not be empty!");
		
		this.dao.deleteByFileSourceUid(uid);
	}
	
	public Boolean exitByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("File Source Category UID can not be empty!");
				
		return this.dao.exitByCategoryUid(uid);
	}
}
