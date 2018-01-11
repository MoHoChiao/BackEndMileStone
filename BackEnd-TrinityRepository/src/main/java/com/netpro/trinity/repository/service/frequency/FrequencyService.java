package com.netpro.trinity.repository.service.frequency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jpa.frequency.FrequencyJPADao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.entity.connection.jpa.Connection;
import com.netpro.trinity.repository.entity.frequency.jpa.Frequency;
import com.netpro.trinity.repository.util.Constant;

@Service
public class FrequencyService {
	public static final String[] FREQ_FIELD_VALUES = new String[] {"frequencyname", "activate", "description"};
	public static final Set<String> FREQ_FIELD_SET = new HashSet<>(Arrays.asList(FREQ_FIELD_VALUES));
	
	@Autowired
	FrequencyJPADao dao;
	
	@Autowired
	FrequencyRelationService relService;
	
	public List<Frequency> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public List<Frequency> getAllWithoutInCategory() throws Exception{
		List<Frequency> freqs = this.dao.findByFrequencyuidNotIn(relService.getAllFrequencyUids(), new Sort(new Order("frequencyname")));
		return freqs;
	}
	
	public Frequency getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		Frequency freq = this.dao.findOne(uid);
		if(freq == null)
			throw new IllegalArgumentException("Frequency UID does not exist!(" + uid + ")");
		
