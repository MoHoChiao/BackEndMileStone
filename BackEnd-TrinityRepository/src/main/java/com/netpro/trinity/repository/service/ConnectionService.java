package com.netpro.trinity.repository.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.netpro.trinity.repository.jpa.dao.ConnectionJPADao;
import com.netpro.trinity.repository.jpa.entity.Connection;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.XMLDataUtility;

@Service
public class ConnectionService {
	public static final String[] CONNECTION_FIELD_VALUES = new String[] { "connectionname", "connectiontype", "description" };
	public static final Set<String> CONNECTION_FIELD_SET = new HashSet<>(Arrays.asList(CONNECTION_FIELD_VALUES));
	
	/*
	 * connectiontype的值,對應如下
	 * J : JDBC Connection
	 * D : Database Connection
	 * S : Sap
	 * N : notes
	 * F : FTP
	 * O : OS
	 * M : Mail
	 */
	public static final String[] CONNECTION_TYPE_VALUES = new String[] { "J", "D", "S", "N", "F", "O", "M" };
	public static final Set<String> CONNECTION_TYPE_SET = new HashSet<>(Arrays.asList(CONNECTION_TYPE_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private ConnectionJPADao dao;
	
	public List<Connection> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public boolean existByUid(String uid) {
		return this.dao.exists(uid);
	}
	
	public Connection getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Connection UID can not be empty!");
		
		Connection conn = this.dao.findOne(uid);
		if(conn == null)
			throw new IllegalArgumentException("Connection UID does not exist!(" + uid + ")");
		return conn;
	}
	
	public List<Connection> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Connection Name can not be empty!");
		
		return this.dao.findByconnectionname(name.toUpperCase());
	}
	
