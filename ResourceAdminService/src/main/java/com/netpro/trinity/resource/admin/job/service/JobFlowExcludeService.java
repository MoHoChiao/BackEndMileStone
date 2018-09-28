package com.netpro.trinity.resource.admin.job.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.frequency.service.ExclFrequencyService;
import com.netpro.trinity.resource.admin.job.dao.JobFlowExcludeJDBCDao;
import com.netpro.trinity.resource.admin.job.entity.JobFlowExclude;

@Service
public class JobFlowExcludeService {
	
	@Autowired
	private JobFlowExcludeJDBCDao dao;
	
	@Autowired
	private ExclFrequencyService efService;
	@Autowired
	private JobFlowService flowService;
	
	public List<String> getAllFlowUids() throws Exception{
		return this.dao.findAllFlowUids();
	}
	
	public List<String> getFlowUidsByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.findFlowUidsByExcludeFrequencyUid(uid);
	}
	
	public List<JobFlowExclude> getFlowFullPathByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.findFlowFullPathByExcludeFrequencyUid(uid);
	}
	
	public JobFlowExclude add(JobFlowExclude jfe) throws IllegalArgumentException, Exception{
		String excludefrequencyuid = jfe.getExcludefrequencyuid();
		if(null == excludefrequencyuid || excludefrequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
		
		if(excludefrequencyuid.trim().equalsIgnoreCase("global"))
			throw new IllegalArgumentException("Exclude Frequency Uid can not be global!");
		
		if(!this.efService.existByUid(excludefrequencyuid))
			throw new IllegalArgumentException("Exclude Frequency UID does not exist!(" + excludefrequencyuid + ")");
		
		String jobflowuid = jfe.getJobflowuid();
		if(null == jobflowuid || jobflowuid.trim().length() <= 0)
			throw new IllegalArgumentException("Flow UID can not be empty!");
	
		if(!this.flowService.existByUid(jobflowuid))
			throw new IllegalArgumentException("Flow UID does not exist!(" + jobflowuid + ")");
		
		if(this.dao.save(jfe) > 0)
			return jfe;
		else
			throw new IllegalArgumentException("Add JobFlow Exclude Fail!");
	}
	
	public int deleteByFlowUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Flow Uid can not be empty!");
		
		return this.dao.deleteByFlowUid(uid);
	}
	
	public int deleteByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
		
		return this.dao.deleteByExcludeFrequencyUid(uid);
	}
	
	public int deleteByPKUids(String excludeFreqUid, String flowUid) throws IllegalArgumentException, Exception{
		if(null == excludeFreqUid || excludeFreqUid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
		
		if(null == flowUid || flowUid.trim().length() <= 0)
			throw new IllegalArgumentException("Flow Uid can not be empty!");
		
		return this.dao.deleteByPKUids(excludeFreqUid, flowUid);
	}
	
	public Boolean existByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.existByExcludeFrequencyUid(uid);
	}
}
