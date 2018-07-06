package com.netpro.trinity.service.agent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.agent.dao.VRAgentJPADao;
import com.netpro.trinity.service.agent.entity.VRAgent;
import com.netpro.trinity.service.agent.entity.VRAgentList;
import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.dto.Querying;
import com.netpro.trinity.service.job.service.JobService;
import com.netpro.trinity.service.member.service.TrinityuserService;
import com.netpro.trinity.service.objectalias.service.ObjectAliasService;
import com.netpro.trinity.service.permission.dto.AccessRight;
import com.netpro.trinity.service.permission.feign.PermissionClient;
import com.netpro.trinity.service.util.ACUtil;
import com.netpro.trinity.service.util.Constant;

@Service
public class VRAgentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRAgentService.class);
	
	public static final String[] VR_AGENT_FIELD_VALUES = new String[] { "virtualagentname", "activate", "description", "mode"};
	public static final Set<String> VR_AGENT_FIELD_SET = new HashSet<>(Arrays.asList(VR_AGENT_FIELD_VALUES));
	
	@Autowired
	private VRAgentJPADao dao;
	
	@Autowired
	private VRAgentListService listService;
	@Autowired
	private TrinityuserService userService;
	@Autowired
	private JobService jobService;
	@Autowired
	private ObjectAliasService objectAliasService;
	
	@Autowired
	private PermissionClient permissionClient;
		
	public List<VRAgent> getAll(Boolean withoutDetail) throws Exception{
		List<VRAgent> vrAgents = this.dao.findAll();
		if(null == withoutDetail || withoutDetail == false)
			getAgentList(vrAgents);
		return vrAgents;
	}
	
	public VRAgent getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Virtual Agent UID can not be empty!");
		
		VRAgent vrAgent = null;
		try {
			vrAgent = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		
		if(null == vrAgent)
			throw new IllegalArgumentException("Virtual Agent UID does not exist!(" + uid + ")");
		
		if(null == withoutDetail || withoutDetail == false)
			getAgentList(vrAgent);
		
		return vrAgent;
	}
	
	public List<VRAgent> getByName(Boolean withoutDetail, String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Virtual Agent Name can not be empty!");
		
		List<VRAgent> vrAgents = this.dao.findByvirtualagentname(name.toUpperCase());
		
		if(null == withoutDetail || withoutDetail == false)
			getAgentList(vrAgents);
		
		return vrAgents;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(Boolean withoutDetail, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null) {
			List<VRAgent> vrAgents = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
				getAgentList(vrAgents);
			return ResponseEntity.ok(vrAgents);
		}
			
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<VRAgent> vrAgents = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
				getAgentList(vrAgents);
			return ResponseEntity.ok(vrAgents);
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
				Page<VRAgent> page_vragent = this.dao.findAll(pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getAgentList(page_vragent.getContent());
				return ResponseEntity.ok(page_vragent);
			}else if(sort != null) {
				List<VRAgent> vrAgents = this.dao.findAll(sort);
				if(null == withoutDetail || withoutDetail == false)
					getAgentList(vrAgents);
				return ResponseEntity.ok(vrAgents);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<VRAgent> vrAgents = this.dao.findAll(sort);
				if(null == withoutDetail || withoutDetail == false)
					getAgentList(vrAgents);
				return ResponseEntity.ok(vrAgents);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !VR_AGENT_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ VR_AGENT_FIELD_SET.toString());
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
				Page<VRAgent> page_vragent = (Page<VRAgent>) method.invoke(this.dao, queryString, pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getAgentList(page_vragent.getContent());
				return ResponseEntity.ok(page_vragent);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<VRAgent> vragents = (List<VRAgent>) method.invoke(this.dao, queryString, sort);
				if(null == withoutDetail || withoutDetail == false)
					getAgentList(vragents);
				return ResponseEntity.ok(vragents);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<VRAgent> vragents = (List<VRAgent>) method.invoke(this.dao, queryString);
				if(null == withoutDetail || withoutDetail == false)
					getAgentList(vragents);
				return ResponseEntity.ok(vragents);
			}
		}
	}
	
	public VRAgent add(HttpServletRequest request, VRAgent vragent) throws IllegalArgumentException, Exception{
		vragent.setVirtualagentuid(UUID.randomUUID().toString());
		
		String vr_agentname = vragent.getVirtualagentname();
		if(null == vr_agentname || vr_agentname.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Name can not be empty!");
		vragent.setVirtualagentname(vr_agentname.toUpperCase());
		
		if(this.dao.existByName(vragent.getVirtualagentname()))
			throw new IllegalArgumentException("Duplicate Virtual Agent Name!");
		
		String activate = vragent.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Virtual Agent activate value can only be 1 or 0!");
		
		String mode = vragent.getMode();
		if(null == mode || (!mode.equals("1") && !mode.equals("0")))
			throw new IllegalArgumentException("Virtual Agent mode value can only be 1 or 0!");
		
		if(null == vragent.getDescription())
			vragent.setDescription("");
		
		if(null == vragent.getMaximumjob())
			throw new IllegalArgumentException("Virtual Agent Maximum Job value can only be small integer!");
		
		vragent.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		vragent.setLastupdatetime(new Date());
		
		this.dao.save(vragent);
		List<VRAgentList> vrlist = vragent.getAgentlist();
		if(null != vrlist && vrlist.size() > 0) {
			int[] returnValue = this.listService.addBatch(vragent.getVirtualagentuid(), vrlist);
			for(int i=0; i<returnValue.length; i++) {//重設working calendar list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					vrlist.remove(i);
				}
			}
			vragent.setAgentlist(vrlist);
		}
		
		//default permission insert
		this.modifyPermissionByObjectUid(vragent.getVirtualagentuid(), request);
		
		return vragent;
	}
	
	public VRAgent edit(VRAgent vragent) throws IllegalArgumentException, Exception{		
		String vr_agentuid = vragent.getVirtualagentuid();
		if(null == vr_agentuid || vr_agentuid.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Uid can not be empty!");
		
		VRAgent old_vragent = null;
		try {
			old_vragent = this.dao.findById(vr_agentuid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_vragent)
			throw new IllegalArgumentException("Virtual Agent Uid does not exist!(" + vr_agentuid + ")");
		
		String vr_agentname = vragent.getVirtualagentname();
		if(null == vr_agentname || vr_agentname.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Name can not be empty!");
		vragent.setVirtualagentname(vr_agentname.toUpperCase());
		
		if(this.dao.existByName(vragent.getVirtualagentname()) && !old_vragent.getVirtualagentname().equalsIgnoreCase(vragent.getVirtualagentname()))
			throw new IllegalArgumentException("Duplicate Virtual Agent Name!");
		
		String activate = vragent.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Virtual Agent activate value can only be 1 or 0!");
		
		String mode = vragent.getMode();
		if(null == mode || (!mode.equals("1") && !mode.equals("0")))
			throw new IllegalArgumentException("Virtual Agent mode value can only be 1 or 0!");
		
		if(null == vragent.getDescription())
			vragent.setDescription("");
		
		if(null == vragent.getMaximumjob())
			throw new IllegalArgumentException("Virtual Agent Maximum Job can not be empty!");
		
		vragent.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		vragent.setLastupdatetime(new Date());
		
		this.dao.save(vragent);
		
		List<VRAgentList> vrlist = vragent.getAgentlist();
		if(null != vrlist) {	//有傳送VRAgentList過來, 才表示需要異動其下的agent list, 否則只需異動VRAgent即可
			this.listService.deleteByVRAgentUid(vragent.getVirtualagentuid());
			if(vrlist.size() > 0) {	//當傳送過來的agent list之size大於0, 才有做下面動作的必要
				int[] returnValue = this.listService.addBatch(vragent.getVirtualagentuid(), vrlist);
				for(int i=0; i<returnValue.length; i++) {//重設working calendar list, 只有插入成功的會留下來傳回前端
					if(returnValue[i] == 0) {
						vrlist.remove(i);
					}
				}
				vragent.setAgentlist(vrlist);
			}
		}
		
		return vragent;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Uid can not be empty!");
		
		if(jobService.existByFrequencyuid(uid)) {
			throw new IllegalArgumentException("Referenceing by job");
		}else if(objectAliasService.existByObjectuid(uid)) {
			throw new IllegalArgumentException("Referenceing by Object Alias");
		}else {
			this.permissionClient.deleteByObjectUid(uid);	//刪掉該agent所有的permission
			
			this.listService.deleteByVRAgentUid(uid);
			
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
		
		if(ordering.getOrderField() != null && VR_AGENT_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "lastupdatetime");
		
	}
	
	private void getAgentList(List<VRAgent> vrAgents) throws Exception {
		for(VRAgent vrAgent : vrAgents) {
			getAgentList(vrAgent);
		}
	}
	
	private void getAgentList(VRAgent vrAgent) throws Exception {
		vrAgent.setAgentlist(this.listService.getExByVRAgentUid(vrAgent.getVirtualagentuid()));
	}
	
	private void modifyPermissionByObjectUid(String objectUid, HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			String peopleUid = userService.getByID(peopleId).getUseruid();
			if(null == peopleUid || peopleUid.trim().isEmpty() || "trinity".equals(peopleUid.trim()))
				return;
			
			List<AccessRight> accessRights = new ArrayList<AccessRight>();
			AccessRight accessRight = new AccessRight();
			accessRight.setPeopleuid(peopleUid);
			accessRight.setObjectuid(objectUid);
			accessRight.setAdd("1");
			accessRight.setDelete("0");
			accessRight.setEdit("0");
			accessRight.setGrant("0");
			accessRight.setImport_export("0");
			accessRight.setReRun("0");
			accessRight.setRun("0");
			accessRight.setView("0");
			accessRights.add(accessRight);
			
			this.permissionClient.modifyByObjectUid(objectUid, accessRights);
		} catch (Exception e) {
			VRAgentService.LOGGER.error("Exception; reason was:", e);
		}
	}
}
