package com.netpro.trinity.service.agent.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.netpro.trinity.service.agent.entity.JCSAgent;
import com.netpro.trinity.service.agent.service.JCSAgentService;
import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.feign.util.TrinityBadResponseWrapper;
import com.netpro.trinity.service.permission.feign.PermissionClient;
import com.netpro.trinity.service.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/jcsagent")
public class JCSAgentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JCSAgentController.class);
		
	@Autowired
	private JCSAgentService service;
	
	@Autowired
	private PermissionClient permissionClient;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllAgents() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isAgentExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findAgentByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkPermission(peopleId, "agent", uid, "view")) {
				return ResponseEntity.ok(this.service.getByUid(uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		}catch(IllegalArgumentException e) {
			JCSAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findAllAgentNames")
	public ResponseEntity<?> findAllAgentNames() {
		try {
			return ResponseEntity.ok(this.service.getAllAgentNames());
		}catch(IllegalArgumentException e) {
			JCSAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findAllAgentUids")
	public ResponseEntity<?> findAllAgentUids() {
		try {
			return ResponseEntity.ok(this.service.getAllAgentUids());
		}catch(IllegalArgumentException e) {
			JCSAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findAgentByFilter(@RequestBody FilterInfo filter) {
		try {
			return this.service.getByFilter(filter);
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/add")
	public ResponseEntity<?> addAgent(HttpServletRequest request, @RequestBody JCSAgent agent) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkFuncPermission(peopleId, "agent", "add")) {
				return ResponseEntity.ok(this.service.add(request, agent));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Add' Permission!");
			}
		}catch(IllegalArgumentException e) {
			JCSAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(TrinityBadResponseWrapper e) {
			JCSAgentController.LOGGER.error("TrinityBadResponseWrapper; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editAgent(HttpServletRequest request, @RequestBody JCSAgent agent) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkPermission(peopleId, "agent", agent.getAgentuid(),  "modify")) {
				return ResponseEntity.ok(this.service.edit(agent));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		}catch(IllegalArgumentException e) {
			JCSAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/delete")
	public ResponseEntity<?> deleteAgentByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkPermission(peopleId, "agent", uid,  "delete")) {
				this.service.deleteByUid(uid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Delete' Permission!");
			}
		}catch(IllegalArgumentException e) {
			JCSAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(TrinityBadResponseWrapper e) {
			JCSAgentController.LOGGER.error("TrinityBadResponseWrapper; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
