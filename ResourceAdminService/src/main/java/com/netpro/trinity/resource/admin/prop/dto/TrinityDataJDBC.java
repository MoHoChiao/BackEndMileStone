package com.netpro.trinity.resource.admin.prop.dto;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.netpro.trinity.resource.admin.drivermanager.dto.DriverInfo;
import com.netpro.trinity.resource.admin.util.DriverFileUtil;

@Component
//@Configuration
//@ConfigurationProperties(prefix = "trinity-data-jdbc")
public class TrinityDataJDBC {
	private Map<String, DriverInfo> info = new TreeMap<String, DriverInfo>(String.CASE_INSENSITIVE_ORDER);

	public TrinityDataJDBC() {
		init();
	}
	
	public void setInfo(Map<String, DriverInfo> info) {
		this.info = info;
	}
	
	public Map<String, DriverInfo> getInfo() {
		return info;
	}
	
	private void init() {
		List<Map<String, String>> list = DriverFileUtil.getAllJdbcDriver();
		
		for (Map<String, String> map : list) {
			DriverInfo obj = new DriverInfo();
			obj.setDriver(map.get("driver"));
			obj.setName(map.get("label"));
			obj.setOwner(map.get("icon"));
			obj.setUrl(map.get("url"));
			
			info.put(map.get("label"), obj);
		}
	}
	
}