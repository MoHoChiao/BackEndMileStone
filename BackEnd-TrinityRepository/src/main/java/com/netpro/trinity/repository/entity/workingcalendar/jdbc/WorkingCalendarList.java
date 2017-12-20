package com.netpro.trinity.repository.entity.workingcalendar.jdbc;

public class WorkingCalendarList {
	private String wcalendaruid;
	private Integer yearnum;
	private Integer monthnum;
	private Integer daynum;

	public String getWcalendaruid() {
		return wcalendaruid;
	}

	public void setWcalendaruid(String wcalendaruid) {
		this.wcalendaruid = wcalendaruid;
	}

	public Integer getYearnum() {
		return yearnum;
	}

	public void setYearnum(Integer yearnum) {
		this.yearnum = yearnum;
	}

	public Integer getMonthnum() {
		return monthnum;
	}

	public void setMonthnum(Integer monthnum) {
		this.monthnum = monthnum;
	}

	public Integer getDaynum() {
		return daynum;
	}

	public void setDaynum(Integer daynum) {
		this.daynum = daynum;
	}
	
	@Override
	public String toString() {
		return "WorkingCalendarList{" + "wcalendaruid='" + wcalendaruid + '\'' + ", "
				+ "yearnum='" + yearnum + '\'' + ", "
				+ "monthnum='" + monthnum + '\'' + ", "
				+ "daynum='" + daynum + '\'' + '}';
	}
}