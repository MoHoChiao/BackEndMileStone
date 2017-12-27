package com.netpro.trinity.repository.util.datepattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class Handler extends DateUtil implements IRecurrenceHandler{
	public static final int DAY = 8;
	public static final int WEEKDAY = 9;
	public static final int WEEKENDDAY = 10;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	protected Calendar calendar;
	protected boolean isFirst = true;
	
	public Handler(Date startDate) {
		setDate(formatDate(startDate));
	}
	
	protected Date formatDate(Date date) {
		try {
			return sdf.parse(sdf.format(date));
		}catch(ParseException e) {
			return date;
		}
	}
	
	protected void setDate(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
	}
	
	protected void addDay(int days) {
		addDay(calendar, days);
	}
	
	protected void addWeekOfYear(int weeks) {
		addWeekOfYear(calendar, weeks);
	}
	
	protected void addMonth(int months) {
		addMonth(calendar, months);
	}
	
	protected void addYear(int years) {
		addYear(calendar, years);
	}
	
	protected boolean isWeekday() {
		return isWeekday(calendar);
	}
	
	protected boolean isWeekendDay() {
		return isWeekendDay(calendar);
	}
	
	protected boolean isSunday() {
		return isSunday(calendar);
	}
	
	protected boolean isSaturday() {
		return isSaturday(calendar);
	}
	
	protected int getDayOfWeek() {
		return getDayOfWeek(calendar);
	}
}
