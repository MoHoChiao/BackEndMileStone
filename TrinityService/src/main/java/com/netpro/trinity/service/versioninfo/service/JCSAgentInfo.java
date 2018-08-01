package com.netpro.trinity.service.versioninfo.service;

import java.util.Map;

public class JCSAgentInfo extends JarInfo {
	
	public JCSAgentInfo(String trinityHome) {
		super(trinityHome);
	}
	
	public Map<String, Map<String, String>> getBinInfo() {
		
		return scanTrinityJar("JCSAgent/bin");
	}
	
	public Map<String, Map<String, String>> getLibInfo() {
		
		Map<String, Map<String, String>> libMap = scanTrinityJar("JCSAgent/lib");
		Map<String, Map<String, String>> pluginMap = scanTrinityJar("JCSAgent/lib/plugin");
		Map<String, Map<String, String>> hadoop1Map = scanTrinityJar("JCSAgent/lib/bigdata/hadoop1.0.0");
		Map<String, Map<String, String>> hadoop2Map = scanTrinityJar("JCSAgent/lib/bigdata/hadoop2.0.0");
		Map<String, Map<String, String>> hbaseMap = scanTrinityJar("JCSAgent/lib/bigdata/hbase0.92.1");
		
		libMap.putAll(pluginMap);
		libMap.putAll(hadoop1Map);
		libMap.putAll(hadoop2Map);
		libMap.putAll(hbaseMap);
		
		return libMap;
	}
}
