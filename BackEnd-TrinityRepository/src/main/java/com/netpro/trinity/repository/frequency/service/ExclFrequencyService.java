package com.netpro.trinity.repository.frequency.service;

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

import com.netpro.trinity.repository.dto.inquire.FilterInfo;
import com.netpro.trinity.repository.dto.inquire.Ordering;
import com.netpro.trinity.repository.dto.inquire.Paging;
import com.netpro.trinity.repository.dto.inquire.Querying;
import com.netpro.trinity.repository.filesource.entity.ExclFrequency;
import com.netpro.trinity.repository.frequency.dao.ExclFrequencyJPADao;
import com.netpro.trinity.repository.frequency.entity.ExclFrequencyList;
import com.netpro.trinity.repository.job.service.JobExcludeService;
import com.netpro.trinity.repository.job.service.JobFlowExcludeService;
import com.netpro.trinity.repository.util.Constant;

@Service
public class ExclFrequencyService {
	public static final String[] EXCL_FIELD_VALUES = new String[] { "excludefrequencyname", "activate", "description"};
	public static final Set<String> EXCL_FIELD_SET = new HashSet<>(Arrays.asList(EXCL_FIELD_VALUES));
	
	@Autowired
	private ExclFrequencyJPADao dao;
	
	@Autowired
	private ExclFrequencyListService listService;
	@Autowired
	private FreqExcludeService freqExcludeService;
	@Autowired
	private JobExcludeService jobExcludeService;
	@Autowired
	private JobFlowExcludeService flowExcludeService;
	
	public List<ExclFrequency> getAll(Boolean withoutDetail) throws Exception{
		List<ExclFrequency> excls = this.dao.findAll();
		if(null == withoutDetail || withoutDetail == false)
			getExclFreqList(excls);
		return excls;
	}
	
	public ExclFrequency getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency UID can not be empty!");
		
		ExclFrequency excl = this.dao.findOne(uid);
		if(null == excl) {
			if(uid.trim().equalsIgnoreCase("global")) {	//for global excl freq
				return excl;	//null即傳空字串回前端
			}else {
				throw new IllegalArgumentException("Exclude Frequency UID does not exist!(" + uid + ")");
			}
		}
			
		if(null == withoutDetail || withoutDetail == false)
			getExclFreqList(excl);
		
