package com.netpro.trinity.repository.service.frequency;

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

import com.netpro.trinity.repository.dao.jpa.frequency.FrequencyCategoryJPADao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.entity.frequency.jpa.FrequencyCategory;
import com.netpro.trinity.repository.util.Constant;

@Service
public class FrequencyCategoryService {
	public static final String[] FREQ_CATEGORY_FIELD_VALUES = new String[] {"freqcategoryname", "description"};
	public static final Set<String> FREQ_CATEGORY_FIELD_SET = new HashSet<>(Arrays.asList(FREQ_CATEGORY_FIELD_VALUES));
	
	@Autowired
	private FrequencyCategoryJPADao dao;
	
	@Autowired
	private FrequencyRelationService relService;
	
	public List<FrequencyCategory> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public FrequencyCategory getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Frequency Category UID can not be empty!");
		
		FrequencyCategory category = this.dao.findOne(uid);
		if(category == null)
			throw new IllegalArgumentException("Frequency Category UID does not exist!(" + uid + ")");

		return category;
	}
	
	public List<FrequencyCategory> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Frequency Category Name can not be empty!");
		
		return this.dao.findByfreqcategoryname(name.toUpperCase());
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
			if(querying.getQueryField() == null || !FREQ_CATEGORY_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ FREQ_CATEGORY_FIELD_SET.toString());
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
				Page<FrequencyCategory> page_category = (Page<FrequencyCategory>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_category);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<FrequencyCategory> categories = (List<FrequencyCategory>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(categories);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<FrequencyCategory> categories = (List<FrequencyCategory>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(categories);
			}
		}
	}
	
	public FrequencyCategory add(FrequencyCategory category) throws IllegalArgumentException, Exception{
		category.setFreqcategoryuid(UUID.randomUUID().toString());
		
		String name = category.getFreqcategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category Name can not be empty!");
		category.setFreqcategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getFreqcategoryname()))
			throw new IllegalArgumentException("Duplicate Frequency Category Name!");
		
		if(null == category.getDescription())
			category.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		category.setLastupdatetime(new Date());
				
		return this.dao.save(category);
	}
	
	public FrequencyCategory edit(FrequencyCategory category) throws IllegalArgumentException, Exception{
		String uid = category.getFreqcategoryuid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category Uid can not be empty!");

		FrequencyCategory old_category = this.dao.findOne(uid);
		if(null == old_category)
			throw new IllegalArgumentException("Frequency Category Uid does not exist!(" + uid + ")");
		
		String name = category.getFreqcategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category Name can not be empty!");
		category.setFreqcategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getFreqcategoryname()) && !old_category.getFreqcategoryname().equalsIgnoreCase(category.getFreqcategoryname()))
			throw new IllegalArgumentException("Duplicate Frequency Category Name!");
		
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
			throw new IllegalArgumentException("Frequency Category Uid can not be empty!");
		if(relService.exitByCategoryUid(uid))
			throw new IllegalArgumentException("Delete Frequency under this category first!");

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
