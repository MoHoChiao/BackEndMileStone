package com.netpro.trinity.service.agent.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.agent.dao.VRAgentListJDBCDao;
import com.netpro.trinity.service.agent.entity.JCSAgent;
import com.netpro.trinity.service.agent.entity.VRAgentList;

@Service
public class VRAgentListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRAgentListService.class);
	
	@Autowired
	private VRAgentListJDBCDao dao;
	
	@Autowired
	private JCSAgentService agentService;
	@Autowired
	private VRAgentService vrAgentService;
	
	public List<VRAgentList> getExByVRAgentUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Virtual Agent UID can not be empty!");
				
		return this.dao.findExByVRAgentUid(uid);
	}
	
	public VRAgentList add(VRAgentList list) throws IllegalArgumentException, Exception{
		String vragentuid = list.getVirtualagentuid();
		if(null == vragentuid)
			throw new IllegalArgumentException("Virtual Agent UID can not be empty!");
		
		if(!this.vrAgentService.existByUid(vragentuid))
			throw new IllegalArgumentException("Virtual Agent UID does not exist!(" + vragentuid + ")");
		
		String agentuid = list.getAgentuid();
		if(null == agentuid)
			throw new IllegalArgumentException("Agent UID can not be empty!");
		
		JCSAgent agent = agentService.getByUid(agentuid);
		list.setAgentname(agent.getAgentname());
		
		Integer seq = list.getSeq();
		if(null == seq)
			throw new IllegalArgumentException("Seq Field must be Integer!");
		if(this.dao.existByVRAgentUidAndSeq(vragentuid, seq))
			throw new IllegalArgumentException("Duplicate Seq Field!");
		
		String vactivate = list.getActivate();
		if(null == vactivate || (!vactivate.equals("1") && !vactivate.equals("0")))
			vactivate = "1";

		String vdescription = list.getDescription();
		if(null == vdescription)
			vdescription = "";
		
		if(this.dao.save(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Add Virtual Agent List Fail!");
	}
	
	public List<VRAgentList> add(String vrAgentUid, List<VRAgentList> lists) throws IllegalArgumentException, Exception{
		List<VRAgentList> new_lists = new ArrayList<VRAgentList>();
		
		if(null == lists)
			return new_lists;
		
		for(VRAgentList list: lists) {
			try {
				list.setVirtualagentuid(vrAgentUid);
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				VRAgentListService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(String vrAgentUid, List<VRAgentList> lists) throws IllegalArgumentException, Exception{
		if(null == vrAgentUid)
			throw new IllegalArgumentException("Virtual Agent UID can not be empty!");
		
		if(!this.vrAgentService.existByUid(vrAgentUid))
			throw new IllegalArgumentException("Virtual Agent UID does not exist!(" + vrAgentUid + ")");
		
		return this.dao.saveBatch(vrAgentUid, lists);
	}
	
	public void deleteByVRAgentUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Uid can not be empty!");
		
		this.dao.deleteByVRAgentUid(uid);
	}
}
