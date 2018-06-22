package com.netpro.trinity.service.frequency.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.frequency.dao.WorkingCalendarListJDBCDao;
import com.netpro.trinity.service.frequency.entity.WorkingCalendarList;

@Service
public class WorkingCalendarListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkingCalendarListService.class);
	
	@Autowired
	private WorkingCalendarListJDBCDao dao;
	
	@Autowired
	private WorkingCalendarService wcService;
	
	public List<WorkingCalendarList> getByWCUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Working Calendar UID can not be empty!");

		return this.dao.findByWCUid(uid);
	}
	
	public WorkingCalendarList add(WorkingCalendarList list) throws IllegalArgumentException, Exception{
		String wcalendaruid = list.getWcalendaruid();
		if(null == wcalendaruid || wcalendaruid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar UID can not be empty!");
		
		if(wcalendaruid.trim().equalsIgnoreCase("SYSTEMDAY"))
			throw new IllegalArgumentException("System day can not be edited!");
		
		if(!this.wcService.existByUid(wcalendaruid))
			throw new IllegalArgumentException("Working Calendar UID does not exist!(" + wcalendaruid + ")");
		
		Integer yearnum = list.getYearnum();
		if(null == yearnum)
			throw new IllegalArgumentException("Year Number must be Integer!");
		
		Integer monthnum = list.getMonthnum();
		if(null == monthnum)
			throw new IllegalArgumentException("Month Number must be Integer!");
		
		Integer daynum = list.getDaynum();
		if(null == daynum)
			throw new IllegalArgumentException("Day Number must be Integer!");
		
		if(this.dao.existByAllPKs(list))
			throw new IllegalArgumentException("Duplicate Working Calendar List!");
		
		if(this.dao.save(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Add Working Calendar List Fail!");
	}
	
	public List<WorkingCalendarList> add(String wcUid, List<WorkingCalendarList> lists) throws IllegalArgumentException, Exception{
		List<WorkingCalendarList> new_lists = new ArrayList<WorkingCalendarList>();
		
		if(null == lists)
			return new_lists;
		
		for(WorkingCalendarList list: lists) {
			try {
				list.setWcalendaruid(wcUid);
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				WorkingCalendarListService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(String wcUid, List<WorkingCalendarList> lists) throws IllegalArgumentException, Exception{
		if(null == wcUid || wcUid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar UID can not be empty!");
		
		if(!this.wcService.existByUid(wcUid))
			throw new IllegalArgumentException("Working Calendar UID does not exist!(" + wcUid + ")");
		
		return this.dao.saveBatch(wcUid, lists);
	}
	
	public void deleteByWCUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar UID can not be empty!");
		
		this.dao.deleteByWCUid(uid);
	}
}
