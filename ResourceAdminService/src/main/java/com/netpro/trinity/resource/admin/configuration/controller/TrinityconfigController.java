package com.netpro.trinity.resource.admin.configuration.controller;

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

import com.netpro.trinity.resource.admin.configuration.entity.Trinityconfig;
import com.netpro.trinity.resource.admin.configuration.service.TrinityconfigService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-config")
public class TrinityconfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityconfigController.class);
		
	@Autowired
	private TrinityconfigService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllTrinityConfigs() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			TrinityconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findTrinityConfigByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(uid));
		}catch(IllegalArgumentException e) {
			TrinityconfigController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editTrinityConfig(@RequestBody Trinityconfig config) {
		try {
			return ResponseEntity.ok(this.service.edit(config));
		}catch(IllegalArgumentException e) {
			TrinityconfigController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isTrinityConfigExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			TrinityconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
