package com.netpro.trinity.resource.admin.prop.dto;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.netpro.trinity.resource.admin.drivermanager.dto.DriverInfo;

@Configuration
@ConfigurationProperties(prefix="trinity-data-jdbc")
public class TrinityDataJDBC
{
	private Map<String, DriverInfo> info = new TreeMap<String, DriverInfo>(String.CASE_INSENSITIVE_ORDER);
	    
    public void setInfo(Map<String, DriverInfo> info) {
		this.info = info;
	}
	public Map<String, DriverInfo> getInfo() {
		return info;
	}
}