package com.netpro.trinity.service.frequency.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.dto.Ordering;
import com.netpro.trinity.service.dto.Paging;
import com.netpro.trinity.service.frequency.dao.FrequencyCategoryJPADao;
import com.netpro.trinity.service.frequency.entity.FrequencyCategory;
import com.netpro.trinity.service.util.Constant;

@Service
public class FrequencyCategoryService {
	public static final String[] FREQ_CATEGORY_FIELD_VALUES = new String[] {"freqcategoryname", "description"};
	public static final Set<String> FREQ_CATEGORY_FIELD_SET = new HashSet<>(Arrays.asList(FREQ_CATEGORY_FIELD_VALUES));
	
	@Autowired
	private FrequencyCategoryJPADao dao;
	
	@Autowired
	private FrequencyRelationService relService;
	
	public List<FrequencyCategory> getAll() throws Exception{
		return this.dao.findAll();
	}
	
	public FrequencyCategory getByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.isEmpty())
			throw new IllegalArgumentException("Frequency Category UID can not be empty!");
		
		FrequencyCategory category = null;
		try {
			category = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == category)
			throw new IllegalArgumentException("Frequency Category UID does not exist!(" + uid + ")");

		return category;
	}
	
	public List<FrequencyCategory> getByName(String name) throws IllegalArgumentException, Exception{
		if(null == name || name.isEmpty())
			throw new IllegalArgumentException("Frequency Category Name can not be empty!");
		
		return this.dao.findByFreqcategorynameLikeIgnoreCase(name.toUpperCase());
	}
	
	public ResponseEntity<?> getByFilter(FilterInfo filter) throws Exception {
		Ordering ordering = filter.getOrdering();
		String param = filter.getParam();
		
		if(null == ordering) 
			ordering = new Ordering("ASC", "freqcategoryname");
		
		if(null == param || param.trim().isEmpty())
			param = "%%";
		param = param.trim();
		
		List<FrequencyCategory> categoryList = this.dao.findByFreqcategorynameLikeIgnoreCase(param, getOrdering(ordering));
		return ResponseEntity.ok(categoryList);
	}
	
	public FrequencyCategory add(FrequencyCategory category) throws IllegalArgumentException, Exception{
		category.setFreqcategoryuid(UUID.randomUUID().toString());
		
		String name = category.getFreqcategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category Name can not be empty!");
		category.setFreqcategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getFreqcategoryname()))
			throw new IllegalArgumentException("Duplicate Frequency Category Name!");
		
		if(null == category.getDescription())
			category.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		category.setLastupdatetime(new Date());
				
		return this.dao.save(category);
	}
	
	public FrequencyCategory edit(FrequencyCategory category) throws IllegalArgumentException, Exception{
		String uid = category.getFreqcategoryuid();
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category Uid can not be empty!");

		FrequencyCategory old_category = null;
		try {
			old_category = this.dao.findById(uid).get();
		}catch(NoSuchElementException e) {}
		 
		if(null == old_category)
			throw new IllegalArgumentException("Frequency Category Uid does not exist!(" + uid + ")");
		
		String name = category.getFreqcategoryname();
		if(null == name || name.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category Name can not be empty!");
		category.setFreqcategoryname(name.toUpperCase());
		
		if(this.dao.existByName(category.getFreqcategoryname()) && !old_category.getFreqcategoryname().equalsIgnoreCase(category.getFreqcategoryname()))
			throw new IllegalArgumentException("Duplicate Frequency Category Name!");
		
		if(null == category.getDescription())
			category.setDescription("");
		
		/*
		 * because lastupdatetime column is auto created value, it can not be reload new value.
		 * here, we force to give value to lastupdatetime column.
		 */
		category.setLastupdatetime(new Date());
						
		return this.dao.save(category);
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Frequency Category Uid can not be empty!");
		if(relService.existByCategoryUid(uid))
			throw new IllegalArgumentException("Delete Frequency under this category first!");

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
}
