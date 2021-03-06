package com.netpro.trinity.repository.agent.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import com.netpro.trinity.repository.agent.dao.JCSAgentJPADao;
import com.netpro.trinity.repository.agent.entity.JCSAgent;
import com.netpro.trinity.repository.dto.inquire.FilterInfo;
import com.netpro.trinity.repository.dto.inquire.Ordering;
import com.netpro.trinity.repository.dto.inquire.Paging;
import com.netpro.trinity.repository.dto.inquire.Querying;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.XMLDataUtility;

@Service
public class JCSAgentService {
	public static final String[] AGENT_FIELD_VALUES = new String[] { "agentname", "activate", "host", "port", "description" };
	public static final Set<String> AGENT_FIELD_SET = new HashSet<>(Arrays.asList(AGENT_FIELD_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private JCSAgentJPADao dao;
	
	public List<JCSAgent> getAll() throws Exception{
		List<JCSAgent> agents = this.dao.findAll();
		setExtraXmlProp(agents);
		return agents;
	}
	
	public JCSAgent getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Agent UID can not be empty!");
		
		JCSAgent agent = this.dao.findOne(uid);
		if(null == agent)
			throw new IllegalArgumentException("Agent UID does not exist!(" + uid + ")");
		setExtraXmlProp(agent);
		return agent;
	}
	
	public List<JCSAgent> getByName(String name) throws IllegalArgumentException, Exception{
		if(null == name || name.isEmpty())
			throw new IllegalArgumentException("Agent Name can not be empty!");
		
		List<JCSAgent> agents = this.dao.findByagentname(name.toUpperCase());
		setExtraXmlProp(agents);
		return agents;
	}
	
	public List<String> getAllAgentNames() throws Exception{
		List<String> names = this.dao.findAllAgentNames();
		return names;
	}
	
	public List<String> getAllAgentUids() throws Exception{
		List<String> uids = this.dao.findAllAgentUids();
		return uids;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<JCSAgent> agents = this.dao.findAll();
			setExtraXmlProp(agents);
			return ResponseEntity.ok(agents);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<JCSAgent> agents = this.dao.findAll();
			setExtraXmlProp(agents);
			return ResponseEntity.ok(agents);
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
				Page<JCSAgent> page_agent = this.dao.findAll(pageRequest);
				setExtraXmlProp(page_agent.getContent());
				return ResponseEntity.ok(page_agent);
			}else if(sort != null) {
				List<JCSAgent> agents = this.dao.findAll(sort);
				setExtraXmlProp(agents);
				return ResponseEntity.ok(agents);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<JCSAgent> agents = this.dao.findAll();
				setExtraXmlProp(agents);
				return ResponseEntity.ok(agents);
			}
		}else {
			if(null == querying.getQueryType() || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(null == querying.getQueryField() || !AGENT_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ AGENT_FIELD_SET.toString());
			if(null == querying.getIgnoreCase())
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
					Page<JCSAgent> page_agent = (Page<JCSAgent>) method.invoke(this.dao, Integer.valueOf(queryString), pageRequest);
					setExtraXmlProp(page_agent.getContent());
					return ResponseEntity.ok(page_agent);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					Page<JCSAgent> page_agent = (Page<JCSAgent>) method.invoke(this.dao, queryString, pageRequest);
					setExtraXmlProp(page_agent.getContent());
					return ResponseEntity.ok(page_agent);
				}
			}else if(sort != null) {
				if(queryField.equals("port")) { //Integer Field must be assigned Integer class
					method = this.dao.getClass().getMethod(methodName.toString(), Integer.class, Sort.class);
					List<JCSAgent> agents = (List<JCSAgent>) method.invoke(this.dao, Integer.valueOf(queryString), sort);
					setExtraXmlProp(agents);
					return ResponseEntity.ok(agents);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					List<JCSAgent> agents = (List<JCSAgent>) method.invoke(this.dao, queryString, sort);
					setExtraXmlProp(agents);
					return ResponseEntity.ok(agents);
				}
			}else {
				if(queryField.equals("port")) { //Integer Field must be assigned Integer class
					method = this.dao.getClass().getMethod(methodName.toString(), Integer.class);
					List<JCSAgent> agents = (List<JCSAgent>) method.invoke(this.dao, Integer.valueOf(queryString));
					setExtraXmlProp(agents);
					return ResponseEntity.ok(agents);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					List<JCSAgent> agents = (List<JCSAgent>) method.invoke(this.dao, queryString);
					setExtraXmlProp(agents);
					return ResponseEntity.ok(agents);
				}
			}
		}
	}
	
	public JCSAgent add(JCSAgent agent) throws IllegalArgumentException, Exception{
		agent.setAgentuid(UUID.randomUUID().toString());
		
		String agentname = agent.getAgentname();
		if(null == agentname || agentname.trim().length() <= 0)
			throw new IllegalArgumentException("JCSAgent Name can not be empty!");
		agent.setAgentname(agentname.toUpperCase());
		
		if(this.dao.existByName(agent.getAgentname()))
			throw new IllegalArgumentException("Duplicate Agent Name!");
		
		String activate = agent.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("JCSAgent activate value can only be 1 or 0!");
		
		agent.setAgentstatus("0");
		
		String compresstransfer = agent.getCompresstransfer();
		if(null == compresstransfer || (!compresstransfer.equals("1") && !compresstransfer.equals("0")))
			throw new IllegalArgumentException("JCSAgent compresstransfer value can only be 1 or 0!");
		
		try{
			Integer.valueOf(agent.getCpuweight());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent cpuweight value can only be integer!");
		}
		
		try{
			Integer.valueOf(agent.getDeadperiod());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent deadperiod value can only be integer!");
		}
		
		if(null == agent.getDescription())
			agent.setDescription("");
		
		if(null == agent.getEncoding())
			agent.setEncoding("");
		
		String host = agent.getHost();
		if(null == host || host.trim().length()<=0)
			throw new IllegalArgumentException("JCSAgent Host can not be empty!");
		
		if(null == agent.getPort())
			throw new IllegalArgumentException("JCSAgent Port value can only be small integer!");
		
		if(null == agent.getMaximumjob())
			throw new IllegalArgumentException("JCSAgent Maximum Job value can only be small integer!");
		
		try{
			Integer.valueOf(agent.getMemweight());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent memweight value can only be integer!");
		}
		
		try{
			Integer.valueOf(agent.getMonitortime());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent monitortime value can only be integer!");
		}
		
		if(null == agent.getOsname())
			agent.setOsname("");
		
		if(null == agent.getOstype())
			agent.setOstype("");
		
		setExtraXmlPropToString(agent);
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		agent.setLastupdatetime(new Date());
		
		JCSAgent new_agent = this.dao.save(agent);
		new_agent.setCompresstransfer(compresstransfer);
		
		/*
		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
		 * The fields associated with xml is very suck design!
		 */
		setExtraXmlProp(new_agent);
		
		return new_agent;
	}
	
	public JCSAgent edit(JCSAgent agent) throws IllegalArgumentException, Exception{
		String agentuid = agent.getAgentuid();
		if(null == agentuid || agentuid.trim().length() <= 0)
			throw new IllegalArgumentException("JCSAgent Uid can not be empty!");

		JCSAgent old_agent = this.dao.findOne(agentuid);
		if(null == old_agent)
			throw new IllegalArgumentException("JCSAgent Uid does not exist!(" + agentuid + ")");
		
		String agentname = agent.getAgentname();
		if(null == agentname || agentname.trim().length() <= 0)
			throw new IllegalArgumentException("JCSAgent Name can not be empty!");
		agent.setAgentname(agentname.toUpperCase());
		
		if(this.dao.existByName(agent.getAgentname()) && !old_agent.getAgentname().equalsIgnoreCase(agent.getAgentname()))
			throw new IllegalArgumentException("Duplicate Agent Name!");
		
		String activate = agent.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("JCSAgent activate value can only be 1 or 0!");
		
		agent.setAgentstatus("0");
		
		String compresstransfer = agent.getCompresstransfer();
		if(null == compresstransfer || (!compresstransfer.equals("1") && !compresstransfer.equals("0")))
			throw new IllegalArgumentException("JCSAgent compresstransfer value can only be 1 or 0!");
		
		try{
			Integer.valueOf(agent.getCpuweight());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent cpuweight value can only be integer!");
		}
		
		try{
			Integer.valueOf(agent.getDeadperiod());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent deadperiod value can only be integer!");
		}
		
		if(null == agent.getDescription())
			agent.setDescription("");
		
		if(null == agent.getEncoding())
			agent.setEncoding("");
		
		String host = agent.getHost();
		if(null == host || host.trim().length()<=0)
			throw new IllegalArgumentException("JCSAgent Host can not be empty!");
		
		if(null == agent.getPort())
			throw new IllegalArgumentException("JCSAgent Port can not be empty!");
		
		if(null == agent.getMaximumjob())
			throw new IllegalArgumentException("JCSAgent Maximum Job can not be empty!");
		
		try{
			Integer.valueOf(agent.getMemweight());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent memweight value can only be integer!");
		}
		
		try{
			Integer.valueOf(agent.getMonitortime());
		}catch(Exception e){
			throw new IllegalArgumentException("JCSAgent monitortime value can only be integer!");
		}
		
		if(null == agent.getOsname())
			agent.setOsname("");
		
		if(null == agent.getOstype())
			agent.setOstype("");
		
		setExtraXmlPropToString(agent);
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		agent.setLastupdatetime(new Date());
		
		JCSAgent new_agent = this.dao.save(agent);
		new_agent.setCompresstransfer(compresstransfer);
		
		/*
		 * Because All fields associated with xml are defined by @Transient, it can not be reload new value.
		 * The fields associated with xml is very suck design!
		 */
		setExtraXmlProp(new_agent);
				
		return new_agent;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("JCSAgent Uid can not be empty!");
		
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
	
	private void setExtraXmlProp(List<JCSAgent> agents) throws Exception{
		for(JCSAgent agent : agents) {
			setExtraXmlProp(agent);
		}
	}
	
	private void setExtraXmlProp(JCSAgent agent) throws Exception{
		HashMap<String, String> map = xmlUtil.parseXMLDataToHashMap(agent.getXmldata());
		if(map != null) {
			agent.setDeadperiod(map.get("deadperiod"));
			agent.setMemweight(map.get("memweight"));
			agent.setCompresstransfer(map.get("compresstransfer"));
			agent.setEncoding(map.get("encoding"));
			agent.setMonitortime(map.get("monitortime"));
			agent.setCpuweight(map.get("cpuweight"));
			
			agent.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
		}
	}
	
	private void setExtraXmlPropToString(JCSAgent agent) throws Exception{
		Map<String, String> xmlMap = new HashMap<String, String>();
		xmlMap.put("deadperiod", agent.getDeadperiod());
		xmlMap.put("memweight", agent.getMemweight());
		xmlMap.put("compresstransfer", agent.getCompresstransfer());
		xmlMap.put("encoding", agent.getEncoding());
		xmlMap.put("monitortime", agent.getMonitortime());
		xmlMap.put("cpuweight", agent.getCpuweight());
		String xmldata = xmlUtil.parseHashMapToXMLString(xmlMap, false);
		agent.setXmldata(xmldata);
	}
}
