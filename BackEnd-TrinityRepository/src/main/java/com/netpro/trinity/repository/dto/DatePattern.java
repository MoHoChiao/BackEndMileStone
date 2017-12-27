package com.netpro.trinity.repository.dto;

public class DatePattern {
	private String startDate;
	private String endDate;
	private Integer occurences;
	private String patternType;
	private String endDateType;
	private String dailyType;
	private Integer day;
	private Integer[] days;
	private Integer week;
	private String monthlyType;
	private Integer month;
	private Integer seq;
	private Integer dayType;
	private String plusOrMinus;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPatternType() {
		return patternType;
	}
	public void setPatternType(String patternType) {
		this.patternType = patternType;
	}
	public String getEndDateType() {
		return endDateType;
	}
	public void setEndDateType(String endDateType) {
		this.endDateType = endDateType;
	}
	public String getDailyType() {
		return dailyType;
	}
	public void setDailyType(String dailyType) {
		this.dailyType = dailyType;
	}
	public Integer[] getDays() {
		return days;
	}
	public void setDays(Integer[] days) {
		this.days = days;
	}
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public String getMonthlyType() {
		return monthlyType;
	}
	public void setMonthlyType(String monthlyType) {
		this.monthlyType = monthlyType;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getOccurences() {
		return occurences;
	}
	public void setOccurences(Integer occurences) {
		this.occurences = occurences;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getPlusOrMinus() {
		return plusOrMinus;
	}
	public void setPlusOrMinus(String plusOrMinus) {
		this.plusOrMinus = plusOrMinus;
	}
	public Integer getDayType() {
		return dayType;
	}
	public void setDayType(Integer dayType) {
		this.dayType = dayType;
	}
}
