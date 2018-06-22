package com.netpro.trinity.service.agent.controller;

import java.lang.reflect.InvocationTargetException;

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

import com.netpro.trinity.service.agent.entity.VRAgent;
import com.netpro.trinity.service.agent.service.VRAgentService;
import com.netpro.trinity.service.dto.FilterInfo;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/vragent")
public class VRAgentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRAgentController.class);
		
	@Autowired
	private VRAgentService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllVRAgents(Boolean withoutDetail) {
		try {
			return ResponseEntity.ok(this.service.getAll(withoutDetail));
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findVRAgentByUid(Boolean withoutDetail, String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(withoutDetail, uid));
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findVRAgentsByName(Boolean withoutDetail, String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(withoutDetail, name));
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findVRAgentsByFilter(Boolean withoutDetail, @RequestBody FilterInfo filter) {
		try {
			return this.service.getByFilter(withoutDetail, filter);
		}catch(SecurityException e) {
			VRAgentController.LOGGER.error("SecurityException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(NoSuchMethodException e) {
			VRAgentController.LOGGER.error("NoSuchMethodException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalAccessException e) {
			VRAgentController.LOGGER.error("IllegalAccessException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(InvocationTargetException e) {
			VRAgentController.LOGGER.error("InvocationTargetException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/add")
	public ResponseEntity<?> addVRAgent(@RequestBody VRAgent vragent) {
		try {
			return ResponseEntity.ok(this.service.add(vragent));
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editVRAgent(@RequestBody VRAgent vragent) {
		try {
			return ResponseEntity.ok(this.service.edit(vragent));
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/delete")
	public ResponseEntity<?> deleteVRAgentByUid(String uid) {
		try {
			this.service.deleteByUid(uid);
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isVRAgentExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
