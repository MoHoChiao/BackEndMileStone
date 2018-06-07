package com.netpro.trinity.repository.frequency.lib;

import java.util.Calendar;
import java.util.Date;

public class YearlyTheYearHandler extends Handler{

	private Integer seq, dayType, month;
	
	public YearlyTheYearHandler(Date startDate, Integer seq, Integer dayType, Integer month) {
		super(startDate);
		this.seq = seq;
		this.dayType = dayType;
		this.month = month;
	}

	@Override
	public Date getNextDate() {
		if(isFirst) {
			isFirst = false;
			
			Long startLong = calendar.getTime().getTime();
			addYear(-1);
			Date d = getNextYear();
			Long nextLong = d.getTime();
			if(startLong <= nextLong) {
				return d;
			}else {
				return getNextYear();
			}
		}else {
			return getNextYear();
		}
	}
	
	private Date getNextYear() {
		addYear(1);
		
		calendar.set(Calendar.MONTH, month);
		
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
		
		return calendar.getTime();
	}
}
