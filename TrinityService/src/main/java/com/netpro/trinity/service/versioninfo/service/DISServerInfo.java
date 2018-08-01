package com.netpro.trinity.service.versioninfo.service;

import java.util.Map;

public class DISServerInfo extends JarInfo {

	public DISServerInfo(String trinityHome) {
		super(trinityHome);
	}
	
	public Map<String, Map<String, String>> getDataInfo() {
		
		return scanTrinityJar("DISServer/data/software");
	}
	
	public Map<String, Map<String, String>> getLibInfo() {
		
		Map<String, Map<String, String>> libMap = scanTrinityJar("DISServer/lib");
		Map<String, Map<String, String>> appMap = scanTrinityJar("DISServer/lib/app");
		Map<String, Map<String, String>> commonMap = scanTrinityJar("DISServer/lib/common");
		Map<String, Map<String, String>> coreMap = scanTrinityJar("DISServer/lib/core");
		Map<String, Map<String, String>> pluginMap = scanTrinityJar("DISServer/lib/plugin");
		Map<String, Map<String, String>> serviceMap = scanTrinityJar("DISServer/lib/service");
		Map<String, Map<String, String>> hadoop1Map = scanTrinityJar("DISServer/lib/bigdata/hadoop1.0.0");
		Map<String, Map<String, String>> hadoop2Map = scanTrinityJar("DISServer/lib/bigdata/hadoop2.0.0");
		
		libMap.putAll(appMap);
		libMap.putAll(commonMap);
		libMap.putAll(coreMap);
		libMap.putAll(pluginMap);
		libMap.putAll(serviceMap);
		libMap.putAll(hadoop1Map);
		libMap.putAll(hadoop2Map);
		
		return libMap;
	}
	
	public Map<String, Map<String, String>> getServiceInfo() {
		
		return scanTrinityJar("DISServer/service");
	}
	
	public Map<String, Map<String, String>> getWebAppInfo() {
		
		return scanTrinityJar("DISServer/webapp");
	}
}
