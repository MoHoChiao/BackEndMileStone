package com.netpro.trinity.repository.service.frequency;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.frequency.FrequencyListJDBCDao;
import com.netpro.trinity.repository.entity.frequency.jdbc.FrequencyList;

@Service
public class FrequencyListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyListService.class);
	
	@Autowired
	private FrequencyListJDBCDao dao;
	
	@Autowired
	private FrequencyService freqService;
	
	public List<FrequencyList> getByFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		if(!this.freqService.existByUid(uid))
			throw new IllegalArgumentException("Frequency UID does not exist!(" + uid + ")");
		
		return this.dao.findByFrequencyUid(uid);
	}
	
	public List<FrequencyList> getDistinctDateByFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		if(!this.freqService.existByUid(uid))
			throw new IllegalArgumentException("Frequency UID does not exist!(" + uid + ")");
		
		return this.dao.findDistinctDateByFrequencyUid(uid);
	}
	
	public List<FrequencyList> getDistinctTimeByFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		if(!this.freqService.existByUid(uid))
			throw new IllegalArgumentException("Frequency UID does not exist!(" + uid + ")");
		
		return this.dao.findDistinctTimeByFrequencyUid(uid);
	}
	
	public FrequencyList add(FrequencyList list) throws IllegalArgumentException, Exception{
		String frequencyuid = list.getFrequencyuid();
		if(null == frequencyuid || frequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		if(!this.freqService.existByUid(frequencyuid))
			throw new IllegalArgumentException("Frequency UID does not exist!(" + frequencyuid + ")");
		
		Integer seq = list.getSeq();
		if(null == seq)
			throw new IllegalArgumentException("Seq Number must be Integer!");
		
		Integer yearnum = list.getYearnum();
		if(null == yearnum)
			throw new IllegalArgumentException("Year Number must be Integer!");
		
		Integer monthnum = list.getMonthnum();
		if(null == monthnum)
			throw new IllegalArgumentException("Month Number must be Integer!");
		
		Integer daynum = list.getDaynum();
		if(null == daynum)
			throw new IllegalArgumentException("Day Number must be Integer!");
		
		Integer weekdaynum = list.getWeekdaynum();
		if(null == weekdaynum)
			throw new IllegalArgumentException("Weekday Number must be Integer!");
		
		if(this.dao.existByAllPKs(list))
			throw new IllegalArgumentException("Duplicate Frequency List!");
		
		if(this.dao.save(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Add Frequency List Fail!");
	}
	
	public List<FrequencyList> add(String freqUid, List<FrequencyList> lists) throws IllegalArgumentException, Exception{
		List<FrequencyList> new_lists = new ArrayList<FrequencyList>();
		
		if(null == lists)
			return new_lists;
		
		for(FrequencyList list: lists) {
			try {
				list.setFrequencyuid(freqUid);
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				FrequencyListService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(String freqUid, List<FrequencyList> lists) throws IllegalArgumentException, Exception{
		if(null == freqUid || freqUid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		if(!this.freqService.existByUid(freqUid))
			throw new IllegalArgumentException("Frequency UID does not exist!(" + freqUid + ")");
		
		return this.dao.saveBatch(freqUid, lists);
	}
	
	public void deleteByFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		this.dao.deleteByFrequencyUid(uid);
	}
	
	public Boolean isNumFieldContainsNegativeOne(String freqUid, String fieldName) throws IllegalArgumentException, Exception{
		return this.dao.isNumFieldContainsNegativeOne(freqUid, fieldName);
	}
}
