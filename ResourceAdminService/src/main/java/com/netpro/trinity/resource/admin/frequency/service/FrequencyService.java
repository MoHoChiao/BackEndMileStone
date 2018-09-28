package com.netpro.trinity.resource.admin.frequency.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
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
import com.netpro.trinity.resource.admin.frequency.dao.FrequencyJPADao;
import com.netpro.trinity.resource.admin.frequency.entity.Frequency;
import com.netpro.trinity.resource.admin.frequency.entity.FrequencyCategory;
import com.netpro.trinity.resource.admin.frequency.entity.FrequencyList;
import com.netpro.trinity.resource.admin.frequency.entity.FrequencyRelation;
import com.netpro.trinity.resource.admin.job.service.JobService;
import com.netpro.trinity.resource.admin.objectalias.service.ObjectAliasService;
import com.netpro.trinity.resource.admin.util.Constant;

@Service
public class FrequencyService {
	public static final String[] FREQ_FIELD_VALUES = new String[] { "frequencyname", "activate", "description" };
	public static final Set<String> FREQ_FIELD_SET = new HashSet<>(Arrays.asList(FREQ_FIELD_VALUES));

	@Autowired
	FrequencyJPADao dao;

	@Autowired
	private FrequencyListService listService;
	@Autowired
	private FrequencyRelationService relService;
	@Autowired
	private FreqExcludeService feService;
	@Autowired
	private JobService jobService;
	@Autowired
	private ObjectAliasService objectAliasService;

	public List<Frequency> getAll() throws Exception {
		return this.dao.findAll();
	}

	public List<Frequency> getAllWithoutInCategory() throws Exception {
		List<String> freq_uids = relService.getAllFrequencyUids();
		if (freq_uids.isEmpty()) {
			/*
			 * 當系統沒有建立任何conn和conn category之間的relation時, 則任意給一個空字串值 以免not
			 * in當中的值為empty, 導致搜尋不到任何不在conn category中的conn
			 */
			freq_uids.add("");
		}
		List<Frequency> freqs = this.dao.findByFrequencyuidNotIn(freq_uids, Sort.by("frequencyname"));
		return freqs;
	}

