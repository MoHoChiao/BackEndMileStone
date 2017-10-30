package com.netpro.trinity.repository.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.VRAgentListDao;
import com.netpro.trinity.repository.entity.VRAgentList;

@Service
public class VRAgentListService {
	
	@Autowired
	private VRAgentListDao dao;
	
	public List<VRAgentList> getExByVRAgentUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Virtual Agent UID can not be empty!");
				
		return this.dao.findExByVRAgentUid(uid);
	}
}
