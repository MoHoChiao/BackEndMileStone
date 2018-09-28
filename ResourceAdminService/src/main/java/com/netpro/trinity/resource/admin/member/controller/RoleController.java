package com.netpro.trinity.resource.admin.member.controller;

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

import com.netpro.trinity.resource.admin.authz.dto.PermissionTable;
import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.member.entity.Role;
import com.netpro.trinity.resource.admin.member.service.RoleService;
import com.netpro.trinity.resource.admin.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/role")
public class RoleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
		
	@Autowired
	private RoleService service;
	
	@Autowired
	private AuthzService authzService;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllRoles() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isRoleExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findRoleByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin()) {
				return ResponseEntity.ok(this.service.getByUid(uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findRolesByFilter(@RequestBody FilterInfo filter) {
		try {
			return this.service.getByFilter(filter);
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addRole(HttpServletRequest request, @RequestBody Role role) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin()) {
				return ResponseEntity.ok(this.service.add(role));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editRole(HttpServletRequest request, @RequestBody Role role) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin()) {
				return ResponseEntity.ok(this.service.edit(role));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> deleteRoleByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin()) {
				this.service.deleteByUid(uid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root Or Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
