package com.netpro.trinity.resource.admin.pluginlicense.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.resource.admin.pluginlicense.service.PluginLicenseService;

@RestController
@RequestMapping("/pluginlicense")
public class PluginLicenseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginLicenseController.class);
	
	@Autowired
	private PluginLicenseService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			PluginLicenseController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findLicenseStatus")
	public ResponseEntity<?> findLicenseStatus() {
		try {
			return ResponseEntity.ok(this.service.getLicenseStatus());
		}catch(Exception e) {
			PluginLicenseController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/importPluginLicense")
	public ResponseEntity<?> importPluginLicense(MultipartFile upload) {
		try {
			return ResponseEntity.ok(this.service.importPluginLicense(upload));
		}catch(Exception e) {
			PluginLicenseController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
}
