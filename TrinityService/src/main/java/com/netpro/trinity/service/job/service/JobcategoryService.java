package com.netpro.trinity.service.job.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.dto.Querying;
import com.netpro.trinity.service.job.dao.JobcategoryJPADao;
import com.netpro.trinity.service.job.entity.Jobcategory;
import com.netpro.trinity.service.util.Constant;

@Service
public class JobcategoryService {
	public static final String[] JOB_CATEGORY_FIELD_VALUES = new String[] {"categoryname", "description", "activate"};
	public static final Set<String> JOB_CATEGORY_FIELD_SET = new HashSet<>(Arrays.asList(JOB_CATEGORY_FIELD_VALUES));
	
	@Autowired
	private JobcategoryJPADao dao;
	
	@Autowired
	private BusentityCategoryService relService;
	
	public List<Jobcategory> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public Jobcategory getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Job Category UID can not be empty!");
		
		Jobcategory category = null;
		try {
			category = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(category == null)
			throw new IllegalArgumentException("Job Category UID does not exist!(" + uid + ")");
		return category;
	}
	
	public List<Jobcategory> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Job Category Name can not be empty!");
		
		return this.dao.findBycategoryname(name.toUpperCase());
	}
	
	public List<Jobcategory> getByEntityUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Business Entity UID can not be empty!");
		
		List<Jobcategory> categories = this.dao.findByCategoryuidIn(relService.getCategoryUidsByEntityUid(uid), Sort.by("categoryname"));
		return categories;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(String entityUid, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		
		if(filter == null) {
			List<Jobcategory> categories;
			if(entityUid == null || entityUid.isEmpty()) {
				categories = this.dao.findAll();
			}else {
				categories = this.dao.findByCategoryuidIn(relService.getCategoryUidsByEntityUid(entityUid));
			}
			return ResponseEntity.ok(categories);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<Jobcategory> categories;
			if(entityUid == null || entityUid.isEmpty()) {
				categories = this.dao.findAll();
			}else {
				categories = this.dao.findByCategoryuidIn(relService.getCategoryUidsByEntityUid(entityUid));
			}
			return ResponseEntity.ok(categories);
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
				Page<Jobcategory> page_category;
				if(entityUid == null || entityUid.isEmpty()) {
					page_category = this.dao.findAll(pageRequest);
				}else {
					page_category = this.dao.findByCategoryuidIn(relService.getCategoryUidsByEntityUid(entityUid), pageRequest);
				}
				return ResponseEntity.ok(page_category);
			}else if(sort != null) {
				List<Jobcategory> categories;
				if(entityUid == null || entityUid.isEmpty()) {
					categories = this.dao.findAll(sort);
				}else {
					categories = this.dao.findByCategoryuidIn(relService.getCategoryUidsByEntityUid(entityUid), sort);
				}
				return ResponseEntity.ok(categories);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Jobcategory> categories;
				if(entityUid == null || entityUid.isEmpty()) {
					categories = this.dao.findAll();
				}else {
					categories = this.dao.findByCategoryuidIn(relService.getCategoryUidsByEntityUid(entityUid));
				}
				return ResponseEntity.ok(categories);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !JOB_CATEGORY_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ JOB_CATEGORY_FIELD_SET.toString());
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
			if(entityUid != null && !entityUid.isEmpty()) {
				methodName.append("AndCategoryuidIn");
			}
			
			Method method = null;
			if(pageRequest != null){
				Page<Jobcategory> page_category;
				if(entityUid == null || entityUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					page_category = (Page<Jobcategory>) method.invoke(this.dao, queryString, pageRequest);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class, List.class);
					page_category = (Page<Jobcategory>) method.invoke(this.dao, queryString, pageRequest, relService.getCategoryUidsByEntityUid(entityUid));
				}
				return ResponseEntity.ok(page_category);
			}else if(sort != null) {
				List<Jobcategory> categories;
				if(entityUid == null || entityUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					categories = (List<Jobcategory>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, List.class);
					categories = (List<Jobcategory>) method.invoke(this.dao, queryString, sort, relService.getCategoryUidsByEntityUid(entityUid));
				}
				return ResponseEntity.ok(categories);
			}else {
				List<Jobcategory> categories;
				if(entityUid == null || entityUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					categories = (List<Jobcategory>) method.invoke(this.dao, queryString);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, List.class);
					categories = (List<Jobcategory>) method.invoke(this.dao, queryString, relService.getCategoryUidsByEntityUid(entityUid));
				}
				return ResponseEntity.ok(categories);
			}
		}
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Job Category Uid can not be empty!");
		
		this.dao.deleteById(uid);
		this.relService.deleteByCategoryUid(uid);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}
	
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
