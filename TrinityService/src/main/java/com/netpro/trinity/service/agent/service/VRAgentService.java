package com.netpro.trinity.service.agent.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		setProfileDataOnly(vrAgents);
		
		if(null == withoutDetail || withoutDetail == false)
			getAgentList(vrAgents);
		
		return vrAgents;
	}
	
	public ResponseEntity<?> getByFilter(Boolean withoutDetail, FilterInfo filter) throws Exception{
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "virtualagentname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<VRAgent> page_agents = this.dao.findByVirtualagentnameLikeIgnoreCase(param, getPagingAndOrdering(paging, ordering));
		setProfileDataOnly(page_agents.getContent());
		
		if(null == withoutDetail || withoutDetail == false)
			getAgentList(page_agents.getContent());
		
		return ResponseEntity.ok(page_agents);
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
			this.permissionClient.deleteByObjectUid(uid);	//刪掉該vragent所有的permission
			
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
			return Sort.by(direct, "virtualagentname");
	}
	
	private void setProfileDataOnly(List<VRAgent> agents) {
		for(VRAgent agent : agents) {
			setProfileDataOnly(agent);
		}
	}
	
	private void setProfileDataOnly(VRAgent agent) {
		agent.setMaximumjob(null);
		agent.setActivate(null);
		agent.setXmldata(null);
	}
	
	@SuppressWarnings("unused")
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
			VRAgentService.LOGGER.error("Exception; reason was:", e);
		}
	}
}
