package com.netpro.trinity.repository.service.permission;

import java.util.ArrayList;
import java.util.List;

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
	
	public AccessRight add(AccessRight list) throws IllegalArgumentException, Exception{
		if(null == list.getPeopleuid())
			throw new IllegalArgumentException("People UID can not be empty!");
		
		if(null == list.getObjectuid())
			throw new IllegalArgumentException("Object UID can not be empty!");
		
		if(this.dao.existByAllPKs(list))
			throw new IllegalArgumentException("Duplicate PKs (People UID & Object UID)");
		
		if(null == list.getFlag1())
			list.setFlag1("0");
		
		if(null == list.getFlag2())
			list.setFlag2("0");
		
		if(null == list.getFlag3())
			list.setFlag3("0");
		
		if(null == list.getFlag4())
			list.setFlag4("0");
		
		if(null == list.getFlag5())
			list.setFlag5("0");
		
		if(null == list.getFlag6())
			list.setFlag6("0");
		
		if(null == list.getFlag7())
			list.setFlag7("0");
		
		if(null == list.getFlag8())
			list.setFlag8("0");
		
		if(this.dao.save(list) > 0)
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
	
	public int[] addBatch(List<AccessRight> lists) throws IllegalArgumentException, Exception{
		return this.dao.saveBatch(lists);
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
}
