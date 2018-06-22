package com.netpro.trinity.service.job.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.service.job.service.JobstepService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/jobstep")
public class JobstepController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobstepController.class);
		
	@Autowired
	private JobstepService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllJobsteps() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			JobstepController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByType")
	public ResponseEntity<?> findJobstepsByType(String type) {
		try {
			return ResponseEntity.ok(this.service.getByType(type));
		}catch(IllegalArgumentException e) {
			JobstepController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobstepController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isEntityExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			JobstepController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByConnectionuid")
	public ResponseEntity<?> isFileSourceExistByConnectionuid(String connectionuid) {
		try {
			return ResponseEntity.ok(this.service.existByConnectionuid(connectionuid));
		}catch(Exception e) {
			JobstepController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
