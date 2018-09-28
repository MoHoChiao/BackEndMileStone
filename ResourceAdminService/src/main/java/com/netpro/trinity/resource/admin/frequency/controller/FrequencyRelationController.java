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

import com.netpro.trinity.resource.admin.frequency.entity.FrequencyRelation;
import com.netpro.trinity.resource.admin.frequency.service.FrequencyRelationService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/frequency-relation")
public class FrequencyRelationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyRelationController.class);
		
	@Autowired
	private FrequencyRelationService service;
	
	@GetMapping("/findAllFrequencyUids")
	public ResponseEntity<?> findAllFrequencyUids() {
		try {
			return ResponseEntity.ok(this.service.getAllFrequencyUids());
		}catch(Exception e) {
			FrequencyRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFrequencyUidsByCategoryUid")
	public ResponseEntity<?> findFrequencyUidsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getFrequencyUidsByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			FrequencyRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneRelation(@RequestBody FrequencyRelation rel) {
		try {
			return ResponseEntity.ok(this.service.add(rel));
		}catch(IllegalArgumentException e) {
			FrequencyRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByFrequencyUid")
	public ResponseEntity<?> deleteRelationByFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FrequencyRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByCategoryUid")
	public ResponseEntity<?> isRelationExistByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			FrequencyRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
