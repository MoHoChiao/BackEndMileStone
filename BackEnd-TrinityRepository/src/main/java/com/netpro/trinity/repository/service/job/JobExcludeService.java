package com.netpro.trinity.repository.service.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jdbc.job.JobExcludeJDBCDao;
import com.netpro.trinity.repository.entity.job.jdbc.JobExclude;
import com.netpro.trinity.repository.service.frequency.ExclFrequencyService;

@Service
public class JobExcludeService {
	
	@Autowired
	private JobExcludeJDBCDao dao;
	
	@Autowired
	private ExclFrequencyService efService;
	@Autowired
	private JobService jobService;
	
	public List<String> getAllJobUids() throws Exception{
		return this.dao.findAllJobUids();
	}
	
	public List<String> getJobUidsByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.findJobUidsByExcludeFrequencyUid(uid);
	}
	
	public List<JobExclude> getJobFullPathByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.findJobFullPathByExcludeFrequencyUid(uid);
	}
	
	public JobExclude add(JobExclude je) throws IllegalArgumentException, Exception{
		String excludefrequencyuid = je.getExcludefrequencyuid();
		if(null == excludefrequencyuid || excludefrequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
		
		if(excludefrequencyuid.trim().equalsIgnoreCase("global"))
			throw new IllegalArgumentException("Exclude Frequency Uid can not be global!");
		
		if(!this.efService.existByUid(excludefrequencyuid))
			throw new IllegalArgumentException("Exclude Frequency UID does not exist!(" + excludefrequencyuid + ")");
		
		String jobuid = je.getJobuid();
		if(null == jobuid || jobuid.trim().length() <= 0)
			throw new IllegalArgumentException("Job UID can not be empty!");
	
		if(!this.jobService.existByUid(jobuid))
			throw new IllegalArgumentException("Job UID does not exist!(" + jobuid + ")");
		
		if(this.dao.save(je) > 0)
			return je;
		else
			throw new IllegalArgumentException("Add Job Exclude Fail!");
	}
	
	public int deleteByJobUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Job Uid can not be empty!");
		
		return this.dao.deleteByJobUid(uid);
	}
	
	public int deleteByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
		
		return this.dao.deleteByExcludeFrequencyUid(uid);
	}
	
	public Boolean exitByExcludeFrequencyUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
				
		return this.dao.exitByExcludeFrequencyUid(uid);
	}
}
