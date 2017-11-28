package com.netpro.trinity.repository.service;

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
import com.netpro.trinity.repository.jpa.dao.BusentityJPADao;
import com.netpro.trinity.repository.jpa.entity.Busentity;
import com.netpro.trinity.repository.util.Constant;

@Service
public class BusentityService {
	public static final String[] BUSENTITY_FIELD_VALUES = new String[] { "busentityname", "description" };
	public static final Set<String> BUSENTITY_FIELD_SET = new HashSet<>(Arrays.asList(BUSENTITY_FIELD_VALUES));
	
	@Autowired
	private BusentityJPADao dao;
	
	public List<Busentity> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public boolean existByUid(String uid) {
		return this.dao.exists(uid);
	}
	
	public Busentity getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Business Entity UID can not be empty!");
		
		Busentity entity = this.dao.findOne(uid);
		if(entity == null)
			throw new IllegalArgumentException("Business Entity UID does not exist!(" + uid + ")");
		return entity;
	}
	
	public List<Busentity> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Business Entity Name can not be empty!");
		
		return this.dao.findBybusentityname(name.toUpperCase());
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
			if(querying.getQueryField() == null || !BUSENTITY_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ BUSENTITY_FIELD_SET.toString());
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
				Page<Busentity> page_entity = (Page<Busentity>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_entity);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Busentity> entities = (List<Busentity>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(entities);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Busentity> entities = (List<Busentity>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(entities);
			}
		}
	}
	
	public Busentity add(Busentity entity) throws IllegalArgumentException, Exception{
		entity.setBusentityuid(UUID.randomUUID().toString());
		
		String entityname = entity.getBusentityname();
		if(null == entityname || entityname.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Name can not be empty!");
		entity.setBusentityname(entityname.toUpperCase());
		
		if(this.dao.existByName(entity.getBusentityname()))
			throw new IllegalArgumentException("Duplicate Business Entity Name!");
				
		if(null == entity.getDescription())
			entity.setDescription("");
		
		entity.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		entity.setLastupdatetime(new Date());
				
		return this.dao.save(entity);
	}
	
	public Busentity edit(Busentity entity) throws IllegalArgumentException, Exception{
		String entityuid = entity.getBusentityuid();
		if(null == entityuid || entityuid.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Uid can not be empty!");

		Busentity old_entity = this.dao.findOne(entityuid);
		if(null == old_entity)
			throw new IllegalArgumentException("Business Entity Uid does not exist!(" + entityuid + ")");
		
		String entityname = entity.getBusentityname();
		if(null == entityname || entityname.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Name can not be empty!");
		entity.setBusentityname(entityname.toUpperCase());
		
		if(this.dao.existByName(entity.getBusentityname()) && !old_entity.getBusentityname().equalsIgnoreCase(entity.getBusentityname()))
			throw new IllegalArgumentException("Duplicate Business Entity Name!");
		
		if(null == entity.getDescription())
			entity.setDescription("");
		
		entity.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		entity.setLastupdatetime(new Date());
						
		return this.dao.save(entity);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Uid can not be empty!");
		
		this.dao.delete(uid);
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
