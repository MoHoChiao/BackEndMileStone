package com.netpro.trinity.service.frequency.entity;

public class WorkingCalendarPattern {
	private String startDate;	//valid format ex: 2018-03-15
	private String endDate;	//valid format ex: 2018-11-03, 若endDateType為'EndAfter'時, 此值無用
	private Integer occurences;	//valid value is great than zero, 若endDateType為'EndBy'時, 此值無用
	private String patternType;	//valid value is 'Daily' or 'Weekly' or 'Monthly' or 'Yearly', 不用區分大小寫
	private String endDateType;	//valid value is 'EndBy' or 'EndAfter', 不用區分大小寫
	private String dailyType;	//valid value is 'Days' or 'WeekDay', 不用區分大小寫, 此值在patternType = 'Daily'時為有用
	/*
	 * valid value is great than zero
	 * 此值在patternType = 'Daily', dailyType = 'Days'時為有用
	 * 此值在patternType = 'Monthly', monthlyType = 'DayOfEveryMonth'時為有用
	 * 此值在patternType = 'Yearly', yearlyType = 'DayOfEveryYear'時為有用
	 */
	private Integer day;
	private Integer[] days;	//valid value is 1~7 (星期日~星期六), 此值在patternType = 'Weekly'時為有用
	private Integer week;	//valid value is great than zero, 此值在patternType = 'Weekly'時為有用
	private String monthlyType;	//valid value is 'DayOfEveryMonth' or 'TheDayOfEveryMonth', 此值在patternType = 'Monthly'時為有用
	/*
	 * valid value is great than zero
	 * 此值在patternType = 'Monthly', monthlyType = 'DayOfEveryMonth'時為有用
	 * 此值在patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth'時為有用
	 * 此值在patternType = 'Yearly', yearlyType = 'DayOfEveryYear'時為有用
	 * 此值在patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear'時為有用
	 * 此值在patternType = 'Yearly', yearlyType = 'DayOfEveryYear' or 'TheDayOfEveryYear' 的情況下, 其valid value為0~11, 分別代表1月至12月
	 */
	private Integer month;
	/*
	 * valid value is -1(last day), 1(first day), 2(second day), 3(third day), 4(fourth day)
	 * 此值在patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth'時為有用
	 * 此值在patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear'時為有用
	 */
	private Integer seq;
	/*
	 * valid value is 1(Sunday), 2(Monday), 3(Tuesday), 4(Wednesday), 5(Thursday), 6(Friday), 7(Saturday), 8(Day), 9(Weekday), 10(second day)
	 * 此值在patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth'時為有用
	 * 此值在patternType = 'Yearly', yearlyType = 'TheDayOfEveryYear'時為有用
	 */
	private Integer dayType;
	/*
	 * valid value is great than zero
	 * 此值可以為null, 表示沒有plus day或minus day, 注意!若非null值,則一定要合法
	 * 此值在patternType = 'Monthly', monthlyType = 'TheDayOfEveryMonth'時為有用
	 */
	private Integer plusOrMinus;
	private String yearlyType;	//valid value is 'DayOfEveryYear' or 'TheDayOfEveryYear', 此值在patternType = 'Yearly'時為有用
	
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
	public Integer getPlusOrMinus() {
		return plusOrMinus;
	}
	public void setPlusOrMinus(Integer plusOrMinus) {
		this.plusOrMinus = plusOrMinus;
	}
	public Integer getDayType() {
		return dayType;
	}
	public void setDayType(Integer dayType) {
		this.dayType = dayType;
	}
	public String getYearlyType() {
		return yearlyType;
	}
	public void setYearlyType(String yearlyType) {
		this.yearlyType = yearlyType;
	}
}