	public List<Connection> getByType(String type) throws IllegalArgumentException, Exception{
		if(type == null || type.isEmpty())
			throw new IllegalArgumentException("Connection Name can not be empty!");
		
		type = type.toUpperCase();
		
		/*
		 * connectiontype的值,對應如下
		 * J : JDBC Connection
		 * D : Database Connection
		 * S : Sap
		 * N : notes
		 * F : FTP
		 * O : OS
		 * M : Mail
		 */
		if(!CONNECTION_TYPE_SET.contains(type))
				throw new IllegalArgumentException("Illegal connection type! "+ CONNECTION_FIELD_SET.toString());
		
		return this.dao.findByconnectiontype(type);
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
			if(querying.getQueryField() == null || !CONNECTION_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ CONNECTION_FIELD_SET.toString());
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
				Page<Connection> page_conn = (Page<Connection>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_conn);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Connection> conns = (List<Connection>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(conns);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Connection> conns = (List<Connection>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(conns);
			}
		}
	}
	
//	public JCSAgent add(JCSAgent agent) throws IllegalArgumentException, Exception{
//		agent.setAgentuid(UUID.randomUUID().toString());
//		
//		String agentname = agent.getAgentname();
//		if(null == agentname || agentname.trim().length() <= 0)
//			throw new IllegalArgumentException("JCSAgent Name can not be empty!");
//		agent.setAgentname(agentname.toUpperCase());
//		
//		if(this.dao.existByName(agent.getAgentname()))
//			throw new IllegalArgumentException("Duplicate Agent Name!");
//		
//		String activate = agent.getActivate();
//		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
//			throw new IllegalArgumentException("JCSAgent activate value can only be 1 or 0!");
//		
//		agent.setAgentstatus("0");
//		
//		String compresstransfer = agent.getCompresstransfer();
//		if(null == compresstransfer || (!compresstransfer.equals("1") && !compresstransfer.equals("0")))
//			throw new IllegalArgumentException("JCSAgent compresstransfer value can only be 1 or 0!");
//		
//		try{
//			Integer.valueOf(agent.getCpuweight());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent cpuweight value can only be integer!");
//		}
//		
//		try{
//			Integer.valueOf(agent.getDeadperiod());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent deadperiod value can only be integer!");
//		}
//		
//		if(null == agent.getDescription())
//			agent.setDescription("");
//		
//		if(null == agent.getEncoding())
//			agent.setEncoding("");
//		
//		String host = agent.getHost();
//		if(null == host || host.trim().length()<=0)
//			throw new IllegalArgumentException("JCSAgent Host can not be empty!");
//		
//		if(null == agent.getPort())
//			throw new IllegalArgumentException("JCSAgent Port value can only be small integer!");
//		
//		if(null == agent.getMaximumjob())
//			throw new IllegalArgumentException("JCSAgent Maximum Job value can only be small integer!");
//		
//		try{
//			Integer.valueOf(agent.getMemweight());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent memweight value can only be integer!");
//		}
//		
//		try{
//			Integer.valueOf(agent.getMonitortime());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent monitortime value can only be integer!");
//		}
//		
//		if(null == agent.getOsname())
//			agent.setOsname("");
//		
//		if(null == agent.getOstype())
//			agent.setOstype("");
//		
//		Map<String, String> xmlMap = new HashMap<String, String>();
//		xmlMap.put("deadperiod", agent.getDeadperiod());
//		xmlMap.put("memweight", agent.getMemweight());
//		xmlMap.put("compresstransfer", agent.getCompresstransfer());
//		xmlMap.put("encoding", agent.getEncoding());
//		xmlMap.put("monitortime", agent.getMonitortime());
//		xmlMap.put("cpuweight", agent.getCpuweight());
//		String xmldata = xmlUtil.parseHashMapToXMLString(xmlMap, false);
//		agent.setXmldata(xmldata);
//		
//		/*
//		 * because lastupdatetime column is auto created value, it can not be reload new value.
//		 * here, we force to give value to lastupdatetime column.
//		 */
//		agent.setLastupdatetime(new Date());
//		
//		JCSAgent new_agent = this.dao.save(agent);
//		new_agent.setCompresstransfer(compresstransfer);
//		
//		/*
//		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
//		 * The fields associated with xml is very suck design!
//		 */
//		setExtraXmlProp(new_agent);
//		
//		return new_agent;
//	}
	
//	public JCSAgent edit(JCSAgent agent) throws IllegalArgumentException, Exception{
//		String agentuid = agent.getAgentuid();
//		if(null == agentuid || agentuid.trim().length() <= 0)
//			throw new IllegalArgumentException("JCSAgent Uid can not be empty!");
//
//		JCSAgent old_agent = this.dao.findOne(agentuid);
//		if(null == old_agent)
//			throw new IllegalArgumentException("JCSAgent Uid does not exist!(" + agentuid + ")");
//		
//		String agentname = agent.getAgentname();
//		if(null == agentname || agentname.trim().length() <= 0)
//			throw new IllegalArgumentException("JCSAgent Name can not be empty!");
//		agent.setAgentname(agentname.toUpperCase());
//		
//		if(this.dao.existByName(agent.getAgentname()) && !old_agent.getAgentname().equalsIgnoreCase(agent.getAgentname()))
//			throw new IllegalArgumentException("Duplicate Agent Name!");
//		
//		String activate = agent.getActivate();
//		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
//			throw new IllegalArgumentException("JCSAgent activate value can only be 1 or 0!");
//		
//		agent.setAgentstatus("0");
//		
//		String compresstransfer = agent.getCompresstransfer();
//		if(null == compresstransfer || (!compresstransfer.equals("1") && !compresstransfer.equals("0")))
//			throw new IllegalArgumentException("JCSAgent compresstransfer value can only be 1 or 0!");
//		
//		try{
//			Integer.valueOf(agent.getCpuweight());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent cpuweight value can only be integer!");
//		}
//		
//		try{
//			Integer.valueOf(agent.getDeadperiod());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent deadperiod value can only be integer!");
//		}
//		
//		if(null == agent.getDescription())
//			agent.setDescription("");
//		
//		if(null == agent.getEncoding())
//			agent.setEncoding("");
//		
//		String host = agent.getHost();
//		if(null == host || host.trim().length()<=0)
//			throw new IllegalArgumentException("JCSAgent Host can not be empty!");
//		
//		if(null == agent.getPort())
//			throw new IllegalArgumentException("JCSAgent Port can not be empty!");
//		
//		if(null == agent.getMaximumjob())
//			throw new IllegalArgumentException("JCSAgent Maximum Job can not be empty!");
//		
//		try{
//			Integer.valueOf(agent.getMemweight());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent memweight value can only be integer!");
//		}
//		
//		try{
//			Integer.valueOf(agent.getMonitortime());
//		}catch(Exception e){
//			throw new IllegalArgumentException("JCSAgent monitortime value can only be integer!");
//		}
//		
//		if(null == agent.getOsname())
//			agent.setOsname("");
//		
//		if(null == agent.getOstype())
//			agent.setOstype("");
//		
//		Map<String, String> xmlMap = new HashMap<String, String>();
//		xmlMap.put("deadperiod", agent.getDeadperiod());
//		xmlMap.put("memweight", agent.getMemweight());
//		xmlMap.put("compresstransfer", agent.getCompresstransfer());
//		xmlMap.put("encoding", agent.getEncoding());
//		xmlMap.put("monitortime", agent.getMonitortime());
//		xmlMap.put("cpuweight", agent.getCpuweight());
//		String xmldata = xmlUtil.parseHashMapToXMLString(xmlMap, false);
//		agent.setXmldata(xmldata);
//		
//		/*
//		 * because lastupdatetime column is auto created value, it can not be reload new value.
//		 * here, we force to give value to lastupdatetime column.
//		 */
//		agent.setLastupdatetime(new Date());
//		
//		JCSAgent new_agent = this.dao.save(agent);
//		new_agent.setCompresstransfer(compresstransfer);
//		
//		/*
//		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
//		 * The fields associated with xml is very suck design!
//		 */
//		setExtraXmlProp(new_agent);
//				
//		return new_agent;
//	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
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
