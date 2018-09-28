package com.netpro.trinity.resource.admin.versioninfo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.resource.admin.versioninfo.service.VersionInfoService;

@RestController
@RequestMapping("/versioninfo")
public class VersionInfoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VersionInfoController.class);
	
	@Autowired
	private VersionInfoService service;
	
	@GetMapping("/findJCSServer")
	public ResponseEntity<?> findJCSServer() {
		try {
			return ResponseEntity.ok(this.service.findJCSServer());
		}catch(Exception e) {
			VersionInfoController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findJCSAgent")
	public ResponseEntity<?> findJCSAgent() {
		try {
			return ResponseEntity.ok(this.service.findJCSAgent());
		}catch(Exception e) {
			VersionInfoController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findDISServer")
	public ResponseEntity<?> findDISServer() {
		try {
			return ResponseEntity.ok(this.service.findDISServer());
		}catch(Exception e) {
			VersionInfoController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
}
