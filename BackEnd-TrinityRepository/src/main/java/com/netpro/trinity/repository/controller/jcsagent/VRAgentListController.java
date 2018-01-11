package com.netpro.trinity.repository.controller.jcsagent;

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

import com.netpro.trinity.repository.entity.jcsagent.jdbc.VRAgentList;
import com.netpro.trinity.repository.service.jcsagent.VRAgentListService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/vragent-list")
public class VRAgentListController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRAgentListController.class);
		
	@Autowired
	private VRAgentListService service;
	
	@GetMapping("/findByVRAgentUid")
	public ResponseEntity<?> findListByVRAgentUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getExByVRAgentUid(uid));
		}catch(IllegalArgumentException e) {
			VRAgentListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneVRAgentList(@RequestBody VRAgentList list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			VRAgentListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyVRAgentList(String vrAgentUid, @RequestBody List<VRAgentList> lists) {
		try {
			return ResponseEntity.ok(this.service.add(vrAgentUid, lists));
		}catch(IllegalArgumentException e) {
			VRAgentListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchVRAgentList(@RequestBody List<VRAgentList> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(lists));
		}catch(Exception e) {
			VRAgentListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByVRAgentUid")
	public ResponseEntity<?> deleteListByVRAgentUid(String uid) {
		try {
			this.service.deleteByVRAgentUid(uid);
		}catch(IllegalArgumentException e) {
			VRAgentListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
