package com.netpro.trinity.service.frequency.service;

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
import com.netpro.trinity.service.filesource.entity.Frequency;
import com.netpro.trinity.service.frequency.dao.FrequencyJPADao;
import com.netpro.trinity.service.frequency.entity.FrequencyList;
import com.netpro.trinity.service.frequency.entity.FrequencyRelation;
import com.netpro.trinity.service.job.service.JobService;
import com.netpro.trinity.service.objectalias.service.ObjectAliasService;
import com.netpro.trinity.service.util.Constant;

@Service
public class FrequencyService {
	public static final String[] FREQ_FIELD_VALUES = new String[] {"frequencyname", "activate", "description"};
	public static final Set<String> FREQ_FIELD_SET = new HashSet<>(Arrays.asList(FREQ_FIELD_VALUES));
	
	@Autowired
	FrequencyJPADao dao;
	
	@Autowired
	private FrequencyListService listService;
	@Autowired
	FrequencyRelationService relService;
	@Autowired
	FreqExcludeService feService;
	@Autowired
	private JobService jobService;
	@Autowired
	private ObjectAliasService objectAliasService;
	
	public List<Frequency> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public List<Frequency> getAllWithoutInCategory() throws Exception{
		List<String> freq_uids = relService.getAllFrequencyUids();
		if(freq_uids.isEmpty()) {
			/*
			 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
			 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
			 */
			freq_uids.add("");
		}
		List<Frequency> freqs = this.dao.findByFrequencyuidNotIn(freq_uids, Sort.by("frequencyname"));
		return freqs;
	}
	
