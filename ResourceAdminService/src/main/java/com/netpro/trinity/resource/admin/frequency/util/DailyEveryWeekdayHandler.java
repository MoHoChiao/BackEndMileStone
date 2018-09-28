package com.netpro.trinity.resource.admin.frequency.util;

import java.util.Date;

public class DailyEveryWeekdayHandler extends Handler{

	public DailyEveryWeekdayHandler(Date startDate) {
		super(startDate);
	}

	@Override
	public Date getNextDate() {
		if(isFirst) {
			isFirst = false;
		}else {
			addDay(1);
		}
		
		while(!isWeekday()) {
			addDay(1);
		}
		
		return calendar.getTime();
	}
}
