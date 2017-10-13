package com.netpro.trinity.repository.service;

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

import com.netpro.trinity.repository.dao.VRAgentDao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.entity.VRAgent;
import com.netpro.trinity.repository.util.Constant;

@Service
public class VRAgentService {
	public static final String[] VR_AGENT_FIELD_VALUES = new String[] { "virtualagentname", "activate", "description", "mode"};
	public static final Set<String> VR_AGENT_FIELD_SET = new HashSet<>(Arrays.asList(VR_AGENT_FIELD_VALUES));
	
	@Autowired
	private VRAgentDao dao;
	
	public List<VRAgent> getAllVRAgent() throws Exception{
		return this.dao.findAll();
	}
	
	public VRAgent getVRAgentById(String id) throws IllegalArgumentException, Exception{
		if(id == null || id.isEmpty())
			throw new IllegalArgumentException("Virtual Agent UID can not be empty!");
		
		VRAgent vrAgent = this.dao.findOne(id);
		if(vrAgent == null)
			throw new IllegalArgumentException("Virtual Agent UID does not exist!");
		
		return vrAgent;
	}
	
	public List<VRAgent> getVRAgentByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Virtual Agent Name can not be empty!");
		
		return this.dao.findByvirtualagentname(name);
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getVRAgentByFieldQuery(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null) 
			return ResponseEntity.ok(this.dao.findAll());
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null)
			return ResponseEntity.ok(this.dao.findAll());
		
		PageRequest pageRequest = null;
		Sort sort = null;
		
		if(paging != null) {
			pageRequest = getPagingAndOrdering(paging, ordering);
		}else {
			if(ordering != null) {
				sort = getOrdering(ordering);
			}
		}
		
		if(querying == null) {
			if(pageRequest != null) {
				return ResponseEntity.ok(this.dao.findAll(pageRequest));
			}else if(sort != null) {
				return ResponseEntity.ok(this.dao.findAll(sort));
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				return ResponseEntity.ok(this.dao.findAll());
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !VR_AGENT_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ VR_AGENT_FIELD_SET.toString());
			if(querying.getIgnoreCase() == null)
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
				Page<VRAgent> page_vragent = (Page<VRAgent>) method.invoke(this.dao, queryString, pageRequest);
				return ResponseEntity.ok(page_vragent);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<VRAgent> vragents = (List<VRAgent>) method.invoke(this.dao, queryString, sort);
				return ResponseEntity.ok(vragents);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<VRAgent> vragents = (List<VRAgent>) method.invoke(this.dao, queryString);
				return ResponseEntity.ok(vragents);
			}
		}
	}
	
	public VRAgent addVRAgent(VRAgent vragent) throws IllegalArgumentException, Exception{
		vragent.setVirtualagentuid(UUID.randomUUID().toString());
		
		String vr_agentname = vragent.getVirtualagentname();
		if(null == vr_agentname || vr_agentname.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Name can not be empty!");
		vragent.setVirtualagentname(vr_agentname.toUpperCase());
		
		if(this.dao.existByName(vragent.getVirtualagentname()))
			throw new IllegalArgumentException("Duplicate Virtual Agent Name!");
		
		String activate = vragent.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Virtual Agent activate value can only be 1 or 0!");
		
		String mode = vragent.getMode();
		if(null == mode || (!mode.equals("1") && !mode.equals("0")))
			throw new IllegalArgumentException("Virtual Agent mode value can only be 1 or 0!");
		
		if(null == vragent.getDescription())
			vragent.setDescription("");
		
		if(null == vragent.getMaximumjob())
			throw new IllegalArgumentException("Virtual Agent Maximum Job value can only be small integer!");
		
		vragent.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		vragent.setLastupdatetime(new Date());
				
		return this.dao.save(vragent);
	}
	
	public VRAgent editVRAgent(VRAgent vragent) throws IllegalArgumentException, Exception{
		String vr_agentuid = vragent.getVirtualagentuid();
		if(null == vr_agentuid || vr_agentuid.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Uid can not be empty!");

		VRAgent old_vragent = getVRAgentById(vragent.getVirtualagentuid());
		if(null == old_vragent)
			throw new IllegalArgumentException("Virtual Agent Uid does not exist!");
		
		String vr_agentname = vragent.getVirtualagentname();
		if(null == vr_agentname || vr_agentname.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Name can not be empty!");
		vragent.setVirtualagentname(vr_agentname.toUpperCase());
		
		if(this.dao.existByName(vragent.getVirtualagentname()) && !old_vragent.getVirtualagentname().equalsIgnoreCase(vragent.getVirtualagentname()))
			throw new IllegalArgumentException("Duplicate Virtual Agent Name!");
		
		String activate = vragent.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Virtual Agent activate value can only be 1 or 0!");
		
		String mode = vragent.getMode();
		if(null == mode || (!mode.equals("1") && !mode.equals("0")))
			throw new IllegalArgumentException("Virtual Agent mode value can only be 1 or 0!");
		
		if(null == vragent.getDescription())
			vragent.setDescription("");
		
		if(null == vragent.getMaximumjob())
			throw new IllegalArgumentException("Virtual Agent Maximum Job can not be empty!");
		
		vragent.setXmldata("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		vragent.setLastupdatetime(new Date());
				
		return this.dao.save(vragent);
	}
	
	public void deleteVRAgent(String vragentuid) throws IllegalArgumentException, Exception{
		if(null == vragentuid || vragentuid.trim().length() <= 0)
			throw new IllegalArgumentException("Virtual Agent Uid can not be empty!");
		
		this.dao.delete(vragentuid);
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
		if(ordering.getOrderField() != null && VR_AGENT_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
}
