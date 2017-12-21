package com.netpro.trinity.repository.service.frequency;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netpro.trinity.repository.dao.jpa.frequency.FrequencyJPADao;

@Service
public class FrequencyService {
	public static final String[] FREQ_FIELD_VALUES = new String[] {"filesourcename", "description"};
	public static final Set<String> FREQ_FIELD_SET = new HashSet<>(Arrays.asList(FREQ_FIELD_VALUES));
	
	@Autowired
	FrequencyJPADao dao;
	
	public boolean existByWCalendaruid(String wcalendaruid) throws IllegalArgumentException, Exception{
		if(null == wcalendaruid || wcalendaruid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		
		return this.dao.existByWCalendaruid(wcalendaruid);
	}
}
