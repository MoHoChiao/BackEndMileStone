package com.netpro.trinity.resource.admin.job.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.dto.Ordering;
import com.netpro.trinity.resource.admin.dto.Paging;
import com.netpro.trinity.resource.admin.filesource.entity.FileSource;
import com.netpro.trinity.resource.admin.job.dao.BusentityCategoryJDBCDao;
import com.netpro.trinity.resource.admin.job.dao.JobJPADao;
import com.netpro.trinity.resource.admin.job.entity.Job;
import com.netpro.trinity.resource.admin.job.entity.JobFullPath;
import com.netpro.trinity.resource.admin.util.Constant;
import com.netpro.trinity.resource.admin.util.XMLDataUtility;

@Service
public class JobService {
	public static final String[] JOB_FIELD_VALUES = new String[] {"jobname", "activate", "description"};
	public static final Set<String> JOB_FIELD_SET = new HashSet<>(Arrays.asList(JOB_FIELD_VALUES));
	
	@Autowired
	private XMLDataUtility xmlUtil;
	
	@Autowired
	private JobJPADao dao;
	
	@Autowired
	private BusentityCategoryService entityCategoryService;
	
	public List<Job> getAll() throws Exception{
		List<Job> jobs = this.dao.findAll();
		setExtraXmlProp(jobs);
		return jobs;
	}
	
	public Job getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Job UID can not be empty!");
		
		Job job = null;
		try {
			job = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null ==job)
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
		
		List<Job> jobs = this.dao.findByCategoryuid(uid, Sort.by("jobname"));
		setExtraXmlProp(jobs);
		return jobs;
	}
	
	public JobFullPath getJobFullPathByUid(String uid) throws IllegalArgumentException, Exception{
		JobFullPath path = new JobFullPath();
		path.setJobuid("");
		path.setJobname("");
		path.setBusentityuid("");
		path.setBusentityname("");
		path.setCategoryuid("");
		path.setCategoryname("");
		
		if(uid == null || uid.isEmpty())
			return path;
		
		Job job = null;
		try {
			job = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		
		if(null == job)
			return path;
		
		String jobUid = job.getJobuid();
		String jobName = job.getJobname();
		String categoryUid = job.getCategoryuid();
		
		if(null == categoryUid || categoryUid.isEmpty())
			return path;
		
		List<JobFullPath> paths = entityCategoryService.getViewEntityCategoryByCategoryUid(categoryUid);
		if(paths.size() > 0) {
			path = paths.get(0);
			path.setJobuid(jobUid);
			path.setJobname(jobName);
		}
		
		return path;
	}
	
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws Exception {
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "jobname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<Job> page_jobs;
		if (categoryUid == null) {
			page_jobs = this.dao.findByjobnameLikeIgnoreCase(param, getPagingAndOrdering(paging, ordering));
			Map<String, JobFullPath> map = entityCategoryService.getAllViewEntityCategory();
			
			for (Job job : page_jobs.getContent()) {
				JobFullPath jfp = map.get(job.getCategoryuid());
				job.setCategoryname(jfp.getCategoryname());
				job.setEntityname(jfp.getBusentityname());
			}
		} else {
			page_jobs = this.dao.findByCategoryuid(categoryUid, getPagingAndOrdering(paging, ordering));
		}
		
		return ResponseEntity.ok(page_jobs);
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
			throw new IllegalArgumentException("Job Uid can not be empty!");
		
		return this.dao.existsById(uid);
	}
	
	public boolean existByName(String jobname) throws IllegalArgumentException, Exception {
		if(null == jobname || jobname.trim().length() <= 0)
			throw new IllegalArgumentException("Job Name can not be empty!");
		
		return this.dao.existByName(jobname);
	}
	
	public boolean existByFilesourceuid(String filesourceuid) throws IllegalArgumentException, Exception {
		if(null == filesourceuid || filesourceuid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Uid can not be empty!");
		
		return this.dao.existByFilesourceuid(filesourceuid);
	}
	
	public boolean existByFrequencyuid(String frequencyuid) throws IllegalArgumentException, Exception {
		if(null == frequencyuid || frequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		return this.dao.existByFrequencyuid(frequencyuid);
	}
	
	public boolean existByDomainuid(String domainuid) throws IllegalArgumentException, Exception {
		if(null == domainuid || domainuid.trim().isEmpty())
			throw new IllegalArgumentException("Domain Uid can not be empty!");
		
		return this.dao.existByDomainuid(domainuid);
	}
	
	public boolean existByAgentuid(String filesourceuid) throws IllegalArgumentException, Exception {
		if(null == filesourceuid || filesourceuid.trim().length() <= 0)
			throw new IllegalArgumentException("File Source Uid can not be empty!");
		
		return this.dao.existByFilesourceuid(filesourceuid);
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
			
			job.setXmldata("");	//不再需要xml欄位的資料, 已經parsing
		}
	}
}
