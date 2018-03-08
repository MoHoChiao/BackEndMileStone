package com.netpro.trinity.repository.service.frequency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.frequency.FreqExcludeJDBCDao;
import com.netpro.trinity.repository.entity.frequency.jdbc.FreqExclude;

@Service
public class FreqExcludeService {
	
	@Autowired
	private FreqExcludeJDBCDao dao;
	
	@Autowired
	private ExclFrequencyService efService;
	@Autowired
	private FrequencyService freqService;
	
	public List<String> getAllFrequencyUids() throws Exception{
		return this.dao.findAllFrequencyUids();
	}
	
	public List<String> getFrequencyUidsByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.findFrequencyUidsByExcludeFrequencyUid(uid);
	}
	
	public List<FreqExclude> getFreqFullPathByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.findFreqFullPathByExcludeFrequencyUid(uid);
	}
	
	public FreqExclude add(FreqExclude fe) throws IllegalArgumentException, Exception{
		String excludefrequencyuid = fe.getExcludefrequencyuid();
		if(null == excludefrequencyuid || excludefrequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
		
		if(excludefrequencyuid.trim().equalsIgnoreCase("global"))
			throw new IllegalArgumentException("Exclude Frequency Uid can not be global!");
		
		if(!this.efService.existByUid(excludefrequencyuid))
			throw new IllegalArgumentException("Exclude Frequency UID does not exist!(" + excludefrequencyuid + ")");
		
		String frequencyuid = fe.getFrequencyuid();
		if(null == frequencyuid || frequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency UID can not be empty!");
	
		if(!this.freqService.existByUid(frequencyuid))
			throw new IllegalArgumentException("Frequency UID does not exist!(" + frequencyuid + ")");
		
		if(this.dao.save(fe) > 0)
			return fe;
		else
			throw new IllegalArgumentException("Add Frequency Exclude Fail!");
	}
	
	public int deleteByFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		return this.dao.deleteByFrequencyUid(uid);
	}
	
	public int deleteByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
		
		return this.dao.deleteByExcludeFrequencyUid(uid);
	}
	
	public int deleteByPKUids(String excludeFreqUid, String freqUid) throws IllegalArgumentException, Exception{
		if(null == excludeFreqUid || excludeFreqUid.trim().isEmpty())
			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
		
		if(null == freqUid || freqUid.trim().isEmpty())
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		return this.dao.deleteByPKUids(excludeFreqUid, freqUid);
	}
	
	public Boolean existByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.existByExcludeFrequencyUid(uid);
	}
}
