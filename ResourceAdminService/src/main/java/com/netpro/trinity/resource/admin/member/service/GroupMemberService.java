package com.netpro.trinity.resource.admin.member.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.member.dao.GroupMemberJDBCDao;
import com.netpro.trinity.resource.admin.member.entity.GroupMember;

@Service
public class GroupMemberService {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupMemberService.class);
	
	@Autowired
	private GroupMemberJDBCDao dao;
	
	@Autowired
	private UsergroupService groupService;
	@Autowired
	private TrinityuserService userService;
	
	public List<String> getAllUserUids() throws Exception{
		return this.dao.findAllUserUids();
	}
	
	public List<String> getUserUidsByGroupUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("User Group UID can not be empty!");
				
		return this.dao.findUserUidsByGroupUid(uid);
	}
	
	public List<GroupMember> getUserFullNameByGroupUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("User Group UID can not be empty!");
				
		return this.dao.findUserFullNameByGroupUid(uid);
	}
	
	public GroupMember add(GroupMember gm) throws IllegalArgumentException, Exception{
		String groupuid = gm.getGroupuid();
		if(null == groupuid || groupuid.trim().isEmpty())
			throw new IllegalArgumentException("User Group UID can not be empty!");
		
		if(!this.groupService.existByUid(groupuid))
			throw new IllegalArgumentException("User Group UID does not exist!(" + groupuid + ")");
		
		String useruid = gm.getUseruid();
		if(null == useruid || useruid.trim().isEmpty())
			throw new IllegalArgumentException("User UID can not be empty!");
	
		if(!this.userService.existByUid(useruid))
			throw new IllegalArgumentException("User UID does not exist!(" + useruid + ")");
		
		if(this.dao.save(gm) > 0)
			return gm;
		else
			throw new IllegalArgumentException("Add Group Member Fail!");
	}
	
	public List<GroupMember> add(String groupUid, List<GroupMember> lists) throws IllegalArgumentException, Exception{
		List<GroupMember> new_lists = new ArrayList<GroupMember>();
		
		if(null == lists)
			return new_lists;
		
		for(GroupMember list: lists) {
			try {
				list.setGroupuid(groupUid);
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				GroupMemberService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(String groupUid, List<GroupMember> lists) throws IllegalArgumentException, Exception{
		if(null == groupUid)
			throw new IllegalArgumentException("User Group UID can not be empty!");
		
		if(!this.groupService.existByUid(groupUid))
			throw new IllegalArgumentException("User Group UID does not exist!(" + groupUid + ")");
		
		return this.dao.saveBatch(groupUid, lists);
	}
	
	public int deleteByUserUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("User Uid can not be empty!");
		
		return this.dao.deleteByUserUid(uid);
	}
	
	public int deleteByGroupUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("User Group Uid can not be empty!");
		
		return this.dao.deleteByGroupUid(uid);
	}
	
	public int deleteByPKUids(String groupUid, String userUid) throws IllegalArgumentException, Exception{
		if(null == groupUid || groupUid.trim().isEmpty())
			throw new IllegalArgumentException("User Group Uid can not be empty!");
		
		if(null == userUid || userUid.trim().isEmpty())
			throw new IllegalArgumentException("User Uid can not be empty!");
		
		return this.dao.deleteByPKUids(groupUid, userUid);
	}
	
	public Boolean existByGroupUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("User Group UID can not be empty!");
				
		return this.dao.existByGroupUid(uid);
	}
}
