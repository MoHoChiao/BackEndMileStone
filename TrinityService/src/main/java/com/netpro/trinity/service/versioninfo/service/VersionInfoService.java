package com.netpro.trinity.service.versioninfo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

@Service
public class VersionInfoService {

	private String trinityHome = "C:/Trinity411113";
	
	public List<ModuleInfo> findJCSServer() {
		List<ModuleInfo> list = new ArrayList<>();
		JCSServerInfo serverInfo = new JCSServerInfo(trinityHome);
		
		Map<String, Map<String, String>> binMap = serverInfo.getBinInfo();
		list.addAll(parseModuleInfo("bin", binMap));
		
		Map<String, Map<String, String>> libMap = serverInfo.getLibInfo();
		list.addAll(parseModuleInfo("lib", libMap));
		
		return list;
	}
	
	public List<ModuleInfo> findJCSAgent() {
		List<ModuleInfo> list = new ArrayList<>();
		JCSAgentInfo agentInfo = new JCSAgentInfo(trinityHome);
		
		Map<String, Map<String, String>> binMap = agentInfo.getBinInfo();
		list.addAll(parseModuleInfo("bin", binMap));
		
		Map<String, Map<String, String>> libMap = agentInfo.getLibInfo();
		list.addAll(parseModuleInfo("lib", libMap));
		
		return list;
	}
	
	public List<ModuleInfo> findDISServer() {
		List<ModuleInfo> list = new ArrayList<>();
		DISServerInfo disInfo = new DISServerInfo(trinityHome);
		
		Map<String, Map<String, String>> dataMap = disInfo.getDataInfo();
		list.addAll(parseModuleInfo("data", dataMap));
		
		Map<String, Map<String, String>> libMap = disInfo.getLibInfo();
		list.addAll(parseModuleInfo("lib", libMap));
		
		Map<String, Map<String, String>> serviceMap = disInfo.getServiceInfo();
		list.addAll(parseModuleInfo("service", serviceMap));
		
		Map<String, Map<String, String>> webApMap = disInfo.getWebAppInfo();
		list.addAll(parseModuleInfo("webapp", webApMap));
		
		return list;
	}
	
	private List<ModuleInfo> parseModuleInfo(String path, Map<String, Map<String, String>> map) {
		List<ModuleInfo> list = new ArrayList<>();
		
		for (Entry<String, Map<String, String>> en : map.entrySet()) {
			ModuleInfo mi = getJarAttribute(en.getKey(), en.getValue());
			mi.setPath(path);
			
			list.add(mi);
		}
		
		return list;
	}
	
	private ModuleInfo getJarAttribute(String jarName, Map<String, String> attrMap) {
		ModuleInfo mi = new ModuleInfo();
		
		String title = "";
		String version = "";
		String buildTime = "";
		
		if (attrMap == null) {
			return mi;
		}
		
		if (attrMap.containsKey("Implementation-Title")) {
			title = attrMap.get("Implementation-Title");
		} else if (attrMap.containsKey("Specification-Title")) {
			title = attrMap.get("Specification-Title");
		} else if (attrMap.containsKey("Bundle-SymbolicName")) {
			title = attrMap.get("Bundle-SymbolicName");
		} else {
			title = jarName;
		}
		
		if (attrMap.containsKey("Specification-Version")) {
			version = attrMap.get("Specification-Version");
		} else if (attrMap.containsKey("Implementation-Version")) {
			version = attrMap.get("Implementation-Version");
		} else if (attrMap.containsKey("Bundle-Version")) {
			version = attrMap.get("Bundle-Version");
		}
		
		if (attrMap.containsKey("Build-Time-ISO-8601")) {
			buildTime = attrMap.get("Build-Time-ISO-8601");
		}
		
		mi.setName(title);
		mi.setVersion(version);
		mi.setBuildTime(buildTime);
		
		return mi;
	}
	
}
