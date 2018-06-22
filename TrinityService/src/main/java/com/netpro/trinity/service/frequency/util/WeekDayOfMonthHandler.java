package com.netpro.trinity.service.frequency.util;

import java.util.Calendar;

public class WeekDayOfMonthHandler extends DateUtil implements IPatternHandler{
	
	private Integer seq;
	
	public WeekDayOfMonthHandler(Integer seq) {
		this.seq = seq;
	}
	
	@Override
	public void handlerPattern(Calendar calendar) {
		if(seq == -1) {//last
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			while(!isWeekday(calendar)) {
				addDay(calendar, -1);
			}
		}else {//first, second, third, fourth
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			int weekdayCount = 0;
			while(seq != weekdayCount) {
				if(isWeekday(calendar)) {
					weekdayCount++;
					
					if(seq != weekdayCount) {
						addDay(calendar, 1);
					}
				}else {
					addDay(calendar, 1);
				}
			}
		}
	}
}
