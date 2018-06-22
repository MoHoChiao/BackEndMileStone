package com.netpro.trinity.service.member.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.dto.Querying;
import com.netpro.trinity.service.member.dao.UsergroupJPADao;
import com.netpro.trinity.service.member.entity.Usergroup;
import com.netpro.trinity.service.util.Constant;

@Service
public class UsergroupService {
	public static final String[] GROUP_FIELD_VALUES = new String[] { "groupname", "description"};
	public static final Set<String> GROUP_FIELD_SET = new HashSet<>(Arrays.asList(GROUP_FIELD_VALUES));
	
	@Autowired
	private UsergroupJPADao dao;
	
	@Autowired
	private GroupMemberService memberService;
	
	public List<Usergroup> getAll() throws Exception{
		List<Usergroup> groups = this.dao.findAll();
		return groups;
	}
	
	public Usergroup getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("User Group UID can not be empty!");
		
		Usergroup group = this.dao.findById(uid).get();
		if(null == group)
			throw new IllegalArgumentException("User Group UID does not exist!(" + uid + ")");
		return group;
	}
	
	public List<Usergroup> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("User Group Name can not be empty!");
		
		return this.dao.findBygroupname(name);
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<Usergroup> groups = this.dao.findAll();
			return ResponseEntity.ok(groups);
		}
			
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<Usergroup> groups = this.dao.findAll();
			return ResponseEntity.ok(groups);
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
				Page<Usergroup> page_group = this.dao.findAll(pageRequest);
				return ResponseEntity.ok(page_group);
			}else if(sort != null) {
				List<Usergroup> groups = this.dao.findAll(sort);
				return ResponseEntity.ok(groups);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Usergroup> groups = this.dao.findAll(sort);
				return ResponseEntity.ok(groups);
			}
		}else {
			if(null == querying.getQueryType() || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(null == querying.getQueryField() || !GROUP_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ GROUP_FIELD_SET.toString());
			if(null == querying.getIgnoreCase())
				querying.setIgnoreCase(false);
			
			String queryType = querying.getQueryType().toLowerCase();
			String queryField = querying.getQueryField().toLowerCase(); //Must be lower case for jpa method
			String queryString = querying.getQueryString();
			
			StringBuffer methodName = new StringBuffer("findBy");
			methodName.append(queryField);
			if(queryType.equals("like")) {
				methodName.append("Like");
				queryString = "%" + queryString + "%";
			}
			if(querying.getIgnoreCase()) {
				methodName.append("IgnoreCase");
			}	

			Method method = null;
			if(pageRequest != null){
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
				Page<Usergroup> page_group = (Page<Usergroup>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_group);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Usergroup> groups = (List<Usergroup>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(groups);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Usergroup> groups = (List<Usergroup>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(groups);
			}
		}
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

		Usergroup old_group = this.dao.findById(uid).get();
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
		
		if(ordering.getOrderField() != null && GROUP_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "groupname");
	}
}
