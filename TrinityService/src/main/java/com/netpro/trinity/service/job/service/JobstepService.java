package com.netpro.trinity.service.job.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.job.dao.JobstepJPADao;
import com.netpro.trinity.service.job.entity.Jobstep;

@Service
public class JobstepService {
	public static final String[] JOBSTEP_FIELD_VALUES = new String[] { "stepname", "description" };
	public static final Set<String> JOBSTEP_FIELD_SET = new HashSet<>(Arrays.asList(JOBSTEP_FIELD_VALUES));
	
	/*
	 * steptype的值,對應如下
	 * A : External Command Jobstep
	 * B : Data Management Jobstep
	 * C : Teradata Script Jobstep
	 * D : SQL Script Jobstep
	 * F : FTP Jobstep
	 * G : Mail Jobstep
	 * M : Mail
	 * E : Big Data Jobstep
	 * R : Recovery Jobstep
	 * H : SQL Report Jobstep
	 */
	public static final String[] JOBSTEP_TYPE_VALUES = new String[] { "A", "B", "C", "D", "F", "G", "M", "E", "R", "H" };
	public static final Set<String> JOBSTEP_TYPE_SET = new HashSet<>(Arrays.asList(JOBSTEP_TYPE_VALUES));
	
	@Autowired
	private JobstepJPADao dao;
	
	public List<Jobstep> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public List<Jobstep> getByType(String type) throws IllegalArgumentException, Exception{
		if(type == null || type.isEmpty())
			throw new IllegalArgumentException("Job Step Type can not be empty!");
		
		type = type.toUpperCase();
		
		/*
		 * steptype的值,對應如下
		 * A : External Command Jobstep
		 * B : Data Management Jobstep
		 * C : Teradata Script Jobstep
		 * D : SQL Script Jobstep
		 * F : FTP Jobstep
		 * G : Mail Jobstep
		 * M : Mail
		 * E : Big Data Jobstep
		 * R : Recovery Jobstep
		 * H : SQL Report Jobstep
		 */
		if(!JOBSTEP_TYPE_SET.contains(type))
				throw new IllegalArgumentException("Illegal Job Step type! "+ JOBSTEP_TYPE_SET.toString());
		
		return this.dao.findBysteptype(type);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}
	
	public boolean existByConnectionuid(String connectionuid) throws IllegalArgumentException, Exception{
		if(null == connectionuid || connectionuid.trim().length() <= 0)
			throw new IllegalArgumentException("Connection Uid can not be empty!");
		
		boolean ret = false;
		
		List<Jobstep> jobsteps = this.dao.findAll();
		for(Jobstep jobstep : jobsteps){
			String xmldata = jobstep.getXmldata();
			if(null != xmldata && xmldata.indexOf(connectionuid) > -1)
				return true;
		}
		
		return ret;
	}
}
