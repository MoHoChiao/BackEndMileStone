package com.netpro.trinity.resource.admin.prop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.resource.admin.prop.dto.TrinitySysSetting;

@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-sys-setting")
public class TrinitySysSettingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinitySysSettingController.class);
		
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@GetMapping("/find-all-dirs")
	public ResponseEntity<?> findAllDirs() {
		try {
			return ResponseEntity.ok(trinitySys.getDir());
		}catch (Exception e) {
			TrinitySysSettingController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
}
