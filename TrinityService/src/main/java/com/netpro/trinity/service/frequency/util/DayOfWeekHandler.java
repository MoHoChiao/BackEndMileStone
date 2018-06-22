package com.netpro.trinity.service.frequency.util;

import java.util.Calendar;

public class DayOfWeekHandler extends DateUtil implements IPatternHandler{
	
	private Integer seq, dayOfWeek;
	
	public DayOfWeekHandler(Integer seq, Integer dayOfWeek) {
		this.seq = seq;
		this.dayOfWeek = dayOfWeek;
	}
	
	@Override
	public void handlerPattern(Calendar calendar) {
		if(seq == -1) {//last
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			int dw = calendar.get(Calendar.DAY_OF_WEEK);
			while(dw != dayOfWeek) {
				addDay(calendar, -1);
				dw = calendar.get(Calendar.DAY_OF_WEEK);
			}
		}else {//first, second, third, fourth
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			int dw = calendar.get(Calendar.DAY_OF_WEEK);
			while(dw != dayOfWeek) {
				addDay(calendar, 1);
				dw = calendar.get(Calendar.DAY_OF_WEEK);
			}
			
			calendar.add(Calendar.WEEK_OF_MONTH, seq - 1);
		}
	}
}
