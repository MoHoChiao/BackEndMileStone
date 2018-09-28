package com.netpro.trinity.resource.admin.agent.controller;

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

import com.netpro.trinity.resource.admin.agent.entity.VRAgent;
import com.netpro.trinity.resource.admin.agent.service.VRAgentService;
import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/vragent")
public class VRAgentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRAgentController.class);
	
	@Autowired
	private VRAgentService service;
	
	@Autowired
	private AuthzService authzService;
	
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
	public ResponseEntity<?> findVRAgentByUid(HttpServletRequest request, Boolean withoutDetail, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkPermission(peopleId, "agent", uid, "view")) {
				return ResponseEntity.ok(this.service.getByUid(withoutDetail, uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
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
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/add")
	public ResponseEntity<?> addVRAgent(HttpServletRequest request, @RequestBody VRAgent vragent) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkFuncPermission(peopleId, "agent", "add")) {
				return ResponseEntity.ok(this.service.add(request, vragent));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Add' Permission!");
			}
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editVRAgent(HttpServletRequest request, @RequestBody VRAgent vragent) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkPermission(peopleId, "agent", vragent.getVirtualagentuid(),  "modify")) {
				return ResponseEntity.ok(this.service.edit(vragent));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		}catch(IllegalArgumentException e) {
			VRAgentController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/delete")
	public ResponseEntity<?> deleteVRAgentByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkPermission(peopleId, "agent", uid,  "delete")) {
				this.service.deleteByUid(uid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Delete' Permission!");
			}
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
