package com.netpro.trinity.resource.admin.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.domain.dao.DomainVariableJDBCDao;
import com.netpro.trinity.resource.admin.domain.entity.DomainVariable;

@Service
public class DomainVariableService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainVariableService.class);
	
	@Autowired
	private DomainVariableJDBCDao dao;
	
	@Autowired
	private DomainService dService;
	
	public List<DomainVariable> getByDomainUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");

		return this.dao.findByDomainUid(uid);
	}
	
	public DomainVariable add(DomainVariable var) throws IllegalArgumentException, Exception{
		String domainuid = var.getDomainuid();
		if(null == domainuid || domainuid.trim().isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		if(!this.dService.existByUid(domainuid))
			throw new IllegalArgumentException("Domain UID does not exist!(" + domainuid + ")");
		
		String variablename = var.getVariablename();
		if(null == variablename || variablename.trim().isEmpty())
			throw new IllegalArgumentException("Variable Name can not be empty!!");
		var.setVariablename(variablename.toUpperCase());
		
		String variablevalue = var.getVariablevalue();
		if(null == variablevalue)
			var.setVariablevalue("");
		
		if(this.dao.existByAllPKs(var))
			throw new IllegalArgumentException("Duplicate Domain Variable!");
		
		if(this.dao.save(var) > 0)
			return var;
		else
			throw new IllegalArgumentException("Add Domain Variable Fail!");
	}
	
	public List<DomainVariable> add(String domainUid, List<DomainVariable> vars) throws IllegalArgumentException, Exception{
		List<DomainVariable> new_vars = new ArrayList<DomainVariable>();
		
		if(null == vars)
			return new_vars;
		
		for(DomainVariable var: vars) {
			try {
				var.setDomainuid(domainUid);
				this.add(var);
				new_vars.add(var);
			}catch(Exception e) {
				DomainVariableService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_vars;
	}
	
	public int[] addBatch(String domainUid, List<DomainVariable> vars) throws IllegalArgumentException, Exception{
		if(null == domainUid || domainUid.trim().isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		if(!this.dService.existByUid(domainUid))
			throw new IllegalArgumentException("Domain UID does not exist!(" + domainUid + ")");
		
		return this.dao.saveBatch(domainUid, vars);
	}
	
	public void deleteByDomainUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		this.dao.deleteByDomainUid(uid);
	}
}
