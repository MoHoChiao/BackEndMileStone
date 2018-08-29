package com.netpro.trinity.service.member.service;

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

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.member.dao.UsergroupJPADao;
import com.netpro.trinity.service.member.entity.Usergroup;
import com.netpro.trinity.service.util.Constant;

@Service
public class UsergroupService {
	@Autowired
	private UsergroupJPADao dao;
	
	@Autowired
	private GroupMemberService memberService;
	
	public List<Usergroup> getAll() throws Exception{
		List<Usergroup> groups = this.dao.findAll();
		return groups;
	}
	
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws Exception{
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == paging) 
			paging = new Paging(0, 20);
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "groupname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		Page<Usergroup> page_groups = this.dao.findByGroupnameLikeIgnoreCase(param, getPagingAndOrdering(paging, ordering));
		
		return ResponseEntity.ok(page_groups);
	}
	
	public Usergroup getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("User Group UID can not be empty!");
		
		Usergroup group = null;
		try {
			group = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == group)
			throw new IllegalArgumentException("User Group UID does not exist!(" + uid + ")");
		return group;
	}
	
	public Usergroup add(Usergroup group) throws IllegalArgumentException, Exception{
		group.setGroupuid(UUID.randomUUID().toString());
		
		String name = group.getGroupname();
		if(null == name || name.trim().isEmpty())
			throw new IllegalArgumentException("User Group Name can not be empty!");
		
		if(this.dao.existByName(group.getGroupname()))
			throw new IllegalArgumentException("Duplicate User Group Name!");
		
		if(null == group.getDescription())
			group.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		group.setLastupdatetime(new Date());
				
		return this.dao.save(group);
	}
	
	public Usergroup edit(Usergroup group) throws IllegalArgumentException, Exception{
		String uid = group.getGroupuid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("User Group Uid can not be empty!");

		Usergroup old_group = null;
		try {
			old_group = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_group)
			throw new IllegalArgumentException("User Group Uid does not exist!(" + uid + ")");
		
		String name = group.getGroupname();
		if(null == name || name.trim().isEmpty())
			throw new IllegalArgumentException("User Group Name can not be empty!");
		
		if(this.dao.existByName(group.getGroupname()) && !old_group.getGroupname().equalsIgnoreCase(group.getGroupname()))
			throw new IllegalArgumentException("Duplicate User Group Name!");
		
		if(null == group.getDescription())
			group.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		group.setLastupdatetime(new Date());
						
		return this.dao.save(group);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("User Group Uid can not be empty!");
		
		this.memberService.deleteByGroupUid(uid);
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
			return Sort.by(direct, "groupname");
	}
}
