package com.netpro.trinity.repository.member.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.member.dao.RoleMemberJDBCDao;
import com.netpro.trinity.repository.member.entity.RoleMember;

@Service
public class RoleMemberService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleMemberService.class);
	
	@Autowired
	private RoleMemberJDBCDao dao;
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private TrinityuserService userService;
	
	public List<String> getAllUserUids() throws Exception{
		return this.dao.findAllUserUids();
	}
	
	public List<String> getUserUidsByRoleUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Role UID can not be empty!");
				
		return this.dao.findUserUidsByRoleUid(uid);
	}
	
	public List<RoleMember> getUserFullNameByRoleUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Role UID can not be empty!");
				
		return this.dao.findUserFullNameByRoleUid(uid);
	}
	
	public RoleMember add(RoleMember rm) throws IllegalArgumentException, Exception{
		String roleuid = rm.getRoleuid();
		if(null == roleuid || roleuid.trim().isEmpty())
			throw new IllegalArgumentException("Role UID can not be empty!");
		
		if(!this.roleService.existByUid(roleuid))
			throw new IllegalArgumentException("Role UID does not exist!(" + roleuid + ")");
		
		String useruid = rm.getUseruid();
		if(null == useruid || useruid.trim().isEmpty())
			throw new IllegalArgumentException("User UID can not be empty!");
	
		if(!this.userService.existByUid(useruid))
			throw new IllegalArgumentException("User UID does not exist!(" + useruid + ")");
		
		if(this.dao.save(rm) > 0)
			return rm;
		else
			throw new IllegalArgumentException("Add Role Member Fail!");
	}
	
	public List<RoleMember> add(String roleUid, List<RoleMember> lists) throws IllegalArgumentException, Exception{
		List<RoleMember> new_lists = new ArrayList<RoleMember>();
		
		if(null == lists)
			return new_lists;
		
		for(RoleMember list: lists) {
			try {
				list.setRoleuid(roleUid);
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				RoleMemberService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(String roleUid, List<RoleMember> lists) throws IllegalArgumentException, Exception{
		if(null == roleUid)
			throw new IllegalArgumentException("Role UID can not be empty!");
		
		if(!this.roleService.existByUid(roleUid))
			throw new IllegalArgumentException("Role UID does not exist!(" + roleUid + ")");
		
		return this.dao.saveBatch(roleUid, lists);
	}
	
	public int deleteByUserUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("User Uid can not be empty!");
		
		return this.dao.deleteByUserUid(uid);
	}
	
	public int deleteByRoleUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Role Uid can not be empty!");
		
		return this.dao.deleteByRoleUid(uid);
	}
	
	public int deleteByPKUids(String roleUid, String userUid) throws IllegalArgumentException, Exception{
		if(null == roleUid || roleUid.trim().isEmpty())
			throw new IllegalArgumentException("Role Uid can not be empty!");
		
		if(null == userUid || userUid.trim().isEmpty())
			throw new IllegalArgumentException("User Uid can not be empty!");
		
		return this.dao.deleteByPKUids(roleUid, userUid);
	}
	
	public Boolean existByRoleUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Role UID can not be empty!");
				
		return this.dao.existByRoleUid(uid);
	}
}
