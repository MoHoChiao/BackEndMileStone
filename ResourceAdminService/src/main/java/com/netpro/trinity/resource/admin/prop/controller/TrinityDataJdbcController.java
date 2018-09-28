package com.netpro.trinity.resource.admin.prop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.resource.admin.prop.dto.TrinityDataJDBC;

@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-data-jdbc")
public class TrinityDataJdbcController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityDataJdbcController.class);
		
	@Autowired
	private TrinityDataJDBC trinityJdbc;
	
	@GetMapping("/find-all-drivers")
	public ResponseEntity<?> findAllDrivers() {
		try {
			return ResponseEntity.ok(trinityJdbc.getInfo());
		}catch (Exception e) {
			TrinityDataJdbcController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
}
