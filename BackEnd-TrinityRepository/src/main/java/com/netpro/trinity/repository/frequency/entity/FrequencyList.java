package com.netpro.trinity.repository.frequency.entity;

public class FrequencyList {
	private String frequencyuid;
	private Integer seq;
	private Integer yearnum;
	private Integer monthnum;
	private Integer daynum;
	private Integer weekdaynum;
	private Integer hour;
	private Integer minute;

	public String getFrequencyuid() {
		return frequencyuid;
	}

	public void setFrequencyuid(String frequencyuid) {
		this.frequencyuid = frequencyuid;
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
	
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getWeekdaynum() {
		return weekdaynum;
	}

	public void setWeekdaynum(Integer weekdaynum) {
		this.weekdaynum = weekdaynum;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	
	@Override
	public String toString() {
		return "WorkingCalendarList{" + "wcalendaruid='" + frequencyuid + '\'' + ", "
				+ "monthnum='" + seq + '\'' + ", "
				+ "yearnum='" + yearnum + '\'' + ", "
				+ "monthnum='" + monthnum + '\'' + ", "
				+ "monthnum='" + daynum + '\'' + ", "
				+ "monthnum='" + weekdaynum + '\'' + ", "
				+ "monthnum='" + hour + '\'' + ", "
				+ "daynum='" + minute + '\'' + '}';
	}
}