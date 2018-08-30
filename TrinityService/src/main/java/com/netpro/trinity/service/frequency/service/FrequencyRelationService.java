package com.netpro.trinity.service.frequency.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.frequency.dao.FrequencyRelationJDBCDao;
import com.netpro.trinity.service.frequency.entity.FrequencyCategory;
import com.netpro.trinity.service.frequency.entity.FrequencyRelation;

@Service
public class FrequencyRelationService {
	
	@Autowired
	private FrequencyRelationJDBCDao dao;
	
	@Autowired
	private FrequencyCategoryService categoryService;
	@Autowired
	private FrequencyService freqService;
	
	public List<String> getAllFrequencyUids() throws Exception{
		return this.dao.findAllFrequencyUids();
	}
	
	public List<String> getFrequencyUidsByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Frequency Category UID can not be empty!");
				
		return this.dao.findFrequencyUidsByCategoryUid(uid);
	}
	
	public FrequencyCategory getCategoryByFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		return this.dao.findCategoryByFrequencyUid(uid);
	}
	
	public Map<String, String> getFrequencyUidAndCategoryNameMap() throws IllegalArgumentException, Exception{
		return this.dao.findFrequencyUidAndCategoryNameMap();
	}
	
	public FrequencyRelation add(FrequencyRelation rel) throws IllegalArgumentException, Exception{
		String freqcategoryuid = rel.getFreqcategoryuid();
		if(null == freqcategoryuid || freqcategoryuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category UID can not be empty!");
		
		if(!this.categoryService.existByUid(freqcategoryuid))
			throw new IllegalArgumentException("Frequency Category UID does not exist!(" + freqcategoryuid + ")");
		
		String frequencyuid = rel.getFrequencyuid();
		if(null == frequencyuid || frequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency UID can not be empty!");
	
		if(!this.freqService.existByUid(frequencyuid))
			throw new IllegalArgumentException("Frequency UID does not exist!(" + frequencyuid + ")");
		
		if(this.dao.save(rel) > 0)
			return rel;
		else
			throw new IllegalArgumentException("Add Frequency Relation Fail!");
	}
	
	public int deleteByFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		return this.dao.deleteByFrequencyUid(uid);
	}
	
	public Boolean existByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Frequency Category UID can not be empty!");
				
		return this.dao.existByCategoryUid(uid);
	}
}