		return freq;
	}
	
	public List<Frequency> getByName(String name) throws IllegalArgumentException, Exception{
		if(name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Frequency Name can not be empty!");
		
		return this.dao.findByfrequencyname(name.toUpperCase());
	}
	
	public List<Frequency> getByCategoryUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Frequency Category UID can not be empty!");
		
		List<Frequency> freqs = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(uid), new Sort(new Order("frequencyname")));
		return freqs;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null) {
			List<Frequency> freqs;
			if(categoryUid == null) {
				freqs = this.dao.findAll();
			}else {
				if(categoryUid.trim().isEmpty()) {
					freqs = this.dao.findByFrequencyuidNotIn(relService.getAllFrequencyUids());
				}else {
					freqs = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(categoryUid));
				}
			}
			return ResponseEntity.ok(freqs);
		}
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<Frequency> freqs;
			if(categoryUid == null) {
				freqs = this.dao.findAll();
			}else {
				if(categoryUid.trim().isEmpty()) {
					freqs = this.dao.findByFrequencyuidNotIn(relService.getAllFrequencyUids());
				}else {
					freqs = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(categoryUid));
				}
			}
			return ResponseEntity.ok(freqs);
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
				Page<Frequency> page_freq;
				if(categoryUid == null) {
					page_freq = this.dao.findAll(pageRequest);
				}else {
					if(categoryUid.trim().isEmpty()) {
						page_freq = this.dao.findByFrequencyuidNotIn(relService.getAllFrequencyUids(), pageRequest);
					}else {
						page_freq = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(categoryUid), pageRequest);
					}
				}
				
				return ResponseEntity.ok(page_freq);
			}else if(sort != null) {
				List<Frequency> freqs;
				if(categoryUid == null) {
					freqs = this.dao.findAll(sort);
				}else {
					if(categoryUid.trim().isEmpty()) {
						freqs = this.dao.findByFrequencyuidNotIn(relService.getAllFrequencyUids(), sort);
					}else {
						freqs = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(categoryUid), sort);
					}
				}
				return ResponseEntity.ok(freqs);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<Frequency> freqs;
				if(categoryUid == null) {
					freqs = this.dao.findAll();
				}else {
					if(categoryUid.trim().isEmpty()) {
						freqs = this.dao.findByFrequencyuidNotIn(relService.getAllFrequencyUids());
					}else {
						freqs = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(freqs);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !FREQ_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ FREQ_FIELD_SET.toString());
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
			if(categoryUid != null) {
				if(categoryUid.trim().isEmpty()) {
					methodName.append("AndFrequencyuidNotIn");
				}else {
					methodName.append("AndFrequencyuidIn");
				}
			}
			
			Method method = null;
			if(pageRequest != null){
				Page<Frequency> page_freq;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class);
					page_freq = (Page<Frequency>) method.invoke(this.dao, queryString, pageRequest);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Pageable.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						page_freq = (Page<Frequency>) method.invoke(this.dao, queryString, pageRequest, relService.getAllFrequencyUids());
					}else {
						page_freq = (Page<Frequency>) method.invoke(this.dao, queryString, pageRequest, relService.getFrequencyUidsByCategoryUid(categoryUid));
					}
				}
				
				return ResponseEntity.ok(page_freq);
			}else if(sort != null) {
				List<Frequency> freqs;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
					freqs = (List<Frequency>) method.invoke(this.dao, queryString, sort);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						freqs = (List<Frequency>) method.invoke(this.dao, queryString, sort, relService.getAllFrequencyUids());
					}else {
						freqs = (List<Frequency>) method.invoke(this.dao, queryString, sort, relService.getFrequencyUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(freqs);
			}else {
				List<Frequency> freqs;
				if(categoryUid == null) {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class);
					freqs = (List<Frequency>) method.invoke(this.dao, queryString);
				}else {
					method = this.dao.getClass().getMethod(methodName.toString(), String.class, List.class);
					if(categoryUid.trim().isEmpty()) {
						freqs = (List<Frequency>) method.invoke(this.dao, queryString, relService.getAllFrequencyUids());
					}else {
						freqs = (List<Frequency>) method.invoke(this.dao, queryString, relService.getFrequencyUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(freqs);
			}
		}
	}
	
	public Connection add(String categoryUid, Map<String, String> connMap) throws IllegalArgumentException, Exception{
//		if(null == connMap || connMap.size() <= 0)
//			throw new IllegalArgumentException("Connection Information can not be empty!");
//		
//		String newUid = UUID.randomUUID().toString();
//		
//		String connectionname = connMap.get("connectionname");
//		if(null == connectionname || connectionname.trim().length() <= 0)
//			throw new IllegalArgumentException("Connection Name can not be empty!");
//		connectionname = connectionname.toUpperCase();
//		
//		if(this.dao.existByName(connectionname))
//			throw new IllegalArgumentException("Duplicate Connection Name!");
//		
//		String description = connMap.get("description");
//		if(null == description)
//			description = "";
//		
//		String connectiontype = connMap.get("connectiontype");
//		if(null == connectiontype || connectiontype.trim().isEmpty() || !ConnectionService.CONNECTION_TYPE_SET.contains(connectiontype))
//			throw new IllegalArgumentException("Illegal connection type! "+ ConnectionService.CONNECTION_TYPE_SET.toString());
//		
//		Connection conn = new Connection();
//		conn.setConnectionuid(newUid);
//		conn.setConnectionname(connectionname);
//		conn.setConnectiontype(connectiontype);
//		conn.setDescription(description);
//		setExtraXmlProp(conn, connMap);
//		conn.setLastupdatetime(new Date());
//		
//		Connection new_conn = this.dao.save(conn);
//		
//		new_conn = getExtraXmlProp(new_conn);
//		
//		//如果所附帶的url參數中有categoryUid的話, 表示是要把Connection新增至某個category
//		if(categoryUid != null && !categoryUid.trim().equals("")) {
//			ConnectionRelation rel = new ConnectionRelation();
//			rel.setConncategoryuid(categoryUid);
//			rel.setConnectionuid(new_conn.getConnectionuid());
//			this.relService.add(rel);
//		}
//		
//		return new_conn;
		
		return null;
	}
	
	public Connection edit(String categoryUid, Map<String, String> connMap) throws IllegalArgumentException, Exception{
//		String connectionuid = connMap.get("connectionuid");
//		if(null == connectionuid || connectionuid.trim().length() <= 0)
//			throw new IllegalArgumentException("Connection Uid can not be empty!");
//		
//		Connection old_conn = this.dao.findOne(connectionuid);
//		if(null == old_conn)
//			throw new IllegalArgumentException("Connection Uid does not exist!(" + connectionuid + ")");
//		
//		String connectionname = connMap.get("connectionname");
//		if(null == connectionname || connectionname.trim().length() <= 0)
//			throw new IllegalArgumentException("Connection Name can not be empty!");
//		connectionname = connectionname.toUpperCase();
//		
//		if(this.dao.existByName(connectionname) && !old_conn.getConnectionname().equalsIgnoreCase(connectionname))
//			throw new IllegalArgumentException("Duplicate Connection Name!");
//		
//		String description = connMap.get("description");
//		if(null == description)
//			description = "";
//		
//		String connectiontype = connMap.get("connectiontype");
//		if(null == connectiontype || connectiontype.trim().isEmpty() || !ConnectionService.CONNECTION_TYPE_SET.contains(connectiontype))
//			throw new IllegalArgumentException("Illegal connection type! "+ ConnectionService.CONNECTION_TYPE_SET.toString());
//		
//		Connection conn = new Connection();
//		conn.setConnectionuid(connectionuid);
//		conn.setConnectionname(connectionname);
//		conn.setConnectiontype(connectiontype);
//		conn.setDescription(description);
//		setExtraXmlProp(conn, connMap);
//		conn.setLastupdatetime(new Date());
//		
//		Connection new_conn = this.dao.save(conn);
//		
//		new_conn = getExtraXmlProp(new_conn);
//		
//		//如果所附帶的url參數中有categoryUid的話, 表示是要把Connection編輯至某個category或root
//		if(categoryUid != null) {
//			this.relService.deleteByConnectionUid(new_conn.getConnectionuid());
//			if(!categoryUid.trim().equals("")) {	//如果categoryUid不是空值, 表示是要把Connection編輯到某一個category底下
//				ConnectionRelation rel = new ConnectionRelation();
//				rel.setConncategoryuid(categoryUid);
//				rel.setConnectionuid(new_conn.getConnectionuid());
//				this.relService.add(rel);
//			}
//		}
//		
//		return new_conn;
		return null;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
//		if(null == uid || uid.trim().length() <= 0)
//			throw new IllegalArgumentException("Connection Uid can not be empty!");
//		
//		if(filesourceService.existByConnectionuid(uid))
//			throw new IllegalArgumentException("Referenced By File Source!");
//		
//		if(jobstepService.existByConnectionuid(uid))
//			throw new IllegalArgumentException("Referenced By JobStep!");
//		
//		this.dao.delete(uid);
//		this.relService.deleteByConnectionUid(uid);
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.exists(uid);
	}
	
	public boolean existByWCalendaruid(String wcalendaruid) throws IllegalArgumentException, Exception{
		if(null == wcalendaruid || wcalendaruid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		
		return this.dao.existByWCalendaruid(wcalendaruid);
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
}
