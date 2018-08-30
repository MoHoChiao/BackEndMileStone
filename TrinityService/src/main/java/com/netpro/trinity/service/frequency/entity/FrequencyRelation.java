package com.netpro.trinity.service.frequency.entity;

public class FrequencyRelation {
	private String freqcategoryuid;
	private String frequencyuid;
	private String freqcategoryname;

	public String getFreqcategoryuid() {
		return freqcategoryuid;
	}
	public void setFreqcategoryuid(String freqcategoryuid) {
		this.freqcategoryuid = freqcategoryuid;
	}
	public String getFrequencyuid() {
		return frequencyuid;
	}
	public void setFrequencyuid(String frequencyuid) {
		this.frequencyuid = frequencyuid;
	}
	public String getFreqcategoryname() {
		return freqcategoryname;
	}
	public void setFreqcategoryname(String freqcategoryname) {
		this.freqcategoryname = freqcategoryname;
	}
	@Override
	public String toString() {
		return "FrequencyRelation{" + "freqcategoryuid='" + freqcategoryuid + '\'' + ", "
				+ "frequencyuid='" + frequencyuid + '\'' + '}';
	}
}