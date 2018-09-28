package com.netpro.trinity.resource.admin.job.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.dto.Ordering;
import com.netpro.trinity.resource.admin.dto.Paging;
import com.netpro.trinity.resource.admin.job.dao.BusentityJPADao;
import com.netpro.trinity.resource.admin.job.entity.Busentity;
import com.netpro.trinity.resource.admin.objectalias.service.ObjectAliasService;
import com.netpro.trinity.resource.admin.util.Constant;

@Service
public class BusentityService {
	@Autowired
	private BusentityJPADao dao;
	
	@Autowired
	private ObjectAliasService oaService;
	
	public List<Busentity> getAll(Boolean withAlias) throws Exception{
		List<Busentity> entities = this.dao.findAll();
		if(null != withAlias && withAlias == true)
			getAlias(entities);
		return entities;
	}
	
	public Busentity getByUid(Boolean withAlias, String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Business Entity UID can not be empty!");
		
		Busentity entity = null;
		try {
			entity = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(entity == null)
			throw new IllegalArgumentException("Business Entity UID does not exist!(" + uid + ")");
		
		if(null != withAlias && withAlias == true)
			getAlias(entity);
		
		return entity;
	}
	
	public ResponseEntity<?> getByFilter(Boolean withAlias, FilterInfo filter) throws Exception{
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "busentityname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<Busentity> page_entity = this.dao.findByBusentitynameLikeIgnoreCase(param, getPagingAndOrdering(paging, ordering));
		
		if(null != withAlias && withAlias == true)
			getAlias(page_entity.getContent());
		
		return ResponseEntity.ok(page_entity);
	}
	
	public Busentity add(Busentity entity) throws IllegalArgumentException, Exception{
		entity.setBusentityuid(UUID.randomUUID().toString());
		
		String entityname = entity.getBusentityname();
		if(null == entityname || entityname.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Name can not be empty!");
		entity.setBusentityname(entityname.toUpperCase());
		
		if(this.dao.existByName(entity.getBusentityname()))
			throw new IllegalArgumentException("Duplicate Business Entity Name!");
				
		if(null == entity.getDescription())
			entity.setDescription("");
		
		entity.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		entity.setLastupdatetime(new Date());
				
		return this.dao.save(entity);
	}
	
	public Busentity edit(Busentity entity) throws IllegalArgumentException, Exception{
		String entityuid = entity.getBusentityuid();
		if(null == entityuid || entityuid.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Uid can not be empty!");

		Busentity old_entity = null;
		try {
			old_entity = this.dao.findById(entityuid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_entity)
			throw new IllegalArgumentException("Business Entity Uid does not exist!(" + entityuid + ")");
		
		String entityname = entity.getBusentityname();
		if(null == entityname || entityname.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Name can not be empty!");
		entity.setBusentityname(entityname.toUpperCase());
		
		if(this.dao.existByName(entity.getBusentityname()) && !old_entity.getBusentityname().equalsIgnoreCase(entity.getBusentityname()))
			throw new IllegalArgumentException("Duplicate Business Entity Name!");
		
		if(null == entity.getDescription())
			entity.setDescription("");
		
		entity.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		entity.setLastupdatetime(new Date());
						
		return this.dao.save(entity);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Business Entity Uid can not be empty!");
		
		this.dao.deleteById(uid);
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
	
	private void getAlias(List<Busentity> entities) throws Exception {
		for(Busentity entity : entities) {
			getAlias(entity);
		}
	}
	
	private void getAlias(Busentity entity) throws Exception {
		entity.setAlias(this.oaService.getExByParentUid(entity.getBusentityuid()));
	}
}
