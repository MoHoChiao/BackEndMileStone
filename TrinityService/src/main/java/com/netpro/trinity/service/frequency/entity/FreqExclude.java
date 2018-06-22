package com.netpro.trinity.service.frequency.entity;

public class FreqExclude {
	private String frequencyuid;
	private String excludefrequencyuid;
	
	private String frequencyname;
	private String freqcategoryname;
	
	public String getFrequencyuid() {
		return frequencyuid;
	}

	public void setFrequencyuid(String frequencyuid) {
		this.frequencyuid = frequencyuid;
	}

	public String getExcludefrequencyuid() {
		return excludefrequencyuid;
	}

	public void setExcludefrequencyuid(String excludefrequencyuid) {
		this.excludefrequencyuid = excludefrequencyuid;
	}
	
	public String getFrequencyname() {
		return frequencyname;
	}

	public void setFrequencyname(String frequencyname) {
		this.frequencyname = frequencyname;
	}

	public String getFreqcategoryname() {
		return freqcategoryname;
	}

	public void setFreqcategoryname(String freqcategoryname) {
		this.freqcategoryname = freqcategoryname;
	}
	
	@Override
	public String toString() {
		return "FreqExclude{" + "frequencyuid='" + frequencyuid + '\'' + ", "
				+ "excludefrequencyuid='" + excludefrequencyuid + '\'' + '}';
	}
}