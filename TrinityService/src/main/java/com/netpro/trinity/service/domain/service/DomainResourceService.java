package com.netpro.trinity.service.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.domain.dao.DomainResourceJDBCDao;
import com.netpro.trinity.service.domain.entity.DomainResource;

@Service
public class DomainResourceService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainResourceService.class);
	
	@Autowired
	private DomainResourceJDBCDao dao;
	
	@Autowired
	private DomainService dService;
	
	public List<DomainResource> getByDomainUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");

		return this.dao.findByDomainUid(uid);
	}
	
	public DomainResource add(DomainResource resource) throws IllegalArgumentException, Exception{
		String domainuid = resource.getDomainuid();
		if(null == domainuid || domainuid.trim().isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		if(!this.dService.existByUid(domainuid))
			throw new IllegalArgumentException("Domain UID does not exist!(" + domainuid + ")");
		
		String resourcename = resource.getResourcename();
		if(null == resourcename || resourcename.trim().isEmpty())
			throw new IllegalArgumentException("Resource Name can not be empty!!");
		resource.setResourcename(resourcename.toUpperCase());
		
		String resourcevalue = resource.getResourcevalue();
		if(null == resourcevalue|| resourcevalue.trim().isEmpty())
			throw new IllegalArgumentException("Resource Value can not be empty!!");
		
		if(this.dao.existByAllPKs(resource))
			throw new IllegalArgumentException("Duplicate Domain Resource!");
		
		if(this.dao.save(resource) > 0)
			return resource;
		else
			throw new IllegalArgumentException("Add Domain Resource Fail!");
	}
	
	public List<DomainResource> add(String domainUid, List<DomainResource> resources) throws IllegalArgumentException, Exception{
		List<DomainResource> new_resources = new ArrayList<DomainResource>();
		
		if(null == resources)
			return new_resources;
		
		for(DomainResource resource: resources) {
			try {
				resource.setDomainuid(domainUid);
				this.add(resource);
				new_resources.add(resource);
			}catch(Exception e) {
				DomainResourceService.LOGGER.warn("Warning; reason was:", e);
			}
		}
		return new_resources;
	}
	
	public int[] addBatch(String domainUid, List<DomainResource> resources) throws IllegalArgumentException, Exception{
		if(null == domainUid || domainUid.trim().isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		if(!this.dService.existByUid(domainUid))
			throw new IllegalArgumentException("Domain UID does not exist!(" + domainUid + ")");
		
		return this.dao.saveBatch(domainUid, resources);
	}
	
	public void deleteByDomainUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		this.dao.deleteByDomainUid(uid);
	}
}
