package com.netpro.trinity.service.member.controller;

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

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.member.entity.Usergroup;
import com.netpro.trinity.service.member.service.UsergroupService;
import com.netpro.trinity.service.permission.feign.PermissionClient;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/user-group")
public class UsergroupController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsergroupController.class);
		
	@Autowired
	private UsergroupService service;
	
	@Autowired
	private PermissionClient permissionClient;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllGroups(HttpServletRequest request) {
		try {
			if(this.permissionClient.isRootOrAdmin()) {
				return ResponseEntity.ok(this.service.getAll());
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(Exception e) {
			UsergroupController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isGroupExistByUid(String uid) {
		try {
			if(this.permissionClient.isRootOrAdmin()) {
				return ResponseEntity.ok(this.service.existByUid(uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(Exception e) {
			UsergroupController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findGroupByUid(String uid) {
		try {
			if(this.permissionClient.isRootOrAdmin()) {
				return ResponseEntity.ok(this.service.getByUid(uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			UsergroupController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			UsergroupController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findGroupsByFilter(@RequestBody FilterInfo filter) {
		try {
			if(this.permissionClient.isRootOrAdmin()) {
				return this.service.getByFilter(filter);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(Exception e) {
			UsergroupController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addGroup(@RequestBody Usergroup group) {
		try {
			if(this.permissionClient.isRootOrAdmin()) {
				return ResponseEntity.ok(this.service.add(group));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			UsergroupController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			UsergroupController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editGroup(@RequestBody Usergroup group) {
		try {
			if(this.permissionClient.isRootOrAdmin()) {
				return ResponseEntity.ok(this.service.edit(group));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			UsergroupController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			UsergroupController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> deleteGroupByUid(String uid) {
		try {
			if(this.permissionClient.isRootOrAdmin()) {
				this.service.deleteByUid(uid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			UsergroupController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			UsergroupController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