		return excl;
	}
	
	public ExclFrequency getByGlobal(Boolean withoutDetail) throws IllegalArgumentException, Exception{
		ExclFrequency excl = this.dao.findOne("global");
		if(null == excl) {
			return excl;
		}
			
		if(null == withoutDetail || withoutDetail == false)
			getExclFreqList(excl);
		
		return excl;
	}
	
	public List<ExclFrequency> getByName(Boolean withoutDetail, String name) throws IllegalArgumentException, Exception{
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("Exclude Frequency Name can not be empty!");
		
		List<ExclFrequency> excls = this.dao.findByexcludefrequencyname(name.toUpperCase());
		
		if(null == withoutDetail || withoutDetail == false)
			getExclFreqList(excls);
		
		return excls;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(Boolean withoutDetail, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(filter == null) {
			List<ExclFrequency> excls = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
				getExclFreqList(excls);
			return ResponseEntity.ok(excls);
		}
			
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(paging == null && ordering == null && querying == null) {
			List<ExclFrequency> excls = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
				getExclFreqList(excls);
			return ResponseEntity.ok(excls);
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
				Page<ExclFrequency> page_excl = this.dao.findAll(pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getExclFreqList(page_excl.getContent());
				return ResponseEntity.ok(page_excl);
			}else if(sort != null) {
				List<ExclFrequency> excls = this.dao.findAll(sort);
				if(null == withoutDetail || withoutDetail == false)
					getExclFreqList(excls);
				return ResponseEntity.ok(excls);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<ExclFrequency> excls = this.dao.findAll(sort);
				if(null == withoutDetail || withoutDetail == false)
					getExclFreqList(excls);
				return ResponseEntity.ok(excls);
			}
		}else {
			if(querying.getQueryType() == null || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(querying.getQueryField() == null || !EXCL_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ EXCL_FIELD_SET.toString());
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
				Page<ExclFrequency> page_excl = (Page<ExclFrequency>) method.invoke(this.dao, queryString, pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getExclFreqList(page_excl.getContent());
				return ResponseEntity.ok(page_excl);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<ExclFrequency> page_excl = (List<ExclFrequency>) method.invoke(this.dao, queryString, sort);
				if(null == withoutDetail || withoutDetail == false)
					getExclFreqList(page_excl);
				return ResponseEntity.ok(page_excl);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<ExclFrequency> page_excl = (List<ExclFrequency>) method.invoke(this.dao, queryString);
				if(null == withoutDetail || withoutDetail == false)
					getExclFreqList(page_excl);
				return ResponseEntity.ok(page_excl);
			}
		}
	}
	
	public ExclFrequency add(ExclFrequency excl) throws IllegalArgumentException, Exception{
		excl.setExcludefrequencyuid(UUID.randomUUID().toString());
		
		String excludefrequencyname = excl.getExcludefrequencyname();
		if(null == excludefrequencyname || excludefrequencyname.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Name can not be empty!");
		excl.setExcludefrequencyname(excludefrequencyname.toUpperCase());
		
		if(this.dao.existByName(excl.getExcludefrequencyname()))
			throw new IllegalArgumentException("Duplicate Exclude Frequency Name!");
		
		String activate = excl.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Exclude Frequency activate value can only be 1 or 0!");
		
		if(null == excl.getDescription())
			excl.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		excl.setLastupdatetime(new Date());
		
		this.dao.save(excl);
		List<ExclFrequencyList> exclFreqList = excl.getExcludefrequencylist();
		if(null != exclFreqList && exclFreqList.size() > 0) {
			int[] returnValue = this.listService.addBatch(excl.getExcludefrequencyuid(), exclFreqList);
			for(int i=0; i<returnValue.length; i++) {//重設exclude frequency list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					exclFreqList.remove(i);
				}
			}
			excl.setExcludefrequencylist(exclFreqList);
		}
		return excl;
	}
	
	public ExclFrequency edit(ExclFrequency excl) throws IllegalArgumentException, Exception{		
		String excludefrequencyuid = excl.getExcludefrequencyuid();
		if(null == excludefrequencyuid || excludefrequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
		
		ExclFrequency old_excl = this.dao.findOne(excludefrequencyuid);
		if(null == old_excl)
			throw new IllegalArgumentException("Exclude Frequency Uid does not exist!(" + excludefrequencyuid + ")");
		
		String excludefrequencyname = excl.getExcludefrequencyname();
		if(null == excludefrequencyname || excludefrequencyname.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Name can not be empty!");
		excl.setExcludefrequencyname(excludefrequencyname.toUpperCase());
		
		if(this.dao.existByName(excl.getExcludefrequencyname()) && 
				!old_excl.getExcludefrequencyname().equalsIgnoreCase(old_excl.getExcludefrequencyname()))
			throw new IllegalArgumentException("Duplicate Exclude Frequency Name!");
		
		String activate = excl.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Exclude Frequency activate value can only be 1 or 0!");
		
		if(null == excl.getDescription())
			excl.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		excl.setLastupdatetime(new Date());
		
		this.dao.save(excl);
		List<ExclFrequencyList> exclFreqList = excl.getExcludefrequencylist();
		if(null != exclFreqList && exclFreqList.size() > 0) {
			this.listService.deleteByExclFreqUid(excl.getExcludefrequencyuid());
			int[] returnValue = this.listService.addBatch(excl.getExcludefrequencyuid(), exclFreqList);
			for(int i=0; i<returnValue.length; i++) {//重設Exclude Frequency list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					exclFreqList.remove(i);
				}
			}
			excl.setExcludefrequencylist(exclFreqList);
		}
		return excl;
	}
	
	public ExclFrequency modifyGlobal(ExclFrequency excl) throws IllegalArgumentException, Exception{		
		excl.setExcludefrequencyuid("global");
		excl.setExcludefrequencyname("GLOBAL");
		
		String activate = excl.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Exclude Frequency activate value can only be 1 or 0!");
		
		if(null == excl.getDescription())
			excl.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		excl.setLastupdatetime(new Date());
		
		this.dao.save(excl);
		List<ExclFrequencyList> exclFreqList = excl.getExcludefrequencylist();
		if(null != exclFreqList && exclFreqList.size() > 0) {
			this.listService.deleteByExclFreqUid(excl.getExcludefrequencyuid());
			int[] returnValue = this.listService.addBatch(excl.getExcludefrequencyuid(), exclFreqList);
			for(int i=0; i<returnValue.length; i++) {//重設Exclude Frequency list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					exclFreqList.remove(i);
				}
			}
			excl.setExcludefrequencylist(exclFreqList);
		}
		return excl;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
		
		this.freqExcludeService.deleteByExcludeFrequencyUid(uid);
		this.jobExcludeService.deleteByExcludeFrequencyUid(uid);
		this.flowExcludeService.deleteByExcludeFrequencyUid(uid);
		this.listService.deleteByExclFreqUid(uid);
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
		
		Order order = new Order(direct, "lastupdatetime");
		if(ordering.getOrderField() != null && EXCL_FIELD_SET.contains(ordering.getOrderField().toLowerCase()))
			order = new Order(direct, ordering.getOrderField());
		
		return new Sort(order);
	}
	
	private void getExclFreqList(List<ExclFrequency> excls) throws Exception {
		for(ExclFrequency excl : excls) {
			getExclFreqList(excl);
		}
	}
	
	private void getExclFreqList(ExclFrequency excl) throws Exception {
		excl.setExcludefrequencylist(this.listService.getByExclFreqUid(excl.getExcludefrequencyuid()));
	}
}
