package com.netpro.trinity.repository.frequency.lib;

import java.util.Calendar;

public class WeekendDayOfMonthHandler extends DateUtil implements IPatternHandler{
	
	private Integer seq;
	
	public WeekendDayOfMonthHandler(Integer seq) {
		this.seq = seq;
	}
	
	@Override
	public void handlerPattern(Calendar calendar) {
		if(seq == -1) {//last
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			while(!isWeekendDay(calendar)) {
				addDay(calendar, -1);
			}
		}else {//first, second, third, fourth
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			int weekendDayCount = 0;
			while(seq != weekendDayCount) {
				if(isWeekendDay(calendar)) {
					weekendDayCount++;
					
					if(seq != weekendDayCount) {
						addDay(calendar, 1);
					}
				}else {
					addDay(calendar, 1);
				}
			}
		}
	}
}
