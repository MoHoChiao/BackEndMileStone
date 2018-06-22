package com.netpro.trinity.service.job.controller;

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

import com.netpro.trinity.service.job.entity.JobExclude;
import com.netpro.trinity.service.job.service.JobExcludeService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/job-exclude")
public class JobExcludeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobExcludeController.class);
		
	@Autowired
	private JobExcludeService service;
	
	@GetMapping("/findAllJobUids")
	public ResponseEntity<?> findAllJobUids() {
		try {
			return ResponseEntity.ok(this.service.getAllJobUids());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findJobUidsByExcludeFrequencyUid")
	public ResponseEntity<?> findJobUidsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getJobUidsByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFullPathByExcludeFrequencyUid")
	public ResponseEntity<?> findJobFullPathByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getJobFullPathByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOne(@RequestBody JobExclude je) {
		try {
			return ResponseEntity.ok(this.service.add(je));
		}catch(IllegalArgumentException e) {
			JobExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByJobUid")
	public ResponseEntity<?> deleteJobExcludeByJobUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByJobUid(uid));
		}catch(IllegalArgumentException e) {
			JobExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByExcludeFrequencyUid")
	public ResponseEntity<?> deleteJobExcludeByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByPKUids")
	public ResponseEntity<?> deleteJobExcludeByPKUids(String excludeFreqUid, String jobUid) {
		try {
			return ResponseEntity.ok(this.service.deleteByPKUids(excludeFreqUid, jobUid));
		}catch(IllegalArgumentException e) {
			JobExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByExcludeFrequencyUid")
	public ResponseEntity<?> isJobExcludeExistExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
