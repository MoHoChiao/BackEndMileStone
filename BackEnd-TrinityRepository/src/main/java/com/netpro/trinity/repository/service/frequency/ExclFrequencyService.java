package com.netpro.trinity.repository.service.frequency;

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

import com.netpro.trinity.repository.dao.jpa.frequency.ExclFrequencyJPADao;
import com.netpro.trinity.repository.dto.FilterInfo;
import com.netpro.trinity.repository.dto.Ordering;
import com.netpro.trinity.repository.dto.Paging;
import com.netpro.trinity.repository.dto.Querying;
import com.netpro.trinity.repository.dto.WorkingCalendarPattern;
import com.netpro.trinity.repository.entity.frequency.jdbc.ExclFrequencyList;
import com.netpro.trinity.repository.entity.frequency.jpa.ExclFrequency;
import com.netpro.trinity.repository.util.Constant;
import com.netpro.trinity.repository.util.datepattern.DailyEveryDaysHandler;
import com.netpro.trinity.repository.util.datepattern.DailyEveryWeekdayHandler;
import com.netpro.trinity.repository.util.datepattern.EndDateHandler;
import com.netpro.trinity.repository.util.datepattern.IRecurrenceEndDateHandler;
import com.netpro.trinity.repository.util.datepattern.IRecurrenceHandler;
import com.netpro.trinity.repository.util.datepattern.MonthlyEveryMonthHandler;
import com.netpro.trinity.repository.util.datepattern.MonthlyTheMonthHandler;
import com.netpro.trinity.repository.util.datepattern.OccurencesEndDateHandler;
import com.netpro.trinity.repository.util.datepattern.Recurrence;
import com.netpro.trinity.repository.util.datepattern.WeeklyHandler;
import com.netpro.trinity.repository.util.datepattern.YearlyEveryYearHandler;
import com.netpro.trinity.repository.util.datepattern.YearlyTheYearHandler;

@Service
public class ExclFrequencyService {
	public static final String[] EXCL_FIELD_VALUES = new String[] { "excludefrequencyname", "activate", "description"};
	public static final Set<String> EXCL_FIELD_SET = new HashSet<>(Arrays.asList(EXCL_FIELD_VALUES));
	
	@Autowired
	private ExclFrequencyJPADao dao;
	
	@Autowired
	private ExclFrequencyListService listService;
	@Autowired
	private FrequencyService freqService;
	
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
		if(excl == null)
			throw new IllegalArgumentException("Exclude Frequency UID does not exist!(" + uid + ")");
		
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
//		if(null == uid || uid.trim().length() <= 0)
//			throw new IllegalArgumentException("Exclude Frequency Uid can not be empty!");
//				
//		if(!freqService.existByWCalendaruid(uid)) {
//			this.listService.deleteByExclFreqUid(uid);
//			this.dao.delete(uid);
//		}else {
//			throw new IllegalArgumentException("Referenceing by frequency");
//		}
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
