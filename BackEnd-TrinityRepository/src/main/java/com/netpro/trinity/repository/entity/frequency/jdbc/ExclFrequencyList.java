package com.netpro.trinity.repository.entity.frequency.jdbc;

public class ExclFrequencyList {
	private String excludefrequencyuid;
	private Integer seq;
	private Long starttime;
	private Long endtime;

	public String getExcludefrequencyuid() {
		return excludefrequencyuid;
	}

	public void setExcludefrequencyuid(String excludefrequencyuid) {
		this.excludefrequencyuid = excludefrequencyuid;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Long getStarttime() {
		return starttime;
	}

	public void setStarttime(Long starttime) {
		this.starttime = starttime;
	}

	public Long getEndtime() {
		return endtime;
	}

	public void setEndtime(Long endtime) {
		this.endtime = endtime;
	}
	
	@Override
	public String toString() {
		return "ExclFrequencyList{" + "excludefrequencyuid='" + excludefrequencyuid + '\'' + ", "
				+ "seq='" + seq + '\'' + ", "
				+ "starttime='" + starttime + '\'' + ", "
				+ "endtime='" + endtime + '\'' + '}';
	}
}