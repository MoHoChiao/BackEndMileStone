package com.netpro.trinity.repository.entity.frequency.jdbc;

public class FrequencyRelation {
	private String freqcategoryuid;
	private String frequencyuid;

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
	
	@Override
	public String toString() {
		return "FrequencyRelation{" + "freqcategoryuid='" + freqcategoryuid + '\'' + ", "
				+ "frequencyuid='" + frequencyuid + '\'' + '}';
	}
}