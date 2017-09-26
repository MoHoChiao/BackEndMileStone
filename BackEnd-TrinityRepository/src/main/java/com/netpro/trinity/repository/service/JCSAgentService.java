package com.netpro.trinity.repository.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.JCSAgentDao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.entity.JCSAgent;

@Service
public class JCSAgentService {
	public static final String[] ORDER_TYPE_VALUES = new String[] { "ASC", "DESC" };
	public static final Set<String> ORDER_TYPE_SET = new HashSet<>(Arrays.asList(ORDER_TYPE_VALUES));
	
	public static final String[] QUERY_TYPE_VALUES = new String[] { "equals", "like" };
	public static final Set<String> QUERY_TYPE_SET = new HashSet<>(Arrays.asList(QUERY_TYPE_VALUES));
	
	public static final String[] AGENT_FIELD_VALUES = new String[] { "agentname", "activate", "host", "port", "description" };
	public static final Set<String> AGENT_FIELD_SET = new HashSet<>(Arrays.asList(AGENT_FIELD_VALUES));
	
	@Autowired
	private JCSAgentDao dao;
	
	public List<JCSAgent> getAllAgent() throws Exception{
		return this.dao.findAll();
	}
	
	public JCSAgent getAgentById(String id) throws IllegalArgumentException, Exception{
		if(id == null || id.isEmpty())
			throw new IllegalArgumentException("Agent UID can not be empty!");
		return this.dao.findOne(id);
	}
	
	public List<JCSAgent> getAgentByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Agent Name can not be empty!");
		
		return this.dao.findByagentname(name);
	}
	
	public ResponseEntity<?> getAgentByFieldQuery(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null)
			return ResponseEntity.ok(this.dao.findAll());
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null)
			return ResponseEntity.ok(this.dao.findAll());
		
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
			if(pageRequest != null)
				return ResponseEntity.ok(this.dao.findAll(pageRequest));
			else if(sort != null)
				return ResponseEntity.ok(this.dao.findAll(sort));
			else
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				return ResponseEntity.ok(this.dao.findAll());
		}else {
			if(querying.getQueryType() == null || !QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !AGENT_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ AGENT_FIELD_SET.toString());
			if(querying.getIgnoreCase() == null)
				querying.setIgnoreCase(false);
			
			String queryType = querying.getQueryType().toLowerCase();
			String queryField = querying.getQueryField().toLowerCase(); //Must be lower case for jpa method
			String queryString = querying.getQueryString();
			
			StringBuffer methodName = new StringBuffer("findBy");
			methodName.append(queryField);
			if(queryType.equals("like")) {
				if(!queryField.equals("port")) { //Integer Field can not be Like query
					methodName.append("Like");
					queryString = "%" + queryString + "%";
				}
			}
			if(querying.getIgnoreCase()) {
				if(!queryField.equals("port")) { //Integer Field can not be IgnoreCase query
					methodName.append("IgnoreCase");
				}
			}	

			Method method = null;
			if(pageRequest != null){
				if(queryField.equals("port")) { //Integer Field must be assigned Integer class
					method = this.dao.getClass().getMethod(methodName.toString(), Integer.class, Pageable.class);
					return ResponseEntity.ok(method.invoke(this.dao, Integer.valueOf(queryString), pageRequest));
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					return ResponseEntity.ok(method.invoke(this.dao, queryString, pageRequest));
				}
			}else if(sort != null) {
				if(queryField.equals("port")) { //Integer Field must be assigned Integer class
					method = this.dao.getClass().getMethod(methodName.toString(), Integer.class, Sort.class);
					return ResponseEntity.ok(method.invoke(this.dao, Integer.valueOf(queryString), sort));
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					return ResponseEntity.ok(method.invoke(this.dao, queryString, sort));
				}
			}else {
				if(queryField.equals("port")) { //Integer Field must be assigned Integer class
					method = this.dao.getClass().getMethod(methodName.toString(), Integer.class);
					return ResponseEntity.ok(method.invoke(this.dao, Integer.valueOf(queryString)));
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					return ResponseEntity.ok(method.invoke(this.dao, queryString));
				}
			}
		}
	}
	
	public JCSAgent upsertAgent(JCSAgent agent) throws IllegalArgumentException, Exception{
		return this.dao.save(agent);
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
		if(ordering.getOrderType() != null && ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromStringOrNull(ordering.getOrderType());
		
		Order order = new Order(direct, "lastupdatetime");
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
}
