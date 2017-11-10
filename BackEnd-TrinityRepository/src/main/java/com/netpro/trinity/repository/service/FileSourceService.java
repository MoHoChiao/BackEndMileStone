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
import com.netpro.trinity.repository.jpa.entity.FileSource;
import com.netpro.trinity.repository.jpa.entity.JCSAgent;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.XMLDataUtility;

@Service
public class FileSourceService {
	public static final String[] FS_FIELD_VALUES = new String[] {"filesourcename", "description"};
	public static final Set<String> FS_FIELD_SET = new HashSet<>(Arrays.asList(FS_FIELD_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private FileSourceDao dao;
	
	@Autowired
	private FilesourceRelationService relService;
	
	public List<FileSource> getAll() throws Exception{
		List<FileSource> filesources = this.dao.findAll();
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	public List<FileSource> getAllWithoutInCategory() throws Exception{
		List<FileSource> filesources = this.dao.findByFilesourceuidNotIn(relService.getAll(), new Sort(new Order("filesourcename")));
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	public boolean existByUid(String uid) {
		return this.dao.exists(uid);
	}
	
	public FileSource getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("File Source UID can not be empty!");
		
		FileSource filesource = this.dao.findOne(uid);
		if(filesource == null)
			throw new IllegalArgumentException("File Source UID does not exist!(" + uid + ")");
		setExtraXmlProp(filesource);
		return filesource;
	}
	
	public List<FileSource> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("File Source Name can not be empty!");
		
		List<FileSource> filesources = this.dao.findByfilesourcename(name.toUpperCase());
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	public List<FileSource> getByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("File Source Category UID can not be empty!");
		
		List<FileSource> filesources = this.dao.findByFilesourceuidIn(relService.getByCategoryUid(uid), new Sort(new Order("filesourcename")));
		setExtraXmlProp(filesources);
		return filesources;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		
		if(filter == null) {
			List<FileSource> filesources;
			if(categoryUid == null || categoryUid.isEmpty()) {
				filesources = this.dao.findAll();
			}else {
				filesources = this.dao.findByFilesourceuidIn(relService.getByCategoryUid(categoryUid));
			}
			setExtraXmlProp(filesources);
			return ResponseEntity.ok(filesources);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<FileSource> filesources;
			if(categoryUid == null || categoryUid.isEmpty()) {
				filesources = this.dao.findAll();
			}else {
				filesources = this.dao.findByFilesourceuidIn(relService.getByCategoryUid(categoryUid));
			}
			setExtraXmlProp(filesources);
			return ResponseEntity.ok(filesources);
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
				Page<FileSource> page_filesource;
				if(categoryUid == null || categoryUid.isEmpty()) {
					page_filesource = this.dao.findAll(pageRequest);
				}else {
					page_filesource = this.dao.findByFilesourceuidIn(relService.getByCategoryUid(categoryUid), pageRequest);
				}
				setExtraXmlProp(page_filesource.getContent());
				return ResponseEntity.ok(page_filesource);
			}else if(sort != null) {
				List<FileSource> filesources;
				if(categoryUid == null || categoryUid.isEmpty()) {
					filesources = this.dao.findAll(sort);
				}else {
					filesources = this.dao.findByFilesourceuidIn(relService.getByCategoryUid(categoryUid), sort);
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<FileSource> filesources;
				if(categoryUid == null || categoryUid.isEmpty()) {
					filesources = this.dao.findAll();
				}else {
					filesources = this.dao.findByFilesourceuidIn(relService.getByCategoryUid(categoryUid));
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !FS_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ FS_FIELD_SET.toString());
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
				methodName.append("AndFilesourceuidIn");
			}
			
			Method method = null;
			if(pageRequest != null){
				Page<FileSource> page_filesource;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					page_filesource = (Page<FileSource>) method.invoke(this.dao, queryString, pageRequest);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class, List.class);
					page_filesource = (Page<FileSource>) method.invoke(this.dao, queryString, pageRequest, relService.getByCategoryUid(categoryUid));
				}
				setExtraXmlProp(page_filesource.getContent());
				return ResponseEntity.ok(page_filesource);
			}else if(sort != null) {
				List<FileSource> filesources;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					filesources = (List<FileSource>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, List.class);
					filesources = (List<FileSource>) method.invoke(this.dao, queryString, sort, relService.getByCategoryUid(categoryUid));
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
			}else {
				List<FileSource> filesources;
				if(categoryUid == null || categoryUid.isEmpty()) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					filesources = (List<FileSource>) method.invoke(this.dao, queryString);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, List.class);
					filesources = (List<FileSource>) method.invoke(this.dao, queryString, relService.getByCategoryUid(categoryUid));
				}
				setExtraXmlProp(filesources);
				return ResponseEntity.ok(filesources);
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
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Uid can not be empty!");
		
		this.dao.delete(uid);
		this.relService.deleteByFileSourceUid(uid);
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
	
	private void setExtraXmlProp(List<FileSource> filesources) throws Exception{
		for(FileSource filesource : filesources) {
			setExtraXmlProp(filesource);
		}
	}
	
	private void setExtraXmlProp(FileSource filesource) throws Exception{
		HashMap<String, String> map = xmlUtil.parseXMLDataToHashMap(filesource.getXmldata());
		if(map != null) {
			filesource.setFtppostaction(map.get("ftppostaction"));
			filesource.setCfImpClass(map.get("cfImpClass"));
			filesource.setTxdateformat(map.get("txdateformat"));
			filesource.setRootdir(map.get("rootdir"));
			filesource.setFtpconnectionuid(map.get("ftpconnectionuid"));
			filesource.setPassive(map.get("passive"));
			filesource.setTriggerjobuid(map.get("triggerjobuid"));
			filesource.setChecksumalg(map.get("checksumalg"));
			filesource.setTxdateendpos(map.get("txdateendpos"));
			filesource.setFtpmovedir(map.get("ftpmovedir"));
			filesource.setDatafilecountmode(map.get("datafilecountmode"));
			filesource.setChecksumfe(map.get("checksumfe"));
			filesource.setAppendUid(map.get("appendUid"));
			filesource.setFtpremotedir(map.get("ftpremotedir"));
			filesource.setChecksum(map.get("checksum"));
			filesource.setSftp(map.get("sftp"));
			filesource.setTxdatestartpos(map.get("txdatestartpos"));
			filesource.setFtpbinary(map.get("ftpbinary"));
		}
	}
}
