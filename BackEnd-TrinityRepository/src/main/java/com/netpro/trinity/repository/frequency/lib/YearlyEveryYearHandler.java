package com.netpro.trinity.repository.frequency.lib;

import java.util.Calendar;
import java.util.Date;

public class YearlyEveryYearHandler extends Handler{

	private Integer month, day;
	
	public YearlyEveryYearHandler(Date startDate, Integer month, Integer day) {
		super(startDate);
		this.month = month;
		this.day = day;
	}

	@Override
	public Date getNextDate() {
		if(isFirst) {
			isFirst = false;
			int nowMonth = calendar.get(Calendar.MONTH);
			int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
			
			if(nowMonth <= month) {
				calendar.set(Calendar.MONTH, month);
				
				if(nowMonth < month || nowDay <= day) {
					calendar.set(Calendar.DAY_OF_MONTH, day);
				}else {
					return getNextYear();
				}
				
				return calendar.getTime();
			}else {
				return getNextYear();
			}
		}else {
			return getNextYear();
		}
	}

	private Date getNextYear() {
		addYear(1);
		calendar.set(Calendar.MONTH,  month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
}
