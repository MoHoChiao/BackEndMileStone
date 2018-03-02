package com.netpro.trinity.repository.service.member;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jpa.member.TrinityuserJPADao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.entity.member.jpa.Trinityuser;
import com.netpro.trinity.repository.util.Constant;

@Service
public class TrinityuserService {
	public static final String[] USER_FIELD_VALUES = new String[] { "username", "activate", "usertype", "description"};
	public static final Set<String> USER_FIELD_SET = new HashSet<>(Arrays.asList(USER_FIELD_VALUES));
	
	@Autowired
	private TrinityuserJPADao dao;
	
	public List<Trinityuser> getAll() throws Exception{
		List<Trinityuser> users = this.dao.findAll();
		return users;
	}
	
	public Trinityuser getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Trinity User UID can not be empty!");
		
		Trinityuser user = this.dao.findOne(uid);
		if(null == user)
			throw new IllegalArgumentException("Trinity User UID does not exist!(" + uid + ")");
		return user;
	}
	
	public List<Trinityuser> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Name can not be empty!");
		
		return this.dao.findByusername(name);
	}
	
	public List<Trinityuser> getByUserType(String userType) throws IllegalArgumentException, Exception{
		if(userType == null || userType.trim().isEmpty())
			throw new IllegalArgumentException("Trinity User Type can not be empty!");
		
		return this.dao.findByusertype(userType.toUpperCase());
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<Trinityuser> users = this.dao.findAll();
			return ResponseEntity.ok(users);
		}
			
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<Trinityuser> users = this.dao.findAll();
			return ResponseEntity.ok(users);
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
		
		if(null == querying) {
			if(pageRequest != null) {
				Page<Trinityuser> page_user = this.dao.findAll(pageRequest);
				return ResponseEntity.ok(page_user);
			}else if(sort != null) {
				List<Trinityuser> users = this.dao.findAll(sort);
				return ResponseEntity.ok(users);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Trinityuser> users = this.dao.findAll(sort);
				return ResponseEntity.ok(users);
			}
		}else {
			if(null == querying.getQueryType() || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(null == querying.getQueryField() || !USER_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ USER_FIELD_SET.toString());
			if(null == querying.getIgnoreCase())
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
				Page<Trinityuser> page_user = (Page<Trinityuser>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_user);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Trinityuser> users = (List<Trinityuser>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(users);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Trinityuser> users = (List<Trinityuser>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(users);
			}
		}
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
		
		Order order = new Order(direct, "username");
		if(ordering.getOrderField() != null && USER_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
}
