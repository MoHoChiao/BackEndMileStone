package com.netpro.trinity.service.frequency.util;

import java.util.Calendar;

public class DateUtil {
	protected void addDay(Calendar cal, int days) {
		cal.add(Calendar.DAY_OF_MONTH, days);
	}
	
	protected void addWeekOfYear(Calendar cal, int weeks) {
		cal.add(Calendar.WEEK_OF_YEAR, weeks);
	}
	
	protected void addMonth(Calendar cal, int months) {
		cal.add(Calendar.MONTH, months);
	}
	
	protected void addYear(Calendar cal, int years) {
		cal.add(Calendar.YEAR, years);
	}
	
	protected boolean isWeekday(Calendar cal) {
		int i = cal.get(Calendar.DAY_OF_WEEK);
		if(i == Calendar.SATURDAY || i == Calendar.SUNDAY)
			return false;
		else
			return true;
	}
	
	protected boolean isWeekendDay(Calendar cal) {
		int i = cal.get(Calendar.DAY_OF_WEEK);
		if(i == Calendar.SATURDAY || i == Calendar.SUNDAY)
			return true;
		else
			return false;
	}
	
	protected boolean isSunday(Calendar cal) {
		int i = cal.get(Calendar.DAY_OF_WEEK);
		if(i == Calendar.SUNDAY)
			return true;
		else
			return false;
	}
	
	protected boolean isSaturday(Calendar cal) {
		int i = cal.get(Calendar.DAY_OF_WEEK);
		if(i == Calendar.SATURDAY)
			return true;
		else
			return false;
	}
	
	protected int getDayOfWeek(Calendar cal) {
		return cal.get(Calendar.DAY_OF_WEEK);
	}
}
