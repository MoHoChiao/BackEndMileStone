package com.netpro.trinity.service.agent.controller;

import java.lang.reflect.InvocationTargetException;

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

import com.netpro.trinity.service.agent.entity.VRAgent;
import com.netpro.trinity.service.agent.service.VRAgentService;
import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.permission.feign.PermissionClient;
import com.netpro.trinity.service.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/vragent")
public class VRAgentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRAgentController.class);
	
	@Autowired
	private VRAgentService service;
	
	@Autowired
	private PermissionClient permissionClient;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllVRAgents(HttpServletRequest request, Boolean withoutDetail) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkFuncPermission(peopleId, "agent", "view")) {
				return ResponseEntity.ok(this.service.getAll(withoutDetail));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findVRAgentByUid(HttpServletRequest request, Boolean withoutDetail, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkPermission(peopleId, "agent", uid, "view")) {
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
	public ResponseEntity<?> findVRAgentsByFilter(HttpServletRequest request, Boolean withoutDetail, @RequestBody FilterInfo filter) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkFuncPermission(peopleId, "agent", "view")) {
				return this.service.getByFilter(withoutDetail, filter);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
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
	public ResponseEntity<?> addVRAgent(HttpServletRequest request, @RequestBody VRAgent vragent) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkFuncPermission(peopleId, "agent", "add")) {
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
			if(this.permissionClient.checkPermission(peopleId, "agent", vragent.getVirtualagentuid(),  "modify")) {
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
			if(this.permissionClient.checkPermission(peopleId, "agent", uid,  "delete")) {
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
	public ResponseEntity<?> isVRAgentExistByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.permissionClient.checkFuncPermission(peopleId, "agent", "view")) {
				return ResponseEntity.ok(this.service.existByUid(uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		}catch(Exception e) {
			VRAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
