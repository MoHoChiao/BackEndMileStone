package com.netpro.trinity.service.job.service;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.dto.Querying;
import com.netpro.trinity.service.filesource.entity.FileSource;
import com.netpro.trinity.service.job.dao.JobFlowJPADao;
import com.netpro.trinity.service.job.entity.JobFlow;
import com.netpro.trinity.service.util.Constant;

@Service
public class JobFlowService {
	public static final String[] FLOW_FIELD_VALUES = new String[] {"flowname", "activate", "description"};
	public static final Set<String> FLOW_FIELD_SET = new HashSet<>(Arrays.asList(FLOW_FIELD_VALUES));
	
	@Autowired
	private JobFlowJPADao dao;
	
	public List<JobFlow> getAll() throws Exception{
		List<JobFlow> flows = this.dao.findAll();
		setExtraXmlProp(flows);
		return flows;
	}
	
	public JobFlow getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Flow UID can not be empty!");
		
		JobFlow flow = this.dao.findById(uid).get();
		if(flow == null)
			throw new IllegalArgumentException("Flow UID does not exist!(" + uid + ")");
		setExtraXmlProp(flow);
		return flow;
	}
	
	public List<JobFlow> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Flow Name can not be empty!");
		
		List<JobFlow> flows = this.dao.findByflowname(name.toUpperCase());
		setExtraXmlProp(flows);
		return flows;
	}
	
	public List<JobFlow> getByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Flow Category UID can not be empty!");
		
		List<JobFlow> flows = this.dao.findByCategoryuid(uid, Sort.by("flowname"));
		setExtraXmlProp(flows);
		return flows;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		
		if(filter == null) {
			List<JobFlow> flows;
			if(categoryUid == null || categoryUid.isEmpty()) {
				flows = this.dao.findAll();
			}else {
				flows = this.dao.findByCategoryuid(categoryUid);
			}
			setExtraXmlProp(flows);
			return ResponseEntity.ok(flows);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<JobFlow> flows;
			if(categoryUid == null || categoryUid.isEmpty()) {
				flows = this.dao.findAll();
			}else {
				flows = this.dao.findByCategoryuid(categoryUid);
			}
			setExtraXmlProp(flows);
			return ResponseEntity.ok(flows);
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
				Page<JobFlow> page_flow;
				if(categoryUid == null || categoryUid.isEmpty()) {
					page_flow = this.dao.findAll(pageRequest);
				}else {
					page_flow = this.dao.findByCategoryuid(categoryUid, pageRequest);
				}
				setExtraXmlProp(page_flow.getContent());
				return ResponseEntity.ok(page_flow);
			}else if(sort != null) {
				List<JobFlow> flows;
				if(categoryUid == null || categoryUid.isEmpty()) {
					flows = this.dao.findAll(sort);
				}else {
					flows = this.dao.findByCategoryuid(categoryUid, sort);
				}
				setExtraXmlProp(flows);
				return ResponseEntity.ok(flows);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<JobFlow> flows;
				if(categoryUid == null || categoryUid.isEmpty()) {
					flows = this.dao.findAll();
				}else {
					flows = this.dao.findByCategoryuid(categoryUid);
				}
				setExtraXmlProp(flows);
				return ResponseEntity.ok(flows);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !FLOW_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ FLOW_FIELD_SET.toString());
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
				Page<JobFlow> page_flow;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					page_flow = (Page<JobFlow>) method.invoke(this.dao, queryString, pageRequest);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class, String.class);
					page_flow = (Page<JobFlow>) method.invoke(this.dao, queryString, pageRequest, categoryUid);
				}
				setExtraXmlProp(page_flow.getContent());
				return ResponseEntity.ok(page_flow);
			}else if(sort != null) {
				List<JobFlow> flows;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					flows = (List<JobFlow>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, String.class);
					flows = (List<JobFlow>) method.invoke(this.dao, queryString, sort, categoryUid);
				}
				setExtraXmlProp(flows);
				return ResponseEntity.ok(flows);
			}else {
				List<JobFlow> flows;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					flows = (List<JobFlow>) method.invoke(this.dao, queryString);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, String.class);
					flows = (List<JobFlow>) method.invoke(this.dao, queryString, categoryUid);
				}
				setExtraXmlProp(flows);
				return ResponseEntity.ok(flows);
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
	
	public boolean existByUid(String uid) throws IllegalArgumentException, Exception {
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Flow Uid can not be empty!");
		
		return this.dao.existsById(uid);
	}
	
	public boolean existByName(String flowname) throws IllegalArgumentException, Exception {
		if(null == flowname || flowname.trim().length() <= 0)
			throw new IllegalArgumentException("Flow Name can not be empty!");
		
		return this.dao.existByName(flowname);
	}
	
	public boolean existByFrequencyuid(String frequencyuid) throws IllegalArgumentException, Exception {
		if(null == frequencyuid || frequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		return this.dao.existByFrequencyuid(frequencyuid);
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
	
	private void setExtraXmlProp(List<JobFlow> flows) throws Exception{
//		for(JobFlow flow : flows) {
//			setExtraXmlProp(flow);
//		}
	}
	
	private void setExtraXmlProp(JobFlow flow) throws Exception{
//		HashMap<String, String> map = xmlUtil.parseXMLDataToHashMap(job.getXmldata());
//		if(map != null) {
//			job.setRerunfromfs(Boolean.parseBoolean(map.get("rerunfromfs")));
//			job.setApplycompletedtask(Boolean.parseBoolean(map.get("applycompletedtask")));
//			job.setWaitingtime(map.get("waitingtime"));
//			job.setDontsavehistory(Boolean.parseBoolean(map.get("dontsavehistory")));
//			job.setUsestepcondi(Boolean.parseBoolean(map.get("usestepcondi")));
//			job.setSkipmissingtask(Boolean.parseBoolean(map.get("skipmissingtask")));
//			
//			job.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
//		}
	}
}
