package com.netpro.trinity.resource.admin.connection.service;

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

import com.netpro.trinity.resource.admin.connection.dao.ConnectionCategoryJPADao;
import com.netpro.trinity.resource.admin.connection.entity.ConnectionCategory;
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.dto.Ordering;
import com.netpro.trinity.resource.admin.dto.Paging;
import com.netpro.trinity.resource.admin.util.Constant;

@Service
public class ConnectionCategoryService {
	@Autowired
	private ConnectionCategoryJPADao dao;
	
	@Autowired
	private ConnectionRelationService relService;
	
	public List<ConnectionCategory> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public ConnectionCategory getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Connection Category UID can not be empty!");
		
		ConnectionCategory category = null;
		try {
			category = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(category == null)
			throw new IllegalArgumentException("Connection Category UID does not exist!(" + uid + ")");

		return category;
	}
	
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws Exception{
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "conncategoryname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		List<ConnectionCategory> agents = this.dao.findByConncategorynameLikeIgnoreCase(param, getOrdering(ordering));
		return ResponseEntity.ok(agents);
	}
	
	public ConnectionCategory add(ConnectionCategory category) throws IllegalArgumentException, Exception{
		category.setConncategoryuid(UUID.randomUUID().toString());
		
		String name = category.getConncategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Category Name can not be empty!");
		category.setConncategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getConncategoryname()))
			throw new IllegalArgumentException("Duplicate Connection Category Name!");
		
		if(null == category.getDescription())
			category.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		category.setLastupdatetime(new Date());
				
		return this.dao.save(category);
	}
	
	public ConnectionCategory edit(ConnectionCategory category) throws IllegalArgumentException, Exception{
		String uid = category.getConncategoryuid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Category Uid can not be empty!");
		
		ConnectionCategory old_category = null;
		try {
			old_category = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_category)
			throw new IllegalArgumentException("Connection Category Uid does not exist!(" + uid + ")");
		
		String name = category.getConncategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Category Name can not be empty!");
		category.setConncategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getConncategoryname()) && !old_category.getConncategoryname().equalsIgnoreCase(category.getConncategoryname()))
			throw new IllegalArgumentException("Duplicate Connection Category Name!");
		
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
			throw new IllegalArgumentException("Connection Category Uid can not be empty!");
		if(relService.existByCategoryUid(uid))
			throw new IllegalArgumentException("Delete Connection under this category first!");

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
