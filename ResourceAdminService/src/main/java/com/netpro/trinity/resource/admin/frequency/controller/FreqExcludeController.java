package com.netpro.trinity.resource.admin.frequency.controller;

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

import com.netpro.trinity.resource.admin.frequency.entity.FreqExclude;
import com.netpro.trinity.resource.admin.frequency.service.FreqExcludeService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/freq-exclude")
public class FreqExcludeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FreqExcludeController.class);
		
	@Autowired
	private FreqExcludeService service;
	
	@GetMapping("/findAllFrequencyUids")
	public ResponseEntity<?> findAllFrequencyUids() {
		try {
			return ResponseEntity.ok(this.service.getAllFrequencyUids());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFrequencyUidsByExcludeFrequencyUid")
	public ResponseEntity<?> findFrequencyUidsByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getFrequencyUidsByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FreqExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFullPathByExcludeFrequencyUid")
	public ResponseEntity<?> findFrequencyFullPathByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getFreqFullPathByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FreqExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOne(@RequestBody FreqExclude fe) {
		try {
			return ResponseEntity.ok(this.service.add(fe));
		}catch(IllegalArgumentException e) {
			FreqExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByFrequencyUid")
	public ResponseEntity<?> deleteFreqExcludeByFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FreqExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByExcludeFrequencyUid")
	public ResponseEntity<?> deleteFreqExcludeByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FreqExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByPKUids")
	public ResponseEntity<?> deleteFreqExcludeByPKUids(String excludeFreqUid, String freqUid) {
		try {
			return ResponseEntity.ok(this.service.deleteByPKUids(excludeFreqUid, freqUid));
		}catch(IllegalArgumentException e) {
			FreqExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByExcludeFrequencyUid")
	public ResponseEntity<?> isFreqExcludeExistByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByExcludeFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FreqExcludeController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FreqExcludeController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
