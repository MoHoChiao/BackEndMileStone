package com.netpro.trinity.resource.admin.prop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.resource.admin.prop.dto.TrinityPropSetting;
import com.netpro.trinity.resource.admin.prop.dto.TrinityPropSetting.App;

@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-prop-setting")
public class TrinityPropSettingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityPropSettingController.class);
		
	@Autowired
	private TrinityPropSetting trinityProp;
	
	@GetMapping("/find-all-apps")
	public ResponseEntity<?> findAllApps() {
		try {
			String disHost = trinityProp.getServer().getDisHost();
			String disPort = trinityProp.getServer().getDisPort();
			String microserviceHost = trinityProp.getServer().getMicroserviceHost();
			String microservicePort = trinityProp.getServer().getMicroservicePort();
			for(App app : trinityProp.getApps()) {
				String host = disHost;
				String port = disPort;
				if(app.getName().trim().equals("ResourceSetter")) {
					host = microserviceHost;
					port = microservicePort;
				}
				
				if(app.getUrl().indexOf("http://") == -1)
					app.setUrl("http://"+host+":"+port+"/"+app.getUrl());
			}
			return ResponseEntity.ok(trinityProp.getApps());
		}catch (Exception e) {
			TrinityPropSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
	
	@GetMapping("/find-service-info")
	public ResponseEntity<?> findServiceInfo() {
		try {
			return ResponseEntity.ok(trinityProp.getServer());
		}catch (Exception e) {
			TrinityPropSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
	
	@GetMapping("/find-encrypt-key")
	public ResponseEntity<?> findEncryptKey() {
		try {
			return ResponseEntity.ok(trinityProp.getEncrypt().getKey());
		}catch (Exception e) {
			TrinityPropSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
}
