package com.netpro.trinity.repository.frequency.lib;

import java.util.Date;
import java.util.List;

public class OccurencesEndDateHandler implements IRecurrenceEndDateHandler{
	private int count;
	
	public OccurencesEndDateHandler(int count) {
		this.count = count;
	}
	
	@Override
	public boolean canAdd(List<Date> dateList, Date date) {
		if(dateList.size() < count) {
			return true;
		}else {
			return false;
		}		
	}

}
