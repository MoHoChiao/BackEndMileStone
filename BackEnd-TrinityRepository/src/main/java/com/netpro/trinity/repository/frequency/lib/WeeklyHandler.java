package com.netpro.trinity.repository.frequency.lib;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WeeklyHandler extends Handler{
	private int recur;
	private List<Integer> dailyList;
	
	public WeeklyHandler(Date startDate, int recur, Integer ... week) {
		super(startDate);
		this.recur = recur;
		
		if(recur <= 0)
			recur = 1;
		
		dailyList = Arrays.asList(week);
	}

	@Override
	public Date getNextDate() {
		if(isFirst) {
			isFirst = false;
		}else {
			nextDay();
		}
		
		if(dailyList.contains(getDayOfWeek())) {
			return calendar.getTime();
		}else {
			while(true) {
				nextDay();
				if(dailyList.contains(getDayOfWeek())) {
					return calendar.getTime();
				}
			}
		}
	}
	
	private void nextDay() {
		if(isSaturday()) {
			addWeekOfYear(recur - 1);
		}
		addDay(1);
	}
}
