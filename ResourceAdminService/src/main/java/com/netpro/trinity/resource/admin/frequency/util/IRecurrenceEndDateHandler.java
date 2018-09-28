package com.netpro.trinity.resource.admin.frequency.util;

import java.util.Date;
import java.util.List;

public interface IRecurrenceEndDateHandler {
	public boolean canAdd(List<Date> dateList, Date date);
}
