package com.netpro.trinity.resource.admin.prop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.resource.admin.prop.dto.TrinityRepoSetting;

@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-repo-setting")
public class TrinityRepoSettingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityRepoSettingController.class);
		
	@Autowired
	private TrinityRepoSetting trinityRepo;
	
	@GetMapping("/find-database-info")
	public ResponseEntity<?> findDatabaseInfo() {
		try {
			return ResponseEntity.ok(trinityRepo.getDatasource());
		}catch (Exception e) {
			TrinityRepoSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
}
