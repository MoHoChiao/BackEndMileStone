package com.netpro.trinity.repository.controller;

import java.util.List;

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

import com.netpro.trinity.repository.jdbc.entity.VRAgentList;
import com.netpro.trinity.repository.service.FilesourceRelationService;
import com.netpro.trinity.repository.service.VRAgentListService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/file-source-relation")
public class FilesourceRelationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FilesourceRelationController.class);
		
	@Autowired
	private FilesourceRelationService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllRelations() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByCategoryUid")
	public ResponseEntity<?> findRelationsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			FilesourceRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
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
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneVRAgentList(@RequestBody VRAgentList list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			FilesourceRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyVRAgentList(String vrAgentUid, @RequestBody List<VRAgentList> lists) {
		try {
			return ResponseEntity.ok(this.service.add(vrAgentUid, lists));
		}catch(IllegalArgumentException e) {
			FilesourceRelationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FilesourceRelationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchVRAgentList(@RequestBody List<VRAgentList> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(lists));
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
}
