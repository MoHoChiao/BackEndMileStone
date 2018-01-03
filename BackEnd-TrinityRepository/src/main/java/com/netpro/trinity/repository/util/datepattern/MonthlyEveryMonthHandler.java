package com.netpro.trinity.repository.util.datepattern;

import java.util.Calendar;
import java.util.Date;

public class MonthlyEveryMonthHandler extends Handler{
	private Integer day;
	private Integer recur;
	
	public MonthlyEveryMonthHandler(Date startDate, Integer day, Integer recur) {
		super(startDate);
		this.day = day;
		this.recur = recur;
	}

	@Override
	public Date getNextDate() {
		if(isFirst) {
			isFirst = false;
			int today = calendar.get(Calendar.DAY_OF_MONTH);
			if(today <= day) {
				calendar.set(Calendar.DAY_OF_MONTH, day);
				return calendar.getTime();
			}else {
				addMonth(recur);
				calendar.set(Calendar.DAY_OF_MONTH, day);
				return calendar.getTime();
			}
		}else {
			addMonth(recur);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			return calendar.getTime();
		}
	}
}
