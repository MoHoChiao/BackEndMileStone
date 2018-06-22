package com.netpro.trinity.service.frequency.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EndDateHandler implements IRecurrenceEndDateHandler{
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private Date startDate, endDate;
	private Long sl, el;
	
	public EndDateHandler(Date startDate, Date endDate) throws ParseException {
		this.startDate = startDate;
		this.endDate = endDate;
		this.initDateLong();
	}
	
	private void initDateLong() throws ParseException{
		startDate = sdf.parse(sdf.format(startDate));
		sl = startDate.getTime();
		
		endDate = sdf.parse(sdf.format(endDate));
		el = endDate.getTime();
	}
	
	@Override
	public boolean canAdd(List<Date> dateList, Date date) {
		Long inDate = date.getTime();
		if(sl <= inDate && el >= inDate)
			return true;
		else
			return false;
	}
}
