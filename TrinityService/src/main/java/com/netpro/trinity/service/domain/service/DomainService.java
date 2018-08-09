package com.netpro.trinity.service.domain.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.domain.dao.DomainJPADao;
import com.netpro.trinity.service.domain.entity.Domain;
import com.netpro.trinity.service.domain.entity.DomainResource;
import com.netpro.trinity.service.domain.entity.DomainVariable;
import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.job.service.JobService;
import com.netpro.trinity.service.objectalias.service.ObjectAliasService;
import com.netpro.trinity.service.util.Constant;

@Service
public class DomainService {
	public static final String[] DOMAIN_FIELD_VALUES = new String[] { "name", "description" };
	public static final Set<String> DOMAIN_FIELD_SET = new HashSet<>(Arrays.asList(DOMAIN_FIELD_VALUES));
	
	@Autowired
	private DomainJPADao dao;
	
	@Autowired
	private DomainVariableService varService;
	@Autowired
	private DomainResourceService resourceService;
	@Autowired
	private JobService jobService;
	@Autowired
	private ObjectAliasService objectAliasService;
	
	public List<Domain> getAll(Boolean withoutDetail) throws Exception{
		List<Domain> domains = this.dao.findAll();
		if(null == withoutDetail || withoutDetail == false)
			getDomainVariableAndResource(domains);
		return domains;
	}
	
	public ResponseEntity<?> getByFilter(Boolean withoutDetail, FilterInfo filter) throws Exception{
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "name");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<Domain> page_domains = this.dao.findByNameLikeIgnoreCase(param, getPagingAndOrdering(paging, ordering));
		
		if(null == withoutDetail || withoutDetail == false)
			getDomainVariableAndResource(page_domains.getContent());
		
		return ResponseEntity.ok(page_domains);
	}
	
	public Domain getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		Domain domain = null;
		try {
			domain = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == domain)
			throw new IllegalArgumentException("Domain UID does not exist!(" + uid + ")");
		
		if(null == withoutDetail || withoutDetail == false)
			getDomainVariableAndResource(domain);
		
		return domain;
	}
	
	public Domain add(Domain domain) throws IllegalArgumentException, Exception{
		domain.setDomainuid(UUID.randomUUID().toString());
		
		String domainname = domain.getName();
		if(null == domainname || domainname.trim().isEmpty())
			throw new IllegalArgumentException("Domain Name can not be empty!");
		domain.setName(domainname.toUpperCase());
		
		if(this.dao.existByName(domain.getName()))
			throw new IllegalArgumentException("Duplicate Domain Name!");
		
		if(null == domain.getDescription())
			domain.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		domain.setLastupdatetime(new Date());
		
		this.dao.save(domain);
		
		List<DomainVariable> vars = domain.getDomainVars();
		if(null != vars && vars.size() > 0) {
			int[] returnValue = this.varService.addBatch(domain.getDomainuid(), vars);
			for(int i=0; i<returnValue.length; i++) {//重設Domain Vars, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					vars.remove(i);
				}
			}
			domain.setDomainVars(vars);
		}
		
		List<DomainResource> resources = domain.getDomainResources();
		if(null != resources && resources.size() > 0) {
			int[] returnValue = this.resourceService.addBatch(domain.getDomainuid(), resources);
			for(int i=0; i<returnValue.length; i++) {//重設Domain Resources, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					resources.remove(i);
				}
			}
		}else {
			DomainResource resource = new DomainResource();
			resource.setDomainuid(domain.getDomainuid());
			resource.setResourcename("TD-BULKLOAD");
			resource.setResourcevalue("1");
			this.resourceService.add(resource);
			resources.add(resource);
		}
		domain.setDomainResources(resources);
		
		return domain;
	}
	
	public Domain edit(Domain domain) throws IllegalArgumentException, Exception{
		String domainuid = domain.getDomainuid();
		if(null == domainuid || domainuid.trim().isEmpty())
			throw new IllegalArgumentException("Domain Uid can not be empty!");

		Domain old_domain = null;
		try {
			old_domain = this.dao.findById(domainuid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_domain)
			throw new IllegalArgumentException("Domain Uid does not exist!(" + domainuid + ")");
		
		String domainname = domain.getName();
		if(null == domainname || domainname.trim().isEmpty())
			throw new IllegalArgumentException("Domain Name can not be empty!");
		domain.setName(domainname.toUpperCase());
		
		if(this.dao.existByName(domain.getName()) && !old_domain.getName().equalsIgnoreCase(domain.getName()))
			throw new IllegalArgumentException("Duplicate Domain Name!");
		
		if(null == domain.getDescription())
			domain.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		domain.setLastupdatetime(new Date());
		
		this.dao.save(domain);
		
		List<DomainVariable> vars = domain.getDomainVars();
		if(null != vars && vars.size() > 0) {
			this.varService.deleteByDomainUid(domain.getDomainuid());
			int[] returnValue = this.varService.addBatch(domain.getDomainuid(), vars);
			for(int i=0; i<returnValue.length; i++) {//重設Domain Vars, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					vars.remove(i);
				}
			}
			domain.setDomainVars(vars);
		}
		
		List<DomainResource> resources = domain.getDomainResources();
		this.resourceService.deleteByDomainUid(domain.getDomainuid());
		if(null != resources && resources.size() > 0) {
			int[] returnValue = this.resourceService.addBatch(domain.getDomainuid(), resources);
			for(int i=0; i<returnValue.length; i++) {//重設Domain Resources, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					resources.remove(i);
				}
			}
		}else {
			DomainResource resource = new DomainResource();
			resource.setDomainuid(domain.getDomainuid());
			resource.setResourcename("TD-BULKLOAD");
			resource.setResourcevalue("1");
			this.resourceService.add(resource);
			resources.add(resource);
		}
		domain.setDomainResources(resources);
		
		return domain;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Domain Uid can not be empty!");
		
		if(uid.trim().equalsIgnoreCase("default"))
			throw new IllegalArgumentException("Default Domain can not be removed!");
		
		if(uid.trim().equalsIgnoreCase("global"))
			throw new IllegalArgumentException("Global Domain can not be removed!");
		
		if(jobService.existByDomainuid(uid)) {
			throw new IllegalArgumentException("Referenceing by job");
		}else if(objectAliasService.existByObjectuid(uid)) {
			throw new IllegalArgumentException("Referenceing by Object Alias");
		}else {
			resourceService.deleteByDomainUid(uid);
			varService.deleteByDomainUid(uid);
			this.dao.deleteById(uid);
		}
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}
	
	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception{
		if(paging.getNumber() == null)
			paging.setNumber(0);
		
		if(paging.getSize() == null)
			paging.setSize(10);
		
		if(ordering != null) {
			return PageRequest.of(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return PageRequest.of(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromString("DESC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromString(ordering.getOrderType());
		
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "lastupdatetime");
	}
	
	private void getDomainVariableAndResource(List<Domain> ds) throws Exception {
		for(Domain d : ds) {
			getDomainVariableAndResource(d);
		}
	}
	
	private void getDomainVariableAndResource(Domain d) throws Exception {
		d.setDomainVars(this.varService.getByDomainUid(d.getDomainuid()));
		d.setDomainResources(this.resourceService.getByDomainUid(d.getDomainuid()));
	}
}
