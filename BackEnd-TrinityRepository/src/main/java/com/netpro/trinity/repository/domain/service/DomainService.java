package com.netpro.trinity.repository.domain.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.domain.dao.DomainJPADao;
import com.netpro.trinity.repository.domain.entity.Domain;
import com.netpro.trinity.repository.domain.entity.DomainResource;
import com.netpro.trinity.repository.domain.entity.DomainVariable;
import com.netpro.trinity.repository.dto.inquire.FilterInfo;
import com.netpro.trinity.repository.dto.inquire.Ordering;
import com.netpro.trinity.repository.dto.inquire.Paging;
import com.netpro.trinity.repository.dto.inquire.Querying;
import com.netpro.trinity.repository.job.service.JobService;
import com.netpro.trinity.repository.objectalias.service.ObjectAliasService;
import com.netpro.trinity.repository.util.Constant;

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
	
	public Domain getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Domain UID can not be empty!");
		
		Domain domain = this.dao.findOne(uid);
		if(null == domain)
			throw new IllegalArgumentException("Domain UID does not exist!(" + uid + ")");
		
		if(null == withoutDetail || withoutDetail == false)
			getDomainVariableAndResource(domain);
		
		return domain;
	}
	
	public List<Domain> getByName(Boolean withoutDetail, String name) throws IllegalArgumentException, Exception{
		if(null == name || name.isEmpty())
			throw new IllegalArgumentException("Domain Name can not be empty!");
		
		List<Domain> domains = this.dao.findByname(name.toUpperCase());
		
		if(null == withoutDetail || withoutDetail == false)
			getDomainVariableAndResource(domains);
		
		return domains;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(Boolean withoutDetail, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<Domain> domains = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
				getDomainVariableAndResource(domains);
			return ResponseEntity.ok(domains);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<Domain> domains = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
				getDomainVariableAndResource(domains);
			return ResponseEntity.ok(domains);
		}
		
		PageRequest pageRequest = null;
		Sort sort = null;
		
		if(paging != null) {
			pageRequest = getPagingAndOrdering(paging, ordering);
		}else {
			if(ordering != null) {
				sort = getOrdering(ordering);
			}
		}
		
		if(null == querying) {
			if(pageRequest != null) {
				Page<Domain> page_domain = this.dao.findAll(pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getDomainVariableAndResource(page_domain.getContent());
				return ResponseEntity.ok(page_domain);
			}else if(sort != null) {
				List<Domain> domains = this.dao.findAll(sort);
				if(null == withoutDetail || withoutDetail == false)
					getDomainVariableAndResource(domains);
				return ResponseEntity.ok(domains);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Domain> domains = this.dao.findAll();
				if(null == withoutDetail || withoutDetail == false)
					getDomainVariableAndResource(domains);
				return ResponseEntity.ok(domains);
			}
		}else {
			if(null == querying.getQueryType() || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(null == querying.getQueryField() || !DOMAIN_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ DOMAIN_FIELD_SET.toString());
			if(null == querying.getIgnoreCase())
				querying.setIgnoreCase(false);
			
			String queryType = querying.getQueryType().toLowerCase();
			String queryField = querying.getQueryField().toLowerCase(); //Must be lower case for jpa method
			String queryString = querying.getQueryString();
			
			StringBuffer methodName = new StringBuffer("findBy");
			methodName.append(queryField);
			if(queryType.equals("like")) {
				if(!queryField.equals("port")) { //Integer Field can not be Like query
					methodName.append("Like");
					queryString = "%" + queryString + "%";
				}
			}
			if(querying.getIgnoreCase()) {
				if(!queryField.equals("port")) { //Integer Field can not be IgnoreCase query
					methodName.append("IgnoreCase");
				}
			}	

			Method method = null;
			if(pageRequest != null){
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
				Page<Domain> page_domain = (Page<Domain>) method.invoke(this.dao, queryString, pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getDomainVariableAndResource(page_domain.getContent());
				return ResponseEntity.ok(page_domain);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Domain> domains = (List<Domain>) method.invoke(this.dao, queryString, sort);
				if(null == withoutDetail || withoutDetail == false)
					getDomainVariableAndResource(domains);
				return ResponseEntity.ok(domains);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Domain> domains = (List<Domain>) method.invoke(this.dao, queryString);
				if(null == withoutDetail || withoutDetail == false)
					getDomainVariableAndResource(domains);
				return ResponseEntity.ok(domains);
			}
		}
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

		Domain old_domain = this.dao.findOne(domainuid);
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
			this.dao.delete(uid);
		}
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception{
		if(paging.getNumber() == null)
			paging.setNumber(0);
		
		if(paging.getSize() == null)
			paging.setSize(10);
		
		if(ordering != null) {
			return new PageRequest(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		}else {
			return new PageRequest(paging.getNumber(), paging.getSize());
		}
	}
	
	private Sort getOrdering(Ordering ordering) throws Exception{
		Direction direct = Direction.fromStringOrNull("DESC");
		if(ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromStringOrNull(ordering.getOrderType());
		
		Order order = new Order(direct, "lastupdatetime");
		if(ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
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
