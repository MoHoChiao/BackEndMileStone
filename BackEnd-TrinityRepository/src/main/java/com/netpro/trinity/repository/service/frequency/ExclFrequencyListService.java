package com.netpro.trinity.repository.service.frequency;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.frequency.ExclFrequencyListJDBCDao;
import com.netpro.trinity.repository.entity.frequency.jdbc.ExclFrequencyList;

@Service
public class ExclFrequencyListService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExclFrequencyListService.class);
	
	@Autowired
	private ExclFrequencyListJDBCDao dao;
	
	@Autowired
	private ExclFrequencyService exclService;
	
	public List<ExclFrequencyList> getByExclFreqUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");

		return this.dao.findByExclFreqUid(uid);
	}
	
	public ExclFrequencyList add(ExclFrequencyList list) throws IllegalArgumentException, Exception{
		String excludefrequencyuid = list.getExcludefrequencyuid();
		if(null == excludefrequencyuid || excludefrequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
		
		if(!this.exclService.existByUid(excludefrequencyuid))
			throw new IllegalArgumentException("Exclude Frequency UID does not exist!(" + excludefrequencyuid + ")");
		
		Integer seq = this.dao.findMaxSeqByExclFreqUid(excludefrequencyuid);
		if(null == seq)
			seq = 1;
		else
			seq++;
		list.setSeq(seq);
		
		if(this.dao.existByAllPKs(list))
			throw new IllegalArgumentException("Duplicate Exclude Frequency List!");
		
		Long starttime = list.getStarttime();
		if(null == starttime)
			throw new IllegalArgumentException("Start time must be Integer!");
		
		Long endtime = list.getEndtime();
		if(null == endtime)
			throw new IllegalArgumentException("End time must be Integer!");
		
		if(this.dao.existByTime(list))
			throw new IllegalArgumentException("Duplicate Exclude Frequency Start Time And End Time!");
		
		if(this.dao.save(list) > 0)
			return list;
		else
			throw new IllegalArgumentException("Add Exclude Frequency List Fail!");
	}
	
	public List<ExclFrequencyList> add(String exclFreqUid, List<ExclFrequencyList> lists) throws IllegalArgumentException, Exception{
		List<ExclFrequencyList> new_lists = new ArrayList<ExclFrequencyList>();
		
		if(null == lists)
			return new_lists;
		
		for(ExclFrequencyList list: lists) {
			try {
				list.setExcludefrequencyuid(exclFreqUid);
				this.add(list);
				new_lists.add(list);
			}catch(Exception e) {
				ExclFrequencyListService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_lists;
	}
	
	public int[] addBatch(String exclFreqUid, List<ExclFrequencyList> lists) throws IllegalArgumentException, Exception{
		if(null == exclFreqUid || exclFreqUid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
		
		if(!this.exclService.existByUid(exclFreqUid))
			throw new IllegalArgumentException("Exclude Frequency UID does not exist!(" + exclFreqUid + ")");
		
		return this.dao.saveBatch(exclFreqUid, lists);
	}
	
	public void deleteByExclFreqUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
		
		this.dao.deleteByExclFreqUid(uid);
	}
}
