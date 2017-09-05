package com.netpro.trinity.config.service.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.config.service.config.entity.TrinityPropSetting;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-prop-setting")
public class TrinityPropSettingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityPropSettingController.class);
		
	@Autowired
	private TrinityPropSetting trinityPropConfig;
	
	@GetMapping("/find-all-prop")
	public ResponseEntity<?> findTrinityProp() {
		try {
			return ResponseEntity.ok(trinityPropConfig.getTrinityprop());
		} catch (Exception e) {
			TrinityPropSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/find-trinity-prop-by-key")
	public ResponseEntity<?> findPropByKey() {
		try {
			Map<String, String> all =  new HashMap<>();
			all.putAll(trinityPropConfig.getTrinityprop());
			return ResponseEntity.ok(all);
		} catch (Exception e) {
			TrinityPropSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
