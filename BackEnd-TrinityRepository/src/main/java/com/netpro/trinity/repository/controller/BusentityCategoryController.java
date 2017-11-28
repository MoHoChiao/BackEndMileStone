package com.netpro.trinity.repository.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.service.BusentityCategoryService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/busentity-category")
public class BusentityCategoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusentityCategoryController.class);
		
	@Autowired
	private BusentityCategoryService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllRelations() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			BusentityCategoryController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByEntityUid")
	public ResponseEntity<?> findRelationsByEntityUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByEntityUid(uid));
		}catch(IllegalArgumentException e) {
			BusentityCategoryController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityCategoryController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByCategoryUid")
	public ResponseEntity<?> findRelationsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			BusentityCategoryController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityCategoryController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByEntityUid")
	public ResponseEntity<?> isRelationExistByEntityUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.exitByEntityUid(uid));
		}catch(IllegalArgumentException e) {
			BusentityCategoryController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityCategoryController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByJobCategoryUid")
	public ResponseEntity<?> deleteRelationByCategoryUid(String uid) {
		try {
			this.service.deleteByCategoryUid(uid);
		}catch(IllegalArgumentException e) {
			BusentityCategoryController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityCategoryController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
