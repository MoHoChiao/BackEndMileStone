package com.netpro.trinity.repository.controller;

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

import com.netpro.trinity.repository.jdbc.entity.FilesourceRelation;
import com.netpro.trinity.repository.service.FilesourceRelationService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/file-source-relation")
public class FilesourceRelationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FilesourceRelationController.class);
		
	@Autowired
	private FilesourceRelationService service;
	
	@GetMapping("/findAllFileSourceUids")
	public ResponseEntity<?> findAllFileSourceUids() {
		try {
			return ResponseEntity.ok(this.service.getAllFileSourceUids());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFileSourceUidsByCategoryUid")
	public ResponseEntity<?> findFileSourceUidsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getFileSourceUidsByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			FilesourceRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneRelation(@RequestBody FilesourceRelation rel) {
		try {
			return ResponseEntity.ok(this.service.add(rel));
		}catch(IllegalArgumentException e) {
			FilesourceRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByFileSourceUid")
	public ResponseEntity<?> deleteRelationByFileSourceUid(String uid) {
		try {
			this.service.deleteByFileSourceUid(uid);
		}catch(IllegalArgumentException e) {
			FilesourceRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/isExistByCategoryUid")
	public ResponseEntity<?> isRelationExistByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.exitByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			FilesourceRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
