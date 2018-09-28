package com.netpro.trinity.resource.admin.configuration.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.resource.admin.configuration.entity.Monitorconfig;
import com.netpro.trinity.resource.admin.configuration.service.MonitorconfigService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/monitor-config")
public class MonitorconfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorconfigController.class);
		
	@Autowired
	private MonitorconfigService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllMonitorConfigs() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			MonitorconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findMonitorConfigByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(uid));
		}catch(IllegalArgumentException e) {
			MonitorconfigController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			MonitorconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/modify")
	public ResponseEntity<?> modifyMonitorConfig(@RequestBody Monitorconfig config) {
		try {
			return ResponseEntity.ok(this.service.modify(config));
		}catch(IllegalArgumentException e) {
			MonitorconfigController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			MonitorconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/modifyMany")
	public ResponseEntity<?> modifyMonitorConfig(@RequestBody List<Monitorconfig> configs) {
		try {
			return ResponseEntity.ok(this.service.modify(configs));
		}catch(IllegalArgumentException e) {
			MonitorconfigController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			MonitorconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isMonitorConfigExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			MonitorconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
