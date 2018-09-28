package com.netpro.trinity.resource.admin.filesource.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.dto.Ordering;
import com.netpro.trinity.resource.admin.dto.Paging;
import com.netpro.trinity.resource.admin.filesource.dao.FileSourceCategoryJPADao;
import com.netpro.trinity.resource.admin.filesource.entity.FileSourceCategory;
import com.netpro.trinity.resource.admin.util.Constant;

@Service
public class FileSourceCategoryService {
	@Autowired
	private FileSourceCategoryJPADao dao;
	
	@Autowired
	private FilesourceRelationService relService;
	
	public List<FileSourceCategory> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public FileSourceCategory getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("File Source Category UID can not be empty!");
		
		FileSourceCategory category = null;
		try {
			category = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(category == null)
			throw new IllegalArgumentException("File Source Category UID does not exist!(" + uid + ")");

		return category;
	}
	
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws Exception{
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "fscategoryname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		List<FileSourceCategory> fsCategories = this.dao.findByFscategorynameLikeIgnoreCase(param, getOrdering(ordering));
		return ResponseEntity.ok(fsCategories);
	}
	
	public FileSourceCategory add(FileSourceCategory category) throws IllegalArgumentException, Exception{
		category.setFscategoryuid(UUID.randomUUID().toString());
		
		String name = category.getFscategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Category Name can not be empty!");
		category.setFscategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getFscategoryname()))
			throw new IllegalArgumentException("Duplicate File Source Category Name!");
		
		if(null == category.getDescription())
			category.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		category.setLastupdatetime(new Date());
				
		return this.dao.save(category);
	}
	
	public FileSourceCategory edit(FileSourceCategory category) throws IllegalArgumentException, Exception{
		String uid = category.getFscategoryuid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Category Uid can not be empty!");

		FileSourceCategory old_category = null;
		try {
			old_category = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_category)
			throw new IllegalArgumentException("File Source Category Uid does not exist!(" + uid + ")");
		
		String name = category.getFscategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Category Name can not be empty!");
		category.setFscategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getFscategoryname()) && !old_category.getFscategoryname().equalsIgnoreCase(category.getFscategoryname()))
			throw new IllegalArgumentException("Duplicate File Source Category Name!");
		
		if(null == category.getDescription())
			category.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		category.setLastupdatetime(new Date());
						
		return this.dao.save(category);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Category Uid can not be empty!");
		if(relService.existByCategoryUid(uid))
			throw new IllegalArgumentException("Delete File Source under this category first!");

		this.dao.deleteById(uid);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}
	
	@SuppressWarnings("unused")
	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception{	
		if(paging.getNumber() == null)
			paging.setNumber(0);
		
		if(paging.getSize() == null)
			paging.setSize(10);
		
		if(ordering != null) {
			return PageRequest.of(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return PageRequest.of(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromString("DESC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromString(ordering.getOrderType());
		
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "lastupdatetime");
	}
}
