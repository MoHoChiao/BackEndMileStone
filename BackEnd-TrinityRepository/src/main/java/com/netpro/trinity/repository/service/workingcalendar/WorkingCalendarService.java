package com.netpro.trinity.repository.service.workingcalendar;

import java.util.Arrays;
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

import com.netpro.trinity.repository.dao.jpa.workingcalendar.WorkingCalendarJPADao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.entity.workingcalendar.jpa.WorkingCalendar;
import com.netpro.trinity.repository.util.Constant;

@Service
public class WorkingCalendarService {
	public static final String[] WC_FIELD_VALUES = new String[] { "wcalendarname", "activate", "description"};
	public static final Set<String> WC_FIELD_SET = new HashSet<>(Arrays.asList(WC_FIELD_VALUES));
	
	@Autowired
	private WorkingCalendarJPADao dao;
	
	@Autowired
	private WorkingCalendarListService listService;
	
	public List<WorkingCalendar> getAll() throws Exception{
		List<WorkingCalendar> wcs = this.dao.findAll();
		getWCList(wcs);
		return wcs;
	}
	
	public WorkingCalendar getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Working Calendar UID can not be empty!");
		
		WorkingCalendar wc = this.dao.findOne(uid);
		if(wc == null)
			throw new IllegalArgumentException("Working Calendar UID does not exist!(" + uid + ")");
		
		getWCList(wc);
		
		return wc;
	}
	
	public List<WorkingCalendar> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Working Calendar Name can not be empty!");
		
		List<WorkingCalendar> wcs = this.dao.findBywcalendarname(name.toUpperCase());
		
		getWCList(wcs);
		
		return wcs;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null) {
			List<WorkingCalendar> wcs = this.dao.findAll();
			getWCList(wcs);
			return ResponseEntity.ok(wcs);
		}
			
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<WorkingCalendar> wcs = this.dao.findAll();
			getWCList(wcs);
			return ResponseEntity.ok(wcs);
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
		
		if(querying == null) {
			if(pageRequest != null) {
				Page<WorkingCalendar> page_wc = this.dao.findAll(pageRequest);
				getWCList(page_wc.getContent());
				return ResponseEntity.ok(page_wc);
			}else if(sort != null) {
				List<WorkingCalendar> wcs = this.dao.findAll(sort);
				getWCList(wcs);
				return ResponseEntity.ok(wcs);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<WorkingCalendar> wcs = this.dao.findAll(sort);
				getWCList(wcs);
				return ResponseEntity.ok(wcs);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !WC_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ WC_FIELD_SET.toString());
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
				Page<WorkingCalendar> page_wc = (Page<WorkingCalendar>) method.invoke(this.dao, queryString, pageRequest);
				getWCList(page_wc.getContent());
				return ResponseEntity.ok(page_wc);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<WorkingCalendar> wcs = (List<WorkingCalendar>) method.invoke(this.dao, queryString, sort);
				getWCList(wcs);
				return ResponseEntity.ok(wcs);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<WorkingCalendar> wcs = (List<WorkingCalendar>) method.invoke(this.dao, queryString);
				getWCList(wcs);
				return ResponseEntity.ok(wcs);
			}
		}
	}
	
	public WorkingCalendar add(WorkingCalendar wc) throws IllegalArgumentException, Exception{
		wc.setWcalendaruid(UUID.randomUUID().toString());
		
		String wc_name = wc.getWcalendarname();
		if(null == wc_name || wc_name.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Name can not be empty!");
		wc.setWcalendarname(wc_name.toUpperCase());
		
		if(this.dao.existByName(wc.getWcalendarname()))
			throw new IllegalArgumentException("Duplicate Working Calendar Name!");
		
		String activate = wc.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Working Calendar activate value can only be 1 or 0!");
		
		if(null == wc.getDescription())
			wc.setDescription("");
		
		this.dao.save(wc);
		this.listService.deleteByWCUid(wc.getWcalendaruid());
		wc.setWcalendarlist(this.listService.add(wc.getWcalendaruid(), wc.getWcalendarlist())); //重設working calendar list, 只有插入成功的會留下來
		
		return wc;
	}
	
	public WorkingCalendar edit(WorkingCalendar wc) throws IllegalArgumentException, Exception{		
		String wc_uid = wc.getWcalendaruid();
		if(null == wc_uid || wc_uid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Uid can not be empty!");

		WorkingCalendar old_wc = this.dao.findOne(wc_uid);
		if(null == old_wc)
			throw new IllegalArgumentException("Working Calendar Uid does not exist!(" + wc_uid + ")");
		
		String wc_name = wc.getWcalendarname();
		if(null == wc_name || wc_name.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Name can not be empty!");
		wc.setWcalendarname(wc_name.toUpperCase());
		
		if(this.dao.existByName(wc.getWcalendarname()) && !old_wc.getWcalendarname().equalsIgnoreCase(wc.getWcalendarname()))
			throw new IllegalArgumentException("Duplicate Working Calendar Name!");
		
		String activate = wc.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Working Calendar activate value can only be 1 or 0!");
		
		if(null == wc.getDescription())
			wc.setDescription("");
		
		this.dao.save(wc);
		this.listService.deleteByWCUid(wc.getWcalendaruid());
		wc.setWcalendarlist(this.listService.add(wc.getWcalendaruid(), wc.getWcalendarlist())); //重設working calendar list, 只有插入成功的會留下來
		
		return wc;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		
		this.listService.deleteByWCUid(uid);
		this.dao.delete(uid);
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
		
		Order order = new Order(direct, "wcalendarname");
		if(ordering.getOrderField() != null && WC_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
	
	private void getWCList(List<WorkingCalendar> wcs) throws Exception {
		for(WorkingCalendar wc : wcs) {
			getWCList(wc);
		}
	}
	
	private void getWCList(WorkingCalendar wc) throws Exception {
		wc.setWcalendarlist(this.listService.getByWCUid(wc.getWcalendaruid()));
	}
}
