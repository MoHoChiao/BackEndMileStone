package com.netpro.trinity.resource.admin.frequency.util;

import java.util.Calendar;
import java.util.Date;

public class MonthlyTheMonthHandler extends Handler{

	private Integer seq, dayType, recur, increaseDay;
	private boolean hasPassMonth = false;
	
	public MonthlyTheMonthHandler(Date startDate, Integer seq, Integer dayType, Integer recur) {
		super(startDate);
		this.seq = seq;
		this.dayType = dayType;
		this.recur = recur;
	}

	public MonthlyTheMonthHandler(Date startDate, Integer seq, Integer dayType, Integer recur, Integer increaseDay) {
		super(startDate);
		this.seq = seq;
		this.dayType = dayType;
		this.recur = recur;
		this.increaseDay = increaseDay;
	}
	
	@Override
	public Date getNextDate() {
		if(isFirst) {
			isFirst = false;
			
			Long startLong = calendar.getTime().getTime();
			addMonth(0 - recur);
			Date d = getNextMonth();
			Long nextLong = d.getTime();
			if(startLong <= nextLong) {
				return d;
			}else {
				return getNextMonth();
			}
		}else {
			return getNextMonth();
		}
	}

	private Date getNextMonth() {
		if(hasPassMonth) {
			hasPassMonth = false;
			addMonth(recur - 1);
		}else {
			addMonth(recur);
		}
		
		IPatternHandler ph = null;
		
		if(dayType == 8) {//Day
			ph = new DayOfMonthHandler(seq);
		}else if(dayType == 9) {//WeekDay
			ph = new WeekDayOfMonthHandler(seq);
		}else if(dayType == 10) {//WeekendDay
			ph = new WeekendDayOfMonthHandler(seq);
		}else {//Sunday ~ Saturday (1~7)
			ph = new DayOfWeekHandler(seq, dayType);
		}
		
		ph.handlerPattern(calendar);
		
		if(increaseDay != null) {
			int oldMonth = calendar.get(Calendar.MONTH);
			addDay(increaseDay);
			int newMonth = calendar.get(Calendar.MONTH);
			
			if(oldMonth != newMonth)
				hasPassMonth = true;
		}
		
		return calendar.getTime();
	}
}
