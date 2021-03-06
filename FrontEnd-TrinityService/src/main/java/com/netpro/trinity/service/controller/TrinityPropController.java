package com.netpro.trinity.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.service.dto.prop.TrinityPropSetting;
import com.netpro.trinity.service.dto.prop.TrinityPropSetting.App;
import com.netpro.trinity.service.util.tool.ExceptionMsgFormat;

@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-prop-setting")
public class TrinityPropController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityPropController.class);
		
	@Autowired
	private TrinityPropSetting trinityProp;
	
	@GetMapping("/find-all-apps")
	public ResponseEntity<?> findAllApps() {
		String methodKey = "TrinityPropController#findAllApps(...)";
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
			TrinityPropController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
	    }
	}
	
	@GetMapping("/find-service-info")
	public ResponseEntity<?> findServiceInfo() {
		String methodKey = "TrinityPropController#findServiceInfo(...)";
		try {
			return ResponseEntity.ok(trinityProp.getServer());
		}catch (Exception e) {
			TrinityPropController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
	    }
	}
	
	@GetMapping("/find-encrypt-key")
	public ResponseEntity<?> findEncryptKey() {
		String methodKey = "TrinityPropController#findEncryptKey(...)";
		try {
			return ResponseEntity.ok(trinityProp.getEncrypt().getKey());
		}catch (Exception e) {
			TrinityPropController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
	    }
	}
}
