package com.netpro.trinity.service.member.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.dto.Querying;
import com.netpro.trinity.service.member.dao.RoleJPADao;
import com.netpro.trinity.service.member.entity.Role;
import com.netpro.trinity.service.util.Constant;

@Service
public class RoleService {
	public static final String[] ROLE_FIELD_VALUES = new String[] { "rolename", "description"};
	public static final Set<String> ROLE_FIELD_SET = new HashSet<>(Arrays.asList(ROLE_FIELD_VALUES));
	
	@Autowired
	private RoleJPADao dao;
	
	@Autowired
	private RoleMemberService memberService;
	
	public List<Role> getAll() throws Exception{
		List<Role> roles = this.dao.findAll();
		return roles;
	}
	
	public Role getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Role UID can not be empty!");
		
		Role role = null;
		try {
			role = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == role)
			throw new IllegalArgumentException("Role UID does not exist!(" + uid + ")");
		return role;
	}
	
	public List<Role> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Role Name can not be empty!");
		
		return this.dao.findByrolename(name);
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<Role> roles = this.dao.findAll();
			return ResponseEntity.ok(roles);
		}
			
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<Role> roles = this.dao.findAll();
			return ResponseEntity.ok(roles);
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
				Page<Role> page_role = this.dao.findAll(pageRequest);
				return ResponseEntity.ok(page_role);
			}else if(sort != null) {
				List<Role> roles = this.dao.findAll(sort);
				return ResponseEntity.ok(roles);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Role> roles = this.dao.findAll(sort);
				return ResponseEntity.ok(roles);
			}
		}else {
			if(null == querying.getQueryType() || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(null == querying.getQueryField() || !ROLE_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ ROLE_FIELD_SET.toString());
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
				Page<Role> page_role = (Page<Role>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_role);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<Role> roles = (List<Role>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(roles);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<Role> roles = (List<Role>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(roles);
			}
		}
	}
	
	public Role add(Role role) throws IllegalArgumentException, Exception{
		role.setRoleuid(UUID.randomUUID().toString());
		
		String name = role.getRolename();
		if(null == name || name.trim().isEmpty())
			throw new IllegalArgumentException("Role Name can not be empty!");
		
		if(this.dao.existByName(role.getRolename()))
			throw new IllegalArgumentException("Duplicate Role Name!");
		
		if(null == role.getDescription())
			role.setDescription("");
		
		if(null == role.getHomedir())
			role.setHomedir("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		role.setLastupdatetime(new Date());
				
		return this.dao.save(role);
	}
	
	public Role edit(Role role) throws IllegalArgumentException, Exception{
		String uid = role.getRoleuid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Role Uid can not be empty!");
		
		Role old_role = null;
		try {
			old_role = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_role)
			throw new IllegalArgumentException("Role Uid does not exist!(" + uid + ")");
		
		String name = role.getRolename();
		if(null == name || name.trim().isEmpty())
			throw new IllegalArgumentException("Role Name can not be empty!");
		
		if(this.dao.existByName(role.getRolename()) && !old_role.getRolename().equalsIgnoreCase(role.getRolename()))
			throw new IllegalArgumentException("Duplicate Role Name!");
		
		if(null == role.getDescription())
			role.setDescription("");
		
		if(null == role.getHomedir())
			role.setHomedir("");
		
		if(uid.trim().startsWith("Role1")) {
			role.setRolename("Administrator");
			role.setDescription("Administrator");
		}else if(uid.trim().startsWith("Role2")) {
			role.setRolename("Developer User");
			role.setDescription("Developer User");
		}else if(uid.trim().startsWith("Role3")) {
			role.setRolename("Operator User");
			role.setDescription("Operator User");
		}else if(uid.trim().startsWith("Role4")) {
			role.setRolename("End User");
			role.setDescription("End User");
		}
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		role.setLastupdatetime(new Date());
						
		return this.dao.save(role);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Role Uid can not be empty!");
		
		if(uid.trim().startsWith("Role"))
			throw new IllegalArgumentException("You cannot delete default roles!");
		
		this.memberService.deleteByRoleUid(uid);
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
		
		if(ordering.getOrderField() != null && ROLE_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "rolename");
	}
}
