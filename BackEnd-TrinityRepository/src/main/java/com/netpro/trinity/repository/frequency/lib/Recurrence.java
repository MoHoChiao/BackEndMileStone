package com.netpro.trinity.repository.frequency.lib;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recurrence {
	private IRecurrenceEndDateHandler endDateHandler;
	private IRecurrenceHandler handler;
	
	private List<Date> dateList;
	
	public Recurrence() {
		dateList = new ArrayList<Date>();
	}
	
	private boolean addDate(Date date) {
		if(endDateHandler == null)
			return false;
		
		if(endDateHandler.canAdd(dateList, date)) {
			dateList.add(date);
			return true;
		}else {
			return false;
		}
	}
	
	public List<Date> getDate(){
		startRecurrenceDate();
		return dateList;
	}
	
	private void startRecurrenceDate() {
		if(handler == null)
			return;
		
		while(true) {
			Date date = handler.getNextDate();
			if(date != null) {
				boolean b = addDate(date);
				if(!b) {
					break;
				}
			}else {
				break;
			}
		}
	}
	
	public void setEndDateHandler(IRecurrenceEndDateHandler endHandler) {
		this.endDateHandler = endHandler;
	}
	
	public void setHandler(IRecurrenceHandler handler) {
		this.handler = handler;
	}
}
