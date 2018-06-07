package com.netpro.trinity.repository.drivermanager.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetadataDriverMaintain {

	@Autowired
	MetadataDriverManager manager;
	
	public void load() throws Exception {
		manager.load();
	}

	public void unload() throws Exception {
		manager.unload();
	}
	
	public void load(String target) throws Exception {
		manager.load(target);
	}
	
	public void unload(String target) throws Exception {
		manager.unload(target);
	}

}
