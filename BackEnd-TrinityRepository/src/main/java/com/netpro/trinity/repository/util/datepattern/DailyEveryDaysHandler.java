package com.netpro.trinity.repository.util.datepattern;

import java.util.Date;

public class DailyEveryDaysHandler extends Handler{
	private int days;
	
	public DailyEveryDaysHandler(Date startDate, int days) {
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