	public Frequency getByUid(String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Frequency UID can not be empty!");
		
		Frequency freq = this.dao.findById(uid).get();
		if(null == freq)
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
		
		List<Frequency> freqs = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(uid), Sort.by("frequencyname"));
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
					List<String> freq_uids = relService.getAllFrequencyUids();
					if(freq_uids.isEmpty()) {
						/*
						 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
						 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
						 */
						freq_uids.add("");
					}
					freqs = this.dao.findByFrequencyuidNotIn(freq_uids);
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
					List<String> freq_uids = relService.getAllFrequencyUids();
					if(freq_uids.isEmpty()) {
						/*
						 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
						 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
						 */
						freq_uids.add("");
					}
					freqs = this.dao.findByFrequencyuidNotIn(freq_uids);
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
						List<String> freq_uids = relService.getAllFrequencyUids();
						if(freq_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							freq_uids.add("");
						}
						page_freq = this.dao.findByFrequencyuidNotIn(freq_uids, pageRequest);
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
						List<String> freq_uids = relService.getAllFrequencyUids();
						if(freq_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							freq_uids.add("");
						}
						freqs = this.dao.findByFrequencyuidNotIn(freq_uids, sort);
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
						List<String> freq_uids = relService.getAllFrequencyUids();
						if(freq_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							freq_uids.add("");
						}
						freqs = this.dao.findByFrequencyuidNotIn(freq_uids);
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
						List<String> freq_uids = relService.getAllFrequencyUids();
						if(freq_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							freq_uids.add("");
						}
						page_freq = (Page<Frequency>) method.invoke(this.dao, queryString, pageRequest, freq_uids);
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
						List<String> freq_uids = relService.getAllFrequencyUids();
						if(freq_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							freq_uids.add("");
						}
						freqs = (List<Frequency>) method.invoke(this.dao, queryString, sort, freq_uids);
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
						List<String> freq_uids = relService.getAllFrequencyUids();
						if(freq_uids.isEmpty()) {
							/*
							 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值
							 * 以免not in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
							 */
							freq_uids.add("");
						}
						freqs = (List<Frequency>) method.invoke(this.dao, queryString, freq_uids);
					}else {
						freqs = (List<Frequency>) method.invoke(this.dao, queryString, relService.getFrequencyUidsByCategoryUid(categoryUid));
					}
				}
				return ResponseEntity.ok(freqs);
			}
		}
	}
	
	public Frequency add(String categoryUid, Frequency freq) throws IllegalArgumentException, Exception{
		freq.setFrequencyuid(UUID.randomUUID().toString());
		
		String frequencyname = freq.getFrequencyname();
		if(null == frequencyname || frequencyname.length() <= 0)
			throw new IllegalArgumentException("Frequency Name can not be empty!");
		frequencyname = frequencyname.toUpperCase();
		
		if(this.dao.existByName(frequencyname))
			throw new IllegalArgumentException("Duplicate Frequency Name!");
		
		freq.setFrequencyname(frequencyname);
		
		String description = freq.getDescription();
		if(null == description)
			description = "";
		
		String activate = freq.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Frequency activate value can only be 1 or 0!");
		
		String manuallyedit = freq.getManuallyedit();
		if(null == manuallyedit || (!manuallyedit.equals("0") && !manuallyedit.equals("1") && !manuallyedit.equals("2")))
			throw new IllegalArgumentException("Frequency manually edit value can only be 0, 1, or 2!");
		
		String bywcalendar = freq.getBywcalendar();
		if(null == bywcalendar || (!bywcalendar.equals("0") && !bywcalendar.equals("1")))
			throw new IllegalArgumentException("Frequency by working calendar value can only be 0 or 1!");
		
		if(bywcalendar.trim().equals("1")) {
			String wcalendaruid = freq.getWcalendaruid();
			if(null == wcalendaruid || wcalendaruid.length() <= 0)
				throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		}
		
		freq.setLastupdatetime(new Date());
		
		this.dao.save(freq);
		List<FrequencyList> freqList = freq.getFreqlist();
		if(null != freqList && freqList.size() > 0) {
			int[] returnValue = this.listService.addBatch(freq.getFrequencyuid(), freqList);
			for(int i=0; i<returnValue.length; i++) {//重設frequency list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					freqList.remove(i);
				}
			}
			freq.setFreqlist(freqList);
		}
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把frequency新增至某個category
		if(categoryUid != null && !categoryUid.trim().equals("")) {
			FrequencyRelation rel = new FrequencyRelation();
			rel.setFreqcategoryuid(categoryUid);
			rel.setFrequencyuid(freq.getFrequencyuid());
			this.relService.add(rel);
		}
		
		return freq;
	}
	
	public Frequency edit(String categoryUid, Frequency freq) throws IllegalArgumentException, Exception{
		String frequencyuid = freq.getFrequencyuid();
		if(null == frequencyuid || frequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		Frequency old_freq = this.dao.findById(frequencyuid).get();
		if(null == old_freq)
			throw new IllegalArgumentException("Frequency Uid does not exist!(" + frequencyuid + ")");
		
		String frequencyname = freq.getFrequencyname();
		if(null == frequencyname || frequencyname.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Name can not be empty!");
		freq.setFrequencyname(frequencyname.toUpperCase());
		
		if(this.dao.existByName(freq.getFrequencyname()) && !old_freq.getFrequencyname().equalsIgnoreCase(freq.getFrequencyname()))
			throw new IllegalArgumentException("Duplicate Frequency Name!");
		
		String description = freq.getDescription();
		if(null == description)
			description = "";
		
		String activate = freq.getActivate();
		if(null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Frequency activate value can only be 1 or 0!");
		
		String manuallyedit = freq.getManuallyedit();
		if(null == manuallyedit || (!manuallyedit.equals("0") && !manuallyedit.equals("1") && !manuallyedit.equals("2")))
			throw new IllegalArgumentException("Frequency manually edit value can only be 0, 1, or 2!");
		
		String bywcalendar = freq.getBywcalendar();
		if(null == bywcalendar || (!bywcalendar.equals("0") && !bywcalendar.equals("1")))
			throw new IllegalArgumentException("Frequency by working calendar value can only be 0 or 1!");
		
		if(bywcalendar.trim().equals("1")) {
			String wcalendaruid = freq.getWcalendaruid();
			if(null == wcalendaruid || wcalendaruid.length() <= 0)
				throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		}
		
		freq.setLastupdatetime(new Date());
		
		this.dao.save(freq);
		
		List<FrequencyList> freqList = freq.getFreqlist();
		if(null != freqList && freqList.size() > 0) {
			this.listService.deleteByFrequencyUid(freq.getFrequencyuid());
			int[] returnValue = this.listService.addBatch(freq.getFrequencyuid(), freqList);
			for(int i=0; i<returnValue.length; i++) {//重設frequency list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					freqList.remove(i);
				}
			}
			freq.setFreqlist(freqList);
		}
		
		//如果所附帶的url參數中有categoryUid的話, 表示是要把Frequency編輯至某個category或root
		if(categoryUid != null) {
			this.relService.deleteByFrequencyUid(freq.getFrequencyuid());
			if(!categoryUid.trim().equals("")) {	//如果categoryUid不是空值, 表示是要把Frequency編輯到某一個category底下
				FrequencyRelation rel = new FrequencyRelation();
				rel.setFreqcategoryuid(categoryUid);
				rel.setFrequencyuid(freq.getFrequencyuid());
				this.relService.add(rel);
			}
		}
		
		return freq;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");
		
		if(jobService.existByFrequencyuid(uid)) {
			throw new IllegalArgumentException("Referenceing by job");
		}else if(objectAliasService.existByObjectuid(uid)) {
			throw new IllegalArgumentException("Referenceing by Object Alias");
		}else {
			this.relService.deleteByFrequencyUid(uid);
			this.listService.deleteByFrequencyUid(uid);
			this.feService.deleteByFrequencyUid(uid);
			this.dao.deleteById(uid);
		}
	}
	
	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
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
}
