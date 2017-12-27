package com.netpro.trinity.repository.util.datepattern;

import java.util.Date;
import java.util.List;

public interface IRecurrenceEndDateHandler {
	public boolean canAdd(List<Date> dateList, Date date);
}
