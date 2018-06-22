package com.netpro.trinity.service.frequency.util;

import java.util.Date;

public class DailyEveryDaysHandler extends Handler{
	private Integer days;
	
	public DailyEveryDaysHandler(Date startDate, Integer days) {
		super(startDate);
		this.days = days;
	}

	@Override
	public Date getNextDate() {
		if(isFirst) {
			isFirst = false;
		}else {
			addDay(days);
		}
		return calendar.getTime();
	}
}
