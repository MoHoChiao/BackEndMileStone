package com.netpro.trinity.repository.service;

import java.util.ArrayList;
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

import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.jpa.dao.FileSourceDao;
import com.netpro.trinity.repository.jpa.dao.JCSAgentDao;
import com.netpro.trinity.repository.jpa.dao.JobDao;
import com.netpro.trinity.repository.jpa.entity.FileSource;
import com.netpro.trinity.repository.jpa.entity.JCSAgent;
import com.netpro.trinity.repository.jpa.entity.Job;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.XMLDataUtility;

@Service
public class JobService {
	public static final String[] JOB_FIELD_VALUES = new String[] {"jobname", "activate", "description"};
	public static final Set<String> JOB_FIELD_SET = new HashSet<>(Arrays.asList(JOB_FIELD_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private JobDao dao;
	
	public List<Job> getAll() throws Exception{
		List<Job> jobs = this.dao.findAll();
		setExtraXmlProp(jobs);
		return jobs;
	}
	
	public boolean existByUid(String uid) {
		return this.dao.exists(uid);
	}
	
	public Job getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Job UID can not be empty!");
		
		Job job = this.dao.findOne(uid);
		if(job == null)
			throw new IllegalArgumentException("Job UID does not exist!(" + uid + ")");
		setExtraXmlProp(job);
		return job;
	}
	
	public List<Job> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Job Name can not be empty!");
		
		List<Job> jobs = this.dao.findByjobname(name.toUpperCase());
		setExtraXmlProp(jobs);
		return jobs;
	}
	
	public List<Job> getByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Job Category UID can not be empty!");
		
		List<Job> jobs = this.dao.findByCategoryuid(uid, new Sort(new Order("jobname")));
		setExtraXmlProp(jobs);
		return jobs;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		
		if(filter == null) {
			List<Job> jobs;
			if(categoryUid == null || categoryUid.isEmpty()) {
				jobs = this.dao.findAll();
			}else {
				jobs = this.dao.findByCategoryuid(categoryUid);
			}
			setExtraXmlProp(jobs);
			return ResponseEntity.ok(jobs);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<Job> jobs;
			if(categoryUid == null || categoryUid.isEmpty()) {
				jobs = this.dao.findAll();
			}else {
				jobs = this.dao.findByCategoryuid(categoryUid);
			}
			setExtraXmlProp(jobs);
			return ResponseEntity.ok(jobs);
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
				Page<Job> page_job;
				if(categoryUid == null || categoryUid.isEmpty()) {
					page_job = this.dao.findAll(pageRequest);
				}else {
					page_job = this.dao.findByCategoryuid(categoryUid, pageRequest);
				}
				setExtraXmlProp(page_job.getContent());
				return ResponseEntity.ok(page_job);
			}else if(sort != null) {
				List<Job> jobs;
				if(categoryUid == null || categoryUid.isEmpty()) {
					jobs = this.dao.findAll(sort);
				}else {
					jobs = this.dao.findByCategoryuid(categoryUid, sort);
				}
				setExtraXmlProp(jobs);
				return ResponseEntity.ok(jobs);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Job> jobs;
				if(categoryUid == null || categoryUid.isEmpty()) {
					jobs = this.dao.findAll();
				}else {
					jobs = this.dao.findByCategoryuid(categoryUid);
				}
				setExtraXmlProp(jobs);
				return ResponseEntity.ok(jobs);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !JOB_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ JOB_FIELD_SET.toString());
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
			if(categoryUid != null && !categoryUid.isEmpty()) {
				methodName.append("AndCategoryuid");
			}
			
			Method method = null;
			if(pageRequest != null){
				Page<Job> page_job;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					page_job = (Page<Job>) method.invoke(this.dao, queryString, pageRequest);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class, String.class);
					page_job = (Page<Job>) method.invoke(this.dao, queryString, pageRequest, categoryUid);
				}
				setExtraXmlProp(page_job.getContent());
				return ResponseEntity.ok(page_job);
			}else if(sort != null) {
				List<Job> jobs;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					jobs = (List<Job>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, String.class);
					jobs = (List<Job>) method.invoke(this.dao, queryString, sort, categoryUid);
				}
				setExtraXmlProp(jobs);
				return ResponseEntity.ok(jobs);
			}else {
				List<Job> jobs;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					jobs = (List<Job>) method.invoke(this.dao, queryString);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, String.class);
					jobs = (List<Job>) method.invoke(this.dao, queryString, categoryUid);
				}
				setExtraXmlProp(jobs);
				return ResponseEntity.ok(jobs);
			}
		}
	}
	
	public FileSource add(FileSource filesource) throws IllegalArgumentException, Exception{
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
		
		return filesource;
	}
	
	public FileSource edit(FileSource filesource) throws IllegalArgumentException, Exception{
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
				
		return filesource;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
//		if(null == uid || uid.trim().length() <= 0)
//			throw new IllegalArgumentException("Job Uid can not be empty!");
//		
//		this.dao.delete(uid);
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
	
	private void setExtraXmlProp(List<Job> jobs) throws Exception{
		for(Job job : jobs) {
			setExtraXmlProp(job);
		}
	}
	
	private void setExtraXmlProp(Job job) throws Exception{
		HashMap<String, String> map = xmlUtil.parseXMLDataToHashMap(job.getXmldata());
		if(map != null) {
			job.setRerunfromfs(Boolean.parseBoolean(map.get("rerunfromfs")));
			job.setApplycompletedtask(Boolean.parseBoolean(map.get("applycompletedtask")));
			job.setWaitingtime(map.get("waitingtime"));
			job.setDontsavehistory(Boolean.parseBoolean(map.get("dontsavehistory")));
			job.setUsestepcondi(Boolean.parseBoolean(map.get("usestepcondi")));
			job.setSkipmissingtask(Boolean.parseBoolean(map.get("skipmissingtask")));
		}
	}
}
