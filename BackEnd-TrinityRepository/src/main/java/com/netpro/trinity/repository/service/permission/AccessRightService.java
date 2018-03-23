package com.netpro.trinity.repository.service.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.permission.AccessRightJDBCDao;
import com.netpro.trinity.repository.entity.permission.jdbc.AccessRight;

@Service
public class AccessRightService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessRightService.class);
	
	@Autowired
	private AccessRightJDBCDao dao;
	
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
}
