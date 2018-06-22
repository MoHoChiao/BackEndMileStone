package com.netpro.trinity.service.permission.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.member.entity.Trinityuser;
import com.netpro.trinity.service.member.service.RoleMemberService;
import com.netpro.trinity.service.member.service.TrinityuserService;
import com.netpro.trinity.service.permission.dao.AccessRightJDBCDao;
import com.netpro.trinity.service.permission.dto.AgentFuncPermission;
import com.netpro.trinity.service.permission.dto.AliasRefFuncPermission;
import com.netpro.trinity.service.permission.dto.ConnectionFuncPermission;
import com.netpro.trinity.service.permission.dto.DomainFuncPermission;
import com.netpro.trinity.service.permission.dto.ExternalRuleFuncPermission;
import com.netpro.trinity.service.permission.dto.FileSourceFuncPermission;
import com.netpro.trinity.service.permission.dto.FrequencyFuncPermission;
import com.netpro.trinity.service.permission.dto.ObjectPermission;
import com.netpro.trinity.service.permission.dto.PerformanceFuncPermission;
import com.netpro.trinity.service.permission.dto.PermissionTable;
import com.netpro.trinity.service.permission.entity.AccessRight;

@Service
public class AccessRightService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessRightService.class);
	
	@Autowired
	private AccessRightJDBCDao dao;
	
	@Autowired
	private TrinityuserService userService;
	@Autowired
	private RoleMemberService roleMemberService;
	
	public List<AccessRight> getByPeopleUid(String peopleUid) throws IllegalArgumentException, Exception{
		if(peopleUid == null || peopleUid.isEmpty())
			throw new IllegalArgumentException("People UID can not be empty!");
				
		return this.dao.findByPeopleUid(peopleUid);
	}
	
	public List<AccessRight> getByObjectUid(String objectUid) throws IllegalArgumentException, Exception{
		if(objectUid == null || objectUid.isEmpty())
			throw new IllegalArgumentException("Object UID can not be empty!");
				
		return this.dao.findByObjectUid(objectUid);
	}
	
	public List<AccessRight> getUserExByObjectUid(String objectUid) throws IllegalArgumentException, Exception{
		if(objectUid == null || objectUid.isEmpty())
			throw new IllegalArgumentException("Object UID can not be empty!");
				
		return this.dao.findUserExByObjectUid(objectUid);
	}
	
	public List<AccessRight> getRoleExByObjectUid(String objectUid) throws IllegalArgumentException, Exception{
		if(objectUid == null || objectUid.isEmpty())
			throw new IllegalArgumentException("Object UID can not be empty!");
				
		return this.dao.findRoleExByObjectUid(objectUid);
	}
	
	public AccessRight getByAllPKs(String peopleUid, String objectUid) throws IllegalArgumentException, Exception{
		if(peopleUid == null || peopleUid.isEmpty())
			throw new IllegalArgumentException("People UID can not be empty!");
		
		if(objectUid == null || objectUid.isEmpty())
			throw new IllegalArgumentException("Object UID can not be empty!");
				
		return this.dao.findByAllPKs(peopleUid, objectUid);
	}
	
	public List<AccessRight> getFunctionalPermissionByPeopleUid(String peopleUid) throws IllegalArgumentException, Exception{
		if(peopleUid == null || peopleUid.isEmpty())
			throw new IllegalArgumentException("People UID can not be empty!");
				
		return this.dao.findFunctionalPermissionByPeopleUid(peopleUid);
	}
	
	public AccessRight add(AccessRight list) throws IllegalArgumentException, Exception{
		if(null == list.getPeopleuid())
			throw new IllegalArgumentException("People UID can not be empty!");
		
		if(null == list.getObjectuid())
			throw new IllegalArgumentException("Object UID can not be empty!");
		
		if(this.dao.existByAllPKs(list))
			throw new IllegalArgumentException("Duplicate PKs (People UID & Object UID)");
		
		if(null == list.getView())
			list.setView("0");
		
		if(null == list.getAdd())
			list.setAdd("0");
		
		if(null == list.getDelete())
			list.setDelete("0");
		
		if(null == list.getEdit())
			list.setEdit("0");
		
		if(null == list.getRun())
			list.setRun("0");
		
		if(null == list.getReRun())
			list.setReRun("0");
		
		if(null == list.getGrant())
			list.setGrant("0");
		
		if(null == list.getImport_export())
			list.setImport_export("0");
		
		if(this.dao.add(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Add Access Right Fail!");
	}
	
	public List<AccessRight> add(List<AccessRight> lists) throws IllegalArgumentException, Exception{
		List<AccessRight> new_lists = new ArrayList<AccessRight>();
		
		if(null == lists)
			return new_lists;
		
		for(AccessRight list: lists) {
			try {
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				AccessRightService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public AccessRight update(AccessRight list) throws IllegalArgumentException, Exception{
		if(null == list.getPeopleuid())
			throw new IllegalArgumentException("People UID can not be empty!");
		
		if(null == list.getObjectuid())
			throw new IllegalArgumentException("Object UID can not be empty!");
		
		if(null == list.getView())
			list.setView("0");
		
		if(null == list.getAdd())
			list.setAdd("0");
		
		if(null == list.getDelete())
			list.setDelete("0");
		
		if(null == list.getEdit())
			list.setEdit("0");
		
		if(null == list.getRun())
			list.setRun("0");
		
		if(null == list.getReRun())
			list.setReRun("0");
		
		if(null == list.getGrant())
			list.setGrant("0");
		
		if(null == list.getImport_export())
			list.setImport_export("0");
		
		if(this.dao.update(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Update Access Right Fail!");
	}
	
	public List<AccessRight> update(List<AccessRight> lists) throws IllegalArgumentException, Exception{
		List<AccessRight> new_lists = new ArrayList<AccessRight>();
		
		if(null == lists)
			return new_lists;
		
		for(AccessRight list: lists) {
			try {
				this.update(list);
				new_lists.add(list);
			}catch(Exception e) {
				AccessRightService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(List<AccessRight> lists) throws IllegalArgumentException, Exception{
		return this.dao.addBatch(lists);
	}
	
	public int[] updateBatch(List<AccessRight> lists) throws IllegalArgumentException, Exception{
		return this.dao.updateBatch(lists);
	}
	
	public List<AccessRight> modifyByPeopleUid(String peopleUid, List<AccessRight> lists) throws IllegalArgumentException, Exception{
		List<AccessRight> new_lists = new ArrayList<AccessRight>();
		if(null == lists)
			return new_lists;
		
		Map<String, AccessRight> oldAccessRightMap = new HashMap<String, AccessRight>();
		Map<String, AccessRight> newAccessRightMap = new HashMap<String, AccessRight>();		
		
		List<AccessRight> oldAccessRightList = this.dao.findByPeopleUid(peopleUid);
		for(AccessRight accessRight : oldAccessRightList) {
			oldAccessRightMap.put(accessRight.getObjectuid().trim(), accessRight);
		}
		
		for(AccessRight accessRight : lists) {
			newAccessRightMap.put(accessRight.getObjectuid().trim(), accessRight);
			
			AccessRight oldAccessRight = oldAccessRightMap.get(accessRight.getObjectuid().trim());
			if(oldAccessRight == null) {
				try {
					accessRight.setPeopleuid(peopleUid);
					this.add(accessRight);
					new_lists.add(accessRight);
				}catch(Exception e) {
					AccessRightService.LOGGER.warn("Warning; reason was:", e);
				}
			}else {
				try {
					String acCode = accessRight.getView() + accessRight.getAdd() + accessRight.getDelete() + accessRight.getEdit() + 
							accessRight.getRun() + accessRight.getReRun() + accessRight.getGrant() + accessRight.getImport_export();
					
					String old_acCode = oldAccessRight.getView() + oldAccessRight.getAdd() + oldAccessRight.getDelete() + oldAccessRight.getEdit() + 
							oldAccessRight.getRun() + oldAccessRight.getReRun() + oldAccessRight.getGrant() + oldAccessRight.getImport_export();
					
					if(!acCode.equals(old_acCode)) {
						accessRight.setPeopleuid(peopleUid);
						this.update(accessRight);
						new_lists.add(accessRight);
					}else {
						new_lists.add(accessRight);
					}
				}catch(Exception e) {
					AccessRightService.LOGGER.warn("Warning; reason was:", e);
				}
			}
		}
		
		for(AccessRight accessRight : oldAccessRightList) {
			if(null == newAccessRightMap.get(accessRight.getObjectuid().trim()) ) {
				this.deleteByPKs(peopleUid, accessRight.getObjectuid());
			}
		}	
		
		return new_lists;
	}
	
	public List<AccessRight> modifyByObjectUid(String objectUid, List<AccessRight> lists) throws IllegalArgumentException, Exception{
		List<AccessRight> new_lists = new ArrayList<AccessRight>();
		if(null == lists)
			return new_lists;
		
		Map<String, AccessRight> oldAccessRightMap = new HashMap<String, AccessRight>();
		Map<String, AccessRight> newAccessRightMap = new HashMap<String, AccessRight>();		
		
		List<AccessRight> oldAccessRightList = this.dao.findByObjectUid(objectUid);
		for(AccessRight accessRight : oldAccessRightList) {
			oldAccessRightMap.put(accessRight.getPeopleuid().trim(), accessRight);
		}
		
		for(AccessRight accessRight : lists) {
			newAccessRightMap.put(accessRight.getPeopleuid().trim(), accessRight);
			
			AccessRight oldAccessRight = oldAccessRightMap.get(accessRight.getPeopleuid().trim());
			if(oldAccessRight == null) {
				try {
					accessRight.setObjectuid(objectUid);
					this.add(accessRight);
					new_lists.add(accessRight);
				}catch(Exception e) {
					AccessRightService.LOGGER.warn("Warning; reason was:", e);
				}
			}else {
				try {
					String acCode = accessRight.getView() + accessRight.getAdd() + accessRight.getDelete() + accessRight.getEdit() + 
							accessRight.getRun() + accessRight.getReRun() + accessRight.getGrant() + accessRight.getImport_export();
					
					String old_acCode = oldAccessRight.getView() + oldAccessRight.getAdd() + oldAccessRight.getDelete() + oldAccessRight.getEdit() + 
							oldAccessRight.getRun() + oldAccessRight.getReRun() + oldAccessRight.getGrant() + oldAccessRight.getImport_export();
					
					if(!acCode.equals(old_acCode)) {
						accessRight.setObjectuid(objectUid);
						this.update(accessRight);
						new_lists.add(accessRight);
					}else {
						new_lists.add(accessRight);
					}
				}catch(Exception e) {
					AccessRightService.LOGGER.warn("Warning; reason was:", e);
				}
			}
		}
		
		for(AccessRight accessRight : oldAccessRightList) {
			if(null == newAccessRightMap.get(accessRight.getPeopleuid().trim()) ) {
				this.deleteByPKs(accessRight.getPeopleuid(), objectUid);
			}
		}	
		
		return new_lists;
	}
	
	public void deleteByPeopleUid(String peopleUid) throws IllegalArgumentException, Exception{
		if(null == peopleUid || peopleUid.trim().isEmpty())
			throw new IllegalArgumentException("People Uid can not be empty!");
		
		this.dao.deleteByPeopleUid(peopleUid);
	}
	
	public void deleteByObjectUid(String objectUid) throws IllegalArgumentException, Exception{
		if(null == objectUid || objectUid.trim().isEmpty())
			throw new IllegalArgumentException("Object Uid can not be empty!");
		
		this.dao.deleteByObjectUid(objectUid);
	}
	
	public void deleteByPKs(String peopleUid, String objectUid) throws IllegalArgumentException, Exception{
		if(null == peopleUid || peopleUid.trim().isEmpty())
			throw new IllegalArgumentException("People Uid can not be empty!");
		
		if(null == objectUid || objectUid.trim().isEmpty())
			throw new IllegalArgumentException("Object Uid can not be empty!");
		
		this.dao.deleteByPKs(peopleUid, objectUid);
	}
	
	public PermissionTable loadPermissionTable(String userid) throws IllegalArgumentException, Exception {
		//Permission Table initial
		PermissionTable permissionTable = new PermissionTable();
		
		Trinityuser user = userService.getByID(userid);
		
		//判斷是否為root帳號
		permissionTable.setRoot(false);
		if(user.getUsertype().equals("R"))
			permissionTable.setRoot(true);
		
		//判斷是否為admin edit mode
		permissionTable.setAdminEditMode(this.dao.isAdminEditMode());
		
		//把此useruid所屬的所有role之uids找出來
		List<String> roleUids = roleMemberService.getRoleUidsByUserUid(user.getUseruid());
		List<List<AccessRight>> accessRightListForRoles = new ArrayList<List<AccessRight>>();
		permissionTable.setAdmin(false);
		permissionTable.setAccountManager(false);
		for(String roleUid : roleUids) {
			//判斷是否為admin role帳號
			if("Role1".equals(roleUid))
				permissionTable.setAdmin(true);
			//判斷是否為account manager帳號
			if("Role5".equals(roleUid))
				permissionTable.setAccountManager(true);
			
			//把此useruid所屬的每一個role的access right存入accessRightListForRoles
			List<AccessRight> accessRightForRoles = this.dao.findByPeopleUid(roleUid);
			accessRightListForRoles.add(accessRightForRoles);
		}
		
		//儲存對object的權限, key為object uid, value是ObjectPermission
		Map<String, ObjectPermission> object_map = new HashMap<String, ObjectPermission>();
		
		//找出該useruid所屬的所有roles對object及object function的權限
		for(int i = 0; i < accessRightListForRoles.size(); i++) {
			for(AccessRight index : accessRightListForRoles.get(i)) {
				String objectUid = index.getObjectuid();
				if("function-connection".equals(objectUid.trim())) {
					ConnectionFuncPermission connFunc = new ConnectionFuncPermission();
					if("1".equals(index.getView()))
						connFunc.setView(true);
					if("1".equals(index.getAdd()))
						connFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						connFunc.setModify(true);
					if("1".equals(index.getDelete()))
						connFunc.setDelete(true);
					permissionTable.setConnection_func(connFunc);
				}else if("function-jcsagent".equals(objectUid.trim())) {
					AgentFuncPermission agentFunc = new AgentFuncPermission();
					if("1".equals(index.getView()))
						agentFunc.setView(true);
					if("1".equals(index.getAdd()))
						agentFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						agentFunc.setModify(true);
					if("1".equals(index.getDelete()))
						agentFunc.setDelete(true);
					permissionTable.setAgent_func(agentFunc);
				}else if("function-frequency".equals(objectUid.trim())) {
					FrequencyFuncPermission freqFunc = new FrequencyFuncPermission();
					if("1".equals(index.getView()))
						freqFunc.setView(true);
					if("1".equals(index.getAdd()))
						freqFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						freqFunc.setModify(true);
					if("1".equals(index.getDelete()))
						freqFunc.setDelete(true);
					permissionTable.setFrequency_func(freqFunc);
				}else if("function-domain".equals(objectUid.trim())) {
					DomainFuncPermission domainFunc = new DomainFuncPermission();
					if("1".equals(index.getView()))
						domainFunc.setView(true);
					if("1".equals(index.getAdd()))
						domainFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						domainFunc.setModify(true);
					if("1".equals(index.getDelete()))
						domainFunc.setDelete(true);
					permissionTable.setDomain_func(domainFunc);
				}else if("function-filesource".equals(objectUid.trim())) {
					FileSourceFuncPermission filesourceFunc = new FileSourceFuncPermission();
					if("1".equals(index.getView()))
						filesourceFunc.setView(true);
					if("1".equals(index.getAdd()))
						filesourceFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						filesourceFunc.setModify(true);
					if("1".equals(index.getDelete()))
						filesourceFunc.setDelete(true);
					permissionTable.setFilesource_func(filesourceFunc);
				}else if("function-aliasref".equals(objectUid.trim())) {
					AliasRefFuncPermission aliasFunc = new AliasRefFuncPermission();
					if("1".equals(index.getView()))
						aliasFunc.setView(true);
					if("1".equals(index.getAdd()))
						aliasFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						aliasFunc.setModify(true);
					if("1".equals(index.getDelete()))
						aliasFunc.setDelete(true);
					permissionTable.setAlias_func(aliasFunc);
				}else if("function-extrule".equals(objectUid.trim())) {
					ExternalRuleFuncPermission extruleFunc = new ExternalRuleFuncPermission();
					if("1".equals(index.getView()))
						extruleFunc.setView(true);
					if("1".equals(index.getAdd()))
						extruleFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						extruleFunc.setModify(true);
					if("1".equals(index.getDelete()))
						extruleFunc.setDelete(true);
					permissionTable.setExtrule_func(extruleFunc);
				}else if("function-performance".equals(objectUid.trim())) {
					PerformanceFuncPermission performanceFunc = new PerformanceFuncPermission();
					if("1".equals(index.getView()))
						performanceFunc.setView(true);
					if("1".equals(index.getAdd()))
						performanceFunc.setAdd(true);
					if("1".equals(index.getEdit()))
						performanceFunc.setModify(true);
					if("1".equals(index.getDelete()))
						performanceFunc.setDelete(true);
					permissionTable.setPerformance_func(performanceFunc);
				}else if(objectUid.trim().indexOf("function-") == -1){
					ObjectPermission objPermission = new ObjectPermission();
					objPermission.setObjuid(objectUid.trim());
					if("1".equals(index.getView()))
						objPermission.setView(true);
					if("1".equals(index.getAdd()))
						objPermission.setAdd(true);
					if("1".equals(index.getEdit()))
						objPermission.setModify(true);
					if("1".equals(index.getDelete()))
						objPermission.setDelete(true);
					if("1".equals(index.getRun()))
						objPermission.setRun(true);
					if("1".equals(index.getReRun()))
						objPermission.setRerun(true);
					if("1".equals(index.getGrant()))
						objPermission.setGrant(true);
					object_map.put(objectUid.trim(), objPermission);
				}
			}
		}
		
		//找出該useruid所屬的所有roles對object的權限
		List<AccessRight> accessRightForUser = this.dao.findByPeopleUid(user.getUseruid());
		for(AccessRight access : accessRightForUser) {
			String objectUid = access.getObjectuid();
			
			ObjectPermission objPermission = new ObjectPermission();
			objPermission.setObjuid(objectUid.trim());
			if("1".equals(access.getView()))
				objPermission.setView(true);
			if("1".equals(access.getAdd()))
				objPermission.setAdd(true);
			if("1".equals(access.getEdit()))
				objPermission.setModify(true);
			if("1".equals(access.getDelete()))
				objPermission.setDelete(true);
			if("1".equals(access.getRun()))
				objPermission.setRun(true);
			if("1".equals(access.getReRun()))
				objPermission.setRerun(true);
			if("1".equals(access.getGrant()))
				objPermission.setGrant(true);
			object_map.put(objectUid.trim(), objPermission);
		}
		permissionTable.setObject_map(object_map);
		
		return permissionTable;
	}
}
