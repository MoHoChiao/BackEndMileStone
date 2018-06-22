package com.netpro.trinity.service.connection.controller;

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

import com.netpro.trinity.service.connection.entity.ConnectionRelation;
import com.netpro.trinity.service.connection.service.ConnectionRelationService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/connection-relation")
public class ConnectionRelationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRelationController.class);
		
	@Autowired
	private ConnectionRelationService service;
	
	@GetMapping("/findAllConnectionUids")
	public ResponseEntity<?> findAllConnectionUids() {
		try {
			return ResponseEntity.ok(this.service.getAllConnectionUids());
		}catch(Exception e) {
			ConnectionRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findConnectionUidsByCategoryUid")
	public ResponseEntity<?> findConnectionUidsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getConnectionUidsByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			ConnectionRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneRelation(@RequestBody ConnectionRelation rel) {
		try {
			return ResponseEntity.ok(this.service.add(rel));
		}catch(IllegalArgumentException e) {
			ConnectionRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByConnectionUid")
	public ResponseEntity<?> deleteRelationByConnectionUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByConnectionUid(uid));
		}catch(IllegalArgumentException e) {
			ConnectionRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByCategoryUid")
	public ResponseEntity<?> isRelationExistByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			ConnectionRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
