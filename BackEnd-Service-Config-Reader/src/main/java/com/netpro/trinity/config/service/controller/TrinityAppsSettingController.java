package com.netpro.trinity.config.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.config.service.config.entity.TrinityAppsSetting.Server.App;
import com.netpro.trinity.config.service.config.entity.TrinityAppsSetting;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-apps-setting")
public class TrinityAppsSettingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityAppsSettingController.class);
	
	@Autowired
	private TrinityAppsSetting appsConfig;
	
	@GetMapping("/find-apps-model")
	public ResponseEntity<?> findAppsModel() {
		try {
			
			String host = appsConfig.getServer().getHost();
			String port = appsConfig.getServer().getPort();
			
			for(App app : appsConfig.getServer().getApps()) {
				if(app.getUrl().indexOf("http://") == -1)
					app.setUrl("http://"+host+":"+port+"/"+app.getUrl());
			}
			return ResponseEntity.ok(appsConfig.getServer().getApps());
		} catch (Exception e) {
			TrinityAppsSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
