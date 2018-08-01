package com.netpro.trinity.service.versioninfo.service;

import java.io.File;
import java.util.Map;

public class JCSServerInfo extends JarInfo {

	public JCSServerInfo(String trinityHome) {
		super(trinityHome);
	}
	
	public Map<String,Map<String, String>> getBinInfo() {
		
		return scanTrinityJar("JCSServer/bin");
	}
	
	public Map<String, Map<String, String>> getLibInfo() {
		
		Map<String, Map<String, String>> libMap = scanTrinityJar("JCSServer/lib");
		Map<String, String> licenseMap = getManifest(new File(trinityHome, "JCSServer/lib/license.jar"));
		Map<String, String> license2Map = getManifest(new File(trinityHome, "JCSServer/lib/license2.jar"));
		libMap.put("license.jar", licenseMap);
		libMap.put("license2.jar", license2Map);
		
		return libMap;
		
	}
	
}
