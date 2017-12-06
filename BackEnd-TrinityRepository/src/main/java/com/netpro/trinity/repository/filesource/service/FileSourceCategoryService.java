package com.netpro.trinity.repository.filesource.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.filesource.dao.jpa.FileSourceCategoryJPADao;
import com.netpro.trinity.repository.filesource.entity.jpa.FileSourceCategory;
import com.netpro.trinity.repository.util.Constant;

@Service
public class FileSourceCategoryService {
	public static final String[] FS_CATEGORY_FIELD_VALUES = new String[] {"fscategoryname", "description"};
	public static final Set<String> FS_CATEGORY_FIELD_SET = new HashSet<>(Arrays.asList(FS_CATEGORY_FIELD_VALUES));
	
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
		
		FileSourceCategory category = this.dao.findOne(uid);
		if(category == null)
			throw new IllegalArgumentException("File Source Category UID does not exist!(" + uid + ")");

		return category;
	}
	
	public List<FileSourceCategory> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("File Source Category Name can not be empty!");
		
		return this.dao.findByfscategoryname(name.toUpperCase());
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null) {
			return ResponseEntity.ok(this.dao.findAll());
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			return ResponseEntity.ok(this.dao.findAll());
		}
		
		PageRequest pageRequest = null;
		Sort sort = null;
		
		if(paging != null) {
			pageRequest = getPagingAndOrdering(paging, ordering);
		}else {
			if(ordering != null) {
				sort = getOrdering(ordering);
			}
		}
		
		if(querying == null) {
			if(pageRequest != null) {
				return ResponseEntity.ok(this.dao.findAll(pageRequest));
			}else if(sort != null) {
				return ResponseEntity.ok(this.dao.findAll(sort));
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				return ResponseEntity.ok(this.dao.findAll());
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !FS_CATEGORY_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ FS_CATEGORY_FIELD_SET.toString());
			if(querying.getIgnoreCase() == null)
				querying.setIgnoreCase(false);
			
			String queryType = querying.getQueryType().toLowerCase();
			String queryField = querying.getQueryField().toLowerCase(); //Must be lower case for jpa method
			String queryString = querying.getQueryString();
			
			StringBuffer methodName = new StringBuffer("findBy");
			methodName.append(queryField);
			if(queryType.equals("like")) {
				methodName.append("Like");
				queryString = "%" + queryString + "%";
			}
			if(querying.getIgnoreCase()) {
				methodName.append("IgnoreCase");
			}	

			Method method = null;
			if(pageRequest != null){
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
				Page<FileSourceCategory> page_category = (Page<FileSourceCategory>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_category);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<FileSourceCategory> categories = (List<FileSourceCategory>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(categories);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<FileSourceCategory> categories = (List<FileSourceCategory>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(categories);
			}
		}
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

		FileSourceCategory old_category = this.dao.findOne(uid);
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
		if(relService.exitByCategoryUid(uid))
			throw new IllegalArgumentException("Delete File Source under this category first!");

		this.dao.delete(uid);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception{	
		if(paging.getNumber() == null)
			paging.setNumber(0);
		
		if(paging.getSize() == null)
			paging.setSize(10);
		
		if(ordering != null) {
			return new PageRequest(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return new PageRequest(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromStringOrNull("DESC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromStringOrNull(ordering.getOrderType());
		
		Order order = new Order(direct, "lastupdatetime");
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
}
