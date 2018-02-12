package com.netpro.trinity.repository.controller.job;

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

import com.netpro.trinity.repository.entity.job.jdbc.JobFlowExclude;
import com.netpro.trinity.repository.service.job.JobFlowExcludeService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/jobflow-exclude")
public class JobFlowExcludeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobFlowExcludeController.class);
		
	@Autowired
	private JobFlowExcludeService service;
	
	@GetMapping("/findAllFlowUids")
	public ResponseEntity<?> findAllFlowUids() {
		try {
			return ResponseEntity.ok(this.service.getAllFlowUids());
		}catch(Exception e) {
			JobFlowExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFlowUidsByExcludeFrequencyUid")
	public ResponseEntity<?> findFlowUidsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getFlowUidsByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFullPathByExcludeFrequencyUid")
	public ResponseEntity<?> findFlowFullPathByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getFlowFullPathByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOne(@RequestBody JobFlowExclude jfe) {
		try {
			return ResponseEntity.ok(this.service.add(jfe));
		}catch(IllegalArgumentException e) {
			JobFlowExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByFlowUid")
	public ResponseEntity<?> deleteFlowExcludeByJobUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByFlowUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByExcludeFrequencyUid")
	public ResponseEntity<?> deleteFlowExcludeByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByExcludeFrequencyUid")
	public ResponseEntity<?> isFlowExcludeExistExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			JobFlowExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JobFlowExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
