package com.netpro.trinity.repository.frequency.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.netpro.trinity.repository.filesource.entity.WorkingCalendar;
import com.netpro.trinity.repository.frequency.dao.WorkingCalendarJPADao;
import com.netpro.trinity.repository.frequency.entity.WorkingCalendarList;
import com.netpro.trinity.repository.frequency.entity.WorkingCalendarPattern;
import com.netpro.trinity.repository.frequency.lib.DailyEveryDaysHandler;
import com.netpro.trinity.repository.frequency.lib.DailyEveryWeekdayHandler;
import com.netpro.trinity.repository.frequency.lib.EndDateHandler;
import com.netpro.trinity.repository.frequency.lib.IRecurrenceEndDateHandler;
import com.netpro.trinity.repository.frequency.lib.IRecurrenceHandler;
import com.netpro.trinity.repository.frequency.lib.MonthlyEveryMonthHandler;
import com.netpro.trinity.repository.frequency.lib.MonthlyTheMonthHandler;
import com.netpro.trinity.repository.frequency.lib.OccurencesEndDateHandler;
import com.netpro.trinity.repository.frequency.lib.Recurrence;
import com.netpro.trinity.repository.frequency.lib.WeeklyHandler;
import com.netpro.trinity.repository.frequency.lib.YearlyEveryYearHandler;
import com.netpro.trinity.repository.frequency.lib.YearlyTheYearHandler;
import com.netpro.trinity.repository.util.Constant;

@Service
public class WorkingCalendarService {
	public static final String[] WC_FIELD_VALUES = new String[] { "wcalendarname", "activate", "description"};
	public static final Set<String> WC_FIELD_SET = new HashSet<>(Arrays.asList(WC_FIELD_VALUES));
	
	@Autowired
	private WorkingCalendarJPADao dao;
	
	@Autowired
	private WorkingCalendarListService listService;
	@Autowired
	private FrequencyService freqService;
	
	public List<WorkingCalendar> getAll(Boolean withoutDetail) throws Exception{
		List<WorkingCalendar> wcs = this.dao.findAll();
		if(null == withoutDetail || withoutDetail == false)
			getWCList(wcs);
		return wcs;
	}
	
	public WorkingCalendar getByUid(Boolean withoutDetail, String uid) throws IllegalArgumentException, Exception{
		if(uid == null || uid.isEmpty())
			throw new IllegalArgumentException("Working Calendar UID can not be empty!");
		
		WorkingCalendar wc = this.dao.findOne(uid);
		if(null == wc)
			throw new IllegalArgumentException("Working Calendar UID does not exist!(" + uid + ")");
		
		if(null == withoutDetail || withoutDetail == false)
			getWCList(wc);
		
		return wc;
	}
	
