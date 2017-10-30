package com.netpro.trinity.repository.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.service.VRAgentListService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/vragent-list")
public class VRAgentListController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRAgentListController.class);
		
	@Autowired
	private VRAgentListService service;
	
	@GetMapping("/findByVRAgentUid")
	public ResponseEntity<?> findVRAgentByVRAgentUid(String uid) {
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
}
