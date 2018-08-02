package com.netpro.trinity.service.agent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.agent.dao.JCSAgentJPADao;
import com.netpro.trinity.service.agent.entity.JCSAgent;
import com.netpro.trinity.service.configuration.service.MonitorconfigService;
import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.feign.util.TrinityBadResponseWrapper;
import com.netpro.trinity.service.job.service.JobService;
import com.netpro.trinity.service.member.service.TrinityuserService;
import com.netpro.trinity.service.objectalias.service.ObjectAliasService;
import com.netpro.trinity.service.permission.dto.AccessRight;
import com.netpro.trinity.service.permission.feign.PermissionClient;
import com.netpro.trinity.service.util.ACUtil;
import com.netpro.trinity.service.util.Constant;
import com.netpro.trinity.service.util.XMLDataUtility;

@Service
public class JCSAgentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JCSAgentService.class);
	
	public static final String[] AGENT_FIELD_VALUES = new String[] { "agentname", "activate", "host", "port", "description" };
	public static final Set<String> AGENT_FIELD_SET = new HashSet<>(Arrays.asList(AGENT_FIELD_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private JCSAgentJPADao dao;
	
	@Autowired
	private TrinityuserService userService;
	@Autowired
	private JobService jobService;
	@Autowired
	private ObjectAliasService objectAliasService;
	@Autowired
	private VRAgentListService vAgentListService;
	@Autowired
	private MonitorconfigService monitorService;
	
	@Autowired
	private PermissionClient permissionClient;
	
	public List<JCSAgent> getAll(HttpServletRequest request) throws Exception{
		List<JCSAgent> agents = this.dao.findAll();
		setProfileDataOnly(agents);
		return agents;
	}
	
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws Exception{
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "agentname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<JCSAgent> page_agents = this.dao.findByAgentnameLikeIgnoreCaseOrHostLikeIgnoreCase(param, param, getPagingAndOrdering(paging, ordering));
		setProfileDataOnly(page_agents.getContent());
		return ResponseEntity.ok(page_agents);
	}
	
	public JCSAgent getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Agent UID can not be empty!");
		
		JCSAgent agent = null;
		try {
			agent = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == agent)
			throw new IllegalArgumentException("Agent UID does not exist!(" + uid + ")");
		setExtraXmlProp(agent);
		return agent;
	}
	
	public List<String> getAllAgentNames() throws Exception{
		List<String> names = this.dao.findAllAgentNames();
		return names;
	}
	
	public List<String> getAllAgentUids() throws Exception{
		List<String> uids = this.dao.findAllAgentUids();
		return uids;
	}
	
	public JCSAgent add(HttpServletRequest request, JCSAgent agent) throws IllegalArgumentException, TrinityBadResponseWrapper, Exception{
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
		
		//default permission insert
		this.modifyPermissionByObjectUid(new_agent.getAgentuid(), request);
		
		return new_agent;
	}
	
	public JCSAgent edit(JCSAgent agent) throws IllegalArgumentException, Exception{
		String agentuid = agent.getAgentuid();
		if(null == agentuid || agentuid.trim().length() <= 0)
			throw new IllegalArgumentException("JCSAgent Uid can not be empty!");

		JCSAgent old_agent = null;
		try {
			old_agent = this.dao.findById(agentuid).get();
		}catch(NoSuchElementException e) {}
		
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
	
	public void deleteByUid(String uid) throws IllegalArgumentException, TrinityBadResponseWrapper, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("JCSAgent Uid can not be empty!");
		
		if(jobService.existByFrequencyuid(uid)) {
			throw new IllegalArgumentException("Referenceing by job");
		}else if(objectAliasService.existByObjectuid(uid)) {
			throw new IllegalArgumentException("Referenceing by Object Alias");
		}else if(vAgentListService.existByAgentuid(uid)) {
			throw new IllegalArgumentException("Referenceing by Virtual Agent");
		}else {
			try {
				this.monitorService.deleteByUid(uid);
			}catch(EmptyResultDataAccessException e) {}	//可能不存在, 所以不影響刪除agent
			
			this.permissionClient.deleteByObjectUid(uid);	//刪掉該agent所有的permission
			
			this.dao.deleteById(uid);
		}
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}
	
	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception{
		if(paging.getNumber() == null)
			paging.setNumber(0);
		
		if(paging.getSize() == null)
			paging.setSize(10);
		
		if(null != ordering) {
			return PageRequest.of(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return PageRequest.of(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromString("ASC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromString(ordering.getOrderType());
		
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "agentname");
	}
	
	private void setProfileDataOnly(List<JCSAgent> agents) {
		for(JCSAgent agent : agents) {
			setProfileDataOnly(agent);
		}
	}
	
	private void setProfileDataOnly(JCSAgent agent) {
		agent.setPort(null);
		agent.setMaximumjob(null);
		agent.setActivate(null);
		agent.setOstype(null);
		agent.setOsname(null);
		agent.setAgentstatus(null);
		agent.setXmldata(null);
	}
	
	@SuppressWarnings("unused")
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
			
			agent.setXmldata(null);	//不再需要xml欄位的資料, 已經parsing
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
	
	private void modifyPermissionByObjectUid(String objectUid, HttpServletRequest request) throws TrinityBadResponseWrapper, Exception {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			String peopleUid = userService.getByID(peopleId).getUseruid();
			if(null == peopleUid || peopleUid.trim().isEmpty() || "trinity".equals(peopleUid.trim()))
				return;
			
			List<AccessRight> accessRights = new ArrayList<AccessRight>();
			AccessRight accessRight = new AccessRight();
			accessRight.setPeopleuid(peopleUid);
			accessRight.setObjectuid(objectUid);
			accessRight.setView("1");
			accessRight.setAdd("1");
			accessRight.setEdit("1");
			accessRight.setDelete("1");
			accessRight.setGrant("1");
			accessRight.setImport_export("0");
			accessRight.setReRun("0");
			accessRight.setRun("0");
			accessRights.add(accessRight);
			
			this.permissionClient.modifyByObjectUid(objectUid, accessRights);
		} catch (Exception e) {
			JCSAgentService.LOGGER.error("Exception; reason was:", e);
		}
	}
}