	public Frequency getByUid(String uid) throws IllegalArgumentException, Exception {
		if (uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Frequency UID can not be empty!");

		Frequency freq = null;
		try {
			freq = this.dao.findById(uid).get();
		} catch (NoSuchElementException e) {
		}

		if (null == freq)
			throw new IllegalArgumentException("Frequency UID does not exist!(" + uid + ")");
		
		FrequencyCategory fc = this.relService.getCategoryByFrequencyUid(uid);
		
		if (fc != null) {
			freq.setCategoryuid(fc.getFreqcategoryuid());
			freq.setCategoryname(fc.getFreqcategoryname());
		}
		
		return freq;
	}

	public List<Frequency> getByName(String name) throws IllegalArgumentException, Exception {
		if (name == null || name.trim().isEmpty())
			throw new IllegalArgumentException("Frequency Name can not be empty!");

		return this.dao.findByfrequencyname(name.toUpperCase());
	}

	public List<Frequency> getByCategoryUid(String uid) throws IllegalArgumentException, Exception {
		if (uid == null || uid.trim().isEmpty())
			throw new IllegalArgumentException("Frequency Category UID can not be empty!");

		List<Frequency> freqs = this.dao.findByFrequencyuidIn(relService.getFrequencyUidsByCategoryUid(uid),
				Sort.by("frequencyname"));
		return freqs;
	}

	public ResponseEntity<?> getByFilter(String categoryUid, FilterInfo filter) throws Exception {
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();

		if (null == paging)
			paging = new Paging(0, 20);

		if (null == ordering)
			ordering = new Ordering("ASC", "frequencyname");

		if (null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();

		Page<Frequency> page = null;
		if (null == categoryUid) {
			page = this.dao.findByfrequencynameLikeIgnoreCase(param, getPagingAndOrdering(paging, ordering));

			/*
			 * 當category uid為null時, 表示沒有指定任何的category, 包括連root也沒指定
			 * 此時需要取得 uid及category name的對映表, 用來多回傳一個category name,
			 * 照作,沒什麼意義的需求
			 */
			Map<String, String> mapping = this.relService.getFrequencyUidAndCategoryNameMap();
			setProfileDataAndSetCategoryName(page.getContent(), mapping);
		} else {
			List<String> freq_uids = null;
			if (categoryUid.trim().isEmpty() || "root".equals(categoryUid.trim())) {
				freq_uids = this.relService.getAllFrequencyUids();
				if (freq_uids.isEmpty()) {
					/*
					 * 當系統沒有建立任何freq和freq category之間的relation時, 則任意給一個空字串值 以免not
					 * in當中的值為empty, 導致搜尋不到任何不在freq category中的freq
					 */
					freq_uids.add("");
				}
				page = this.dao.findByfrequencynameLikeIgnoreCaseAndFrequencyuidNotIn(param,
						getPagingAndOrdering(paging, ordering), freq_uids);
			} else {
				freq_uids = this.relService.getFrequencyUidsByCategoryUid(categoryUid);
				page = this.dao.findByfrequencynameLikeIgnoreCaseAndFrequencyuidIn(param,
						getPagingAndOrdering(paging, ordering), freq_uids);
			}
		}

		return ResponseEntity.ok(page);
	}

	public Frequency add(String categoryUid, Frequency freq) throws IllegalArgumentException, Exception {
		freq.setFrequencyuid(UUID.randomUUID().toString());

		String frequencyname = freq.getFrequencyname();
		if (null == frequencyname || frequencyname.length() <= 0)
			throw new IllegalArgumentException("Frequency Name can not be empty!");
		frequencyname = frequencyname.toUpperCase();

		if (this.dao.existByName(frequencyname))
			throw new IllegalArgumentException("Duplicate Frequency Name!");

		freq.setFrequencyname(frequencyname);

		String description = freq.getDescription();
		if (null == description)
			description = "";

		String activate = freq.getActivate();
		if (null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Frequency activate value can only be 1 or 0!");

		String manuallyedit = freq.getManuallyedit();
		if (null == manuallyedit
				|| (!manuallyedit.equals("0") && !manuallyedit.equals("1") && !manuallyedit.equals("2")))
			throw new IllegalArgumentException("Frequency manually edit value can only be 0, 1, or 2!");

		String bywcalendar = freq.getBywcalendar();
		if (null == bywcalendar || (!bywcalendar.equals("0") && !bywcalendar.equals("1")))
			throw new IllegalArgumentException("Frequency by working calendar value can only be 0 or 1!");

		if (bywcalendar.trim().equals("1")) {
			String wcalendaruid = freq.getWcalendaruid();
			if (null == wcalendaruid || wcalendaruid.length() <= 0)
				throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		}

		freq.setLastupdatetime(new Date());

		this.dao.save(freq);
		List<FrequencyList> freqList = freq.getFreqlist();
		if (null != freqList && freqList.size() > 0) {
			int[] returnValue = this.listService.addBatch(freq.getFrequencyuid(), freqList);
			for (int i = 0; i < returnValue.length; i++) {// 重設frequency list,
															// 只有插入成功的會留下來傳回前端
				if (returnValue[i] == 0) {
					freqList.remove(i);
				}
			}
			freq.setFreqlist(freqList);
		}

		// 如果所附帶的url參數中有categoryUid的話, 表示是要把frequency新增至某個category
		if (categoryUid != null && !categoryUid.trim().equals("")) {
			FrequencyRelation rel = new FrequencyRelation();
			rel.setFreqcategoryuid(categoryUid);
			rel.setFrequencyuid(freq.getFrequencyuid());
			this.relService.add(rel);
		}

		return freq;
	}

	public Frequency edit(String categoryUid, Frequency freq) throws IllegalArgumentException, Exception {
		String frequencyuid = freq.getFrequencyuid();
		if (null == frequencyuid || frequencyuid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");

		Frequency old_freq = null;
		try {
			old_freq = this.dao.findById(frequencyuid).get();
		} catch (NoSuchElementException e) {
		}

		if (null == old_freq)
			throw new IllegalArgumentException("Frequency Uid does not exist!(" + frequencyuid + ")");

		String frequencyname = freq.getFrequencyname();
		if (null == frequencyname || frequencyname.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Name can not be empty!");
		freq.setFrequencyname(frequencyname.toUpperCase());

		if (this.dao.existByName(freq.getFrequencyname())
				&& !old_freq.getFrequencyname().equalsIgnoreCase(freq.getFrequencyname()))
			throw new IllegalArgumentException("Duplicate Frequency Name!");

		String description = freq.getDescription();
		if (null == description)
			description = "";

		String activate = freq.getActivate();
		if (null == activate || (!activate.equals("1") && !activate.equals("0")))
			throw new IllegalArgumentException("Frequency activate value can only be 1 or 0!");

		String manuallyedit = freq.getManuallyedit();
		if (null == manuallyedit
				|| (!manuallyedit.equals("0") && !manuallyedit.equals("1") && !manuallyedit.equals("2")))
			throw new IllegalArgumentException("Frequency manually edit value can only be 0, 1, or 2!");

		String bywcalendar = freq.getBywcalendar();
		if (null == bywcalendar || (!bywcalendar.equals("0") && !bywcalendar.equals("1")))
			throw new IllegalArgumentException("Frequency by working calendar value can only be 0 or 1!");

		if (bywcalendar.trim().equals("1")) {
			String wcalendaruid = freq.getWcalendaruid();
			if (null == wcalendaruid || wcalendaruid.length() <= 0)
				throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		}

		freq.setLastupdatetime(new Date());

		this.dao.save(freq);

		List<FrequencyList> freqList = freq.getFreqlist();
		if (null != freqList && freqList.size() > 0) {
			this.listService.deleteByFrequencyUid(freq.getFrequencyuid());
			int[] returnValue = this.listService.addBatch(freq.getFrequencyuid(), freqList);
			for (int i = 0; i < returnValue.length; i++) {// 重設frequency list,
															// 只有插入成功的會留下來傳回前端
				if (returnValue[i] == 0) {
					freqList.remove(i);
				}
			}
			freq.setFreqlist(freqList);
		}

		// 如果所附帶的url參數中有categoryUid的話, 表示是要把Frequency編輯至某個category或root
		if (categoryUid != null) {
			this.relService.deleteByFrequencyUid(freq.getFrequencyuid());
			if (!categoryUid.trim().equals("")) { // 如果categoryUid不是空值,
													// 表示是要把Frequency編輯到某一個category底下
				FrequencyRelation rel = new FrequencyRelation();
				rel.setFreqcategoryuid(categoryUid);
				rel.setFrequencyuid(freq.getFrequencyuid());
				this.relService.add(rel);
			}
		}

		return freq;
	}

	public void deleteByUid(String uid) throws IllegalArgumentException, Exception {
		if (null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Uid can not be empty!");

		if (jobService.existByFrequencyuid(uid)) {
			throw new IllegalArgumentException("Referenceing by job");
		} else if (objectAliasService.existByObjectuid(uid)) {
			throw new IllegalArgumentException("Referenceing by Object Alias");
		} else {
			this.relService.deleteByFrequencyUid(uid);
			this.listService.deleteByFrequencyUid(uid);
			this.feService.deleteByFrequencyUid(uid);
			this.dao.deleteById(uid);
		}
	}

	public boolean existByUid(String uid) throws Exception {
		return this.dao.existsById(uid);
	}

	public boolean existByWCalendaruid(String wcalendaruid) throws IllegalArgumentException, Exception {
		if (null == wcalendaruid || wcalendaruid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Uid can not be empty!");

		return this.dao.existByWCalendaruid(wcalendaruid);
	}

	private PageRequest getPagingAndOrdering(Paging paging, Ordering ordering) throws Exception {
		if (paging.getNumber() == null)
			paging.setNumber(0);

		if (paging.getSize() == null)
			paging.setSize(10);

		if (ordering != null) {
			return PageRequest.of(paging.getNumber(), paging.getSize(), getOrdering(ordering));
		} else {
			return PageRequest.of(paging.getNumber(), paging.getSize());
		}
	}

	private Sort getOrdering(Ordering ordering) throws Exception {
		Direction direct = Direction.fromString("DESC");
		if (ordering.getOrderType() != null && Constant.ORDER_TYPE_SET.contains(ordering.getOrderType().toUpperCase()))
			direct = Direction.fromString(ordering.getOrderType());

		if (ordering.getOrderField() != null && !ordering.getOrderField().isEmpty())
			return Sort.by(direct, ordering.getOrderField());
		else
			return Sort.by(direct, "lastupdatetime");
	}
	
	private void setProfileDataAndSetCategoryName(List<Frequency> list, Map<String, String> mapping) {
		for(Frequency freq : list) {
			setProfileDataAndSetCategoryName(freq, mapping);
		}
	}
	
	private void setProfileDataAndSetCategoryName(Frequency freq, Map<String, String> mapping) {
		String categoryName = mapping.get(freq.getFrequencyuid());
		if(null != categoryName)
			freq.setCategoryname(categoryName);
		else {
			freq.setCategoryname("/");
		}
	}
	
}
