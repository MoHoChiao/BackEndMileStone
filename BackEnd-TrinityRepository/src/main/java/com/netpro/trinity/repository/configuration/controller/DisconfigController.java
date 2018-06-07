package com.netpro.trinity.repository.configuration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.configuration.entity.DisconfigPKs;
import com.netpro.trinity.repository.configuration.service.DisconfigService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/disconfig")
public class DisconfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisconfigController.class);
	
	@Autowired
	private DisconfigService service;

	@GetMapping("/findById")
	public ResponseEntity<?> findDisconfigById(DisconfigPKs pks) {
		try {
			return ResponseEntity.ok(this.service.getByUid(pks));
		}catch(IllegalArgumentException e) {
			DisconfigController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DisconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findServicePosition")
	public ResponseEntity<?> findUiapPosition(String module1, String module2, String configname1, String configname2) {
		try {
			return ResponseEntity.ok(this.service.getUiapPosition(module1, module2, configname1, configname2));
		}catch(Exception e) {
			DisconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByModule")
	public ResponseEntity<?> findDisconfigByModule(String module) {
		try {
			return ResponseEntity.ok(this.service.getByModule(module));
		}catch(IllegalArgumentException e) {
			DisconfigController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DisconfigController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