	public List<WorkingCalendar> getByName(Boolean withoutDetail, String name) throws IllegalArgumentException, Exception{
		if(null == name || name.isEmpty())
			throw new IllegalArgumentException("Working Calendar Name can not be empty!");
		
		List<WorkingCalendar> wcs = this.dao.findBywcalendarname(name.toUpperCase());
		
		if(null == withoutDetail || withoutDetail == false)
			getWCList(wcs);
		
		return wcs;
	}
	
	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getByFilter(Boolean withoutDetail, FilterInfo filter) throws SecurityException, NoSuchMethodException, 
								IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception{
		if(null == filter) {
			List<WorkingCalendar> wcs = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
				getWCList(wcs);
			return ResponseEntity.ok(wcs);
		}
			
		
		Paging paging = filter.getPaging();
		Ordering ordering = filter.getOrdering();
		Querying querying = filter.getQuerying();
		
		if(null == paging && null == ordering && null == querying) {
			List<WorkingCalendar> wcs = this.dao.findAll();
			if(null == withoutDetail || withoutDetail == false)
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
		
		if(null == querying) {
			if(pageRequest != null) {
				Page<WorkingCalendar> page_wc = this.dao.findAll(pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getWCList(page_wc.getContent());
				return ResponseEntity.ok(page_wc);
			}else if(sort != null) {
				List<WorkingCalendar> wcs = this.dao.findAll(sort);
				if(null == withoutDetail || withoutDetail == false)
					getWCList(wcs);
				return ResponseEntity.ok(wcs);
			}else {
				/*
				 * The paging and ordering both objects are null.
				 * it means pageRequest and sort must be null too.
				 * then return default
				 */
				List<WorkingCalendar> wcs = this.dao.findAll(sort);
				if(null == withoutDetail || withoutDetail == false)
					getWCList(wcs);
				return ResponseEntity.ok(wcs);
			}
		}else {
			if(null == querying.getQueryType() || !Constant.QUERY_TYPE_SET.contains(querying.getQueryType().toLowerCase()))
				throw new IllegalArgumentException("Illegal query type! "+Constant.QUERY_TYPE_SET.toString());
			if(null == querying.getQueryField() || !WC_FIELD_SET.contains(querying.getQueryField().toLowerCase()))
				throw new IllegalArgumentException("Illegal query field! "+ WC_FIELD_SET.toString());
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
				Page<WorkingCalendar> page_wc = (Page<WorkingCalendar>) method.invoke(this.dao, queryString, pageRequest);
				if(null == withoutDetail || withoutDetail == false)
					getWCList(page_wc.getContent());
				return ResponseEntity.ok(page_wc);
			}else if(sort != null) {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class, Sort.class);
				List<WorkingCalendar> wcs = (List<WorkingCalendar>) method.invoke(this.dao, queryString, sort);
				if(null == withoutDetail || withoutDetail == false)
					getWCList(wcs);
				return ResponseEntity.ok(wcs);
			}else {
				method = this.dao.getClass().getMethod(methodName.toString(), String.class);
				List<WorkingCalendar> wcs = (List<WorkingCalendar>) method.invoke(this.dao, queryString);
				if(null == withoutDetail || withoutDetail == false)
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
		List<WorkingCalendarList> wcList = wc.getWcalendarlist();
		if(null != wcList && wcList.size() > 0) {
			int[] returnValue = this.listService.addBatch(wc.getWcalendaruid(), wcList);
			for(int i=0; i<returnValue.length; i++) {//重設working calendar list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					wcList.remove(i);
				}
			}
			wc.setWcalendarlist(wcList);
		}
		
		return wc;
	}
	
	public WorkingCalendar edit(WorkingCalendar wc) throws IllegalArgumentException, Exception{		
		String wc_uid = wc.getWcalendaruid();
		if(null == wc_uid || wc_uid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
		
		if(wc_uid.trim().equalsIgnoreCase("SYSTEMDAY"))
			throw new IllegalArgumentException("System day can not be edited!");
		
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
		List<WorkingCalendarList> wcList = wc.getWcalendarlist();
		if(null != wcList && wcList.size() > 0) {
			this.listService.deleteByWCUid(wc.getWcalendaruid());
			int[] returnValue = this.listService.addBatch(wc.getWcalendaruid(), wcList);
			for(int i=0; i<returnValue.length; i++) {//重設working calendar list, 只有插入成功的會留下來傳回前端
				if(returnValue[i] == 0) {
					wcList.remove(i);
				}
			}
			wc.setWcalendarlist(wcList);
		}
		
		return wc;
	}
	
	public void deleteByUid(String uid) throws IllegalArgumentException, Exception{
		if(null == uid || uid.trim().length() <= 0)
			throw new IllegalArgumentException("Working Calendar Uid can not be empty!");
				
		if(uid.trim().equalsIgnoreCase("SYSTEMDAY"))
			throw new IllegalArgumentException("System day can not be removed!");
		
		if(!freqService.existByWCalendaruid(uid)) {
			this.listService.deleteByWCUid(uid);
			this.dao.delete(uid);
		}else {
			throw new IllegalArgumentException("Referenceing by frequency");
		}
	}
	
	public List<String> getWCPattern(WorkingCalendarPattern dp) throws IllegalArgumentException, ParseException, NumberFormatException, Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(null == dp.getStartDate() || dp.getStartDate().trim().isEmpty())
			throw new IllegalArgumentException("The value of startDate can not be empty!");
		Date startDate = sdf.parse(dp.getStartDate());
		
		String patternType = dp.getPatternType();
		if(null == patternType || patternType.trim().isEmpty())
			throw new IllegalArgumentException("The value of patternType can not be empty!");
		
		String endDateType = dp.getEndDateType();
		if(null == endDateType || endDateType.trim().isEmpty())
			throw new IllegalArgumentException("The value of endDateType can not be empty!");
		
		IRecurrenceEndDateHandler endHandler = null;
		if("EndBy".equalsIgnoreCase(endDateType)) {
			if(null == dp.getEndDate() || dp.getEndDate().trim().isEmpty())
				throw new IllegalArgumentException("endDateType = 'EndBy' - The value of endDate can not be empty!");
			
			Date endDate = sdf.parse(dp.getEndDate());
			endHandler = new EndDateHandler(startDate, endDate);
		}else if("EndAfter".equalsIgnoreCase(endDateType)) {			
			if(null == dp.getOccurences() || dp.getOccurences() <= 0)
				throw new IllegalArgumentException("endDateType = 'EndAfter' - The value of occurences must be greater than zero!");
			
			endHandler = new OccurencesEndDateHandler(dp.getOccurences());
		}else {
			throw new IllegalArgumentException("The value of endDateType can only be 'EndBy' or 'EndAfter'!");
		}
		
		IRecurrenceHandler handler = null;
		if("Daily".equalsIgnoreCase(patternType)){
			String dailyType = dp.getDailyType();
			if(null == dailyType || dailyType.trim().isEmpty())
				throw new IllegalArgumentException("patternType = 'Daily' - The value of dailyType can not be empty!");
			
			if("Days".equalsIgnoreCase(dailyType)) {
				if(null == dp.getDay() || dp.getDay() <= 0)
					throw new IllegalArgumentException("patternType = 'Daily', dailyType = 'Days' - The value of day must be greater than zero!");
				
				handler = new DailyEveryDaysHandler(startDate, dp.getDay());
			}else if("WeekDay".equalsIgnoreCase(dailyType)) {
				handler = new DailyEveryWeekdayHandler(startDate);
			}else {
				throw new IllegalArgumentException("patternType = 'Daily' - The value of dailyType can only be 'Days' or 'WeekDay'!");
			}
		}else if("Weekly".equalsIgnoreCase(patternType)) {
			if(null == dp.getDays() || dp.getDays().length <= 0)
				throw new IllegalArgumentException("patternType = 'Weekly' - The value of days can not be empty!");
			
			if(null == dp.getWeek() || dp.getWeek() <= 0)
				throw new IllegalArgumentException("patternType = 'Weekly' - The value of week must be greater than zero!");
			
			handler = new WeeklyHandler(startDate, dp.getWeek(), dp.getDays());
		}else if("Monthly".equalsIgnoreCase(patternType)) {
			String monthlyType = dp.getMonthlyType();
			if(null == monthlyType || monthlyType.trim().isEmpty())
				throw new IllegalArgumentException("patternType = 'Monthly' - The value of monthlyType can not be empty!");
			
			if("DayOfEveryMonth".equalsIgnoreCase(monthlyType)) {
				if(null == dp.getDay() || dp.getDay() <= 0)
					throw new IllegalArgumentException("patternType = 'Monthly', monthlyType = 'DayOfEveryMonth' - The value of day must be greater than zero!");
				
				if(null == dp.getMonth() || dp.getMonth() <= 0)
					throw new IllegalArgumentException("patternType = 'Monthly', monthlyType = 'DayOfEveryMonth' - The value of month must be greater than zero!");
				
				handler = new MonthlyEveryMonthHandler(startDate, dp.getDay(), dp.getMonth());
			}else if("TheDayOfEveryMonth".equalsIgnoreCase(monthlyType)) {
				if(null == dp.getSeq())
					throw new IllegalArgumentException("patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth' - The value of seq can not be null!");
				
				if(dp.getSeq() != -1 && dp.getSeq() != 1 && dp.getSeq() != 2 && dp.getSeq() != 3 && dp.getSeq() != 4)
					throw new IllegalArgumentException("patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth' - The value of seq can only be -1, 1, 2, 3, or 4!");
				
				if(null == dp.getMonth() || dp.getMonth() <= 0)
					throw new IllegalArgumentException("patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth' - The value of month must be greater than zero!");
				
				if(null == dp.getDayType())
					throw new IllegalArgumentException("patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth' - The value of dayType can not be null!");
				
				if(dp.getDayType() < 1 || dp.getDayType() > 10)
					throw new IllegalArgumentException("patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth' - The value of dayType can only be 1~10!");
				
				if(null == dp.getPlusOrMinus() || dp.getPlusOrMinus() == 0) {
					handler = new MonthlyTheMonthHandler(startDate, dp.getSeq(), dp.getDayType(), dp.getMonth());
				}else {
					handler = new MonthlyTheMonthHandler(startDate, dp.getSeq(), dp.getDayType(), dp.getMonth(), dp.getPlusOrMinus());
				}
			}else {
				throw new IllegalArgumentException("patternType = 'Monthly' - The value of monthlyType can only be 'DayOfEveryMonth' or 'TheDayOfEveryMonth' !");
			}
		}else if("Yearly".equalsIgnoreCase(patternType)) {
			String yearlyType = dp.getYearlyType();
			if(null == yearlyType || yearlyType.trim().isEmpty())
				throw new IllegalArgumentException("patternType = 'Yearly' - The value of yearlyType can not be empty!");
			
			if("DayOfEveryYear".equalsIgnoreCase(yearlyType)) {
				if(null == dp.getDay() || dp.getDay() <= 0)
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'DayOfEveryYear' - The value of day must be greater than zero!");
				
				if(null == dp.getMonth())
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'DayOfEveryYear' - The value of month can not be null!");
				
				if(dp.getMonth() < 0 || dp.getMonth() > 11)
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'DayOfEveryYear' - The value of month can only be 0~11!");
				
				handler = new YearlyEveryYearHandler(startDate, dp.getMonth(), dp.getDay());
			}else if("TheDayOfEveryYear".equalsIgnoreCase(yearlyType)) {
				if(null == dp.getSeq())
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear' - The value of seq can not be null!");
				
				if(dp.getSeq() != -1 && dp.getSeq() != 1 && dp.getSeq() != 2 && dp.getSeq() != 3 && dp.getSeq() != 4)
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear' - The value of seq can only be -1, 1, 2, 3, or 4!");
				
				if(null == dp.getDayType())
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear' - The value of dayType can not be null!");
				
				if(dp.getDayType() < 1 || dp.getDayType() > 10)
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear' - The value of dayType can only be 1~10!");
				
				if(null == dp.getMonth())
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear' - The value of month can not be null!");
			
				if(dp.getMonth() < 0 || dp.getMonth() > 11)
					throw new IllegalArgumentException("patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear' - The value of month can only be 0~11!");
				
				handler = new YearlyTheYearHandler(startDate, dp.getSeq(), dp.getDayType(), dp.getMonth());
			}else {
				throw new IllegalArgumentException("patternType = 'Yearly' - The value of yearlyType can only be 'DayOfEveryYear' or 'TheDayOfEveryYear' !");
			}
		}else {
			throw new IllegalArgumentException("The value of patternType can only be 'Daily' or 'Weekly' or 'Monthly' or 'Yearly'!");
		}
		
		Recurrence rec = new Recurrence();
		rec.setHandler(handler);
		rec.setEndDateHandler(endHandler);
		List<Date> dates = rec.getDate();
		if(dates.size() > 2000)
			throw new IllegalArgumentException("The total number of date selected by pattern can not exceed 2000!");
		
		List<String> dateList = new ArrayList<String>();
		
		for(Date d : dates) {
			dateList.add(sdf.format(d));
		}
		
		return dateList;
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
