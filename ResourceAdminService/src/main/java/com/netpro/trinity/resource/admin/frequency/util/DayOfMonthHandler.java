package com.netpro.trinity.resource.admin.frequency.util;

import java.util.Calendar;

public class DayOfMonthHandler implements IPatternHandler{
	
	private Integer seq;
	
	public DayOfMonthHandler(Integer seq) {
		this.seq = seq;
	}
	
	@Override
	public void handlerPattern(Calendar calendar) {
		if(seq == -1) {//last
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		}else {//first, second, third, fourth
			calendar.set(Calendar.DAY_OF_MONTH, seq);
		}
	}

}
