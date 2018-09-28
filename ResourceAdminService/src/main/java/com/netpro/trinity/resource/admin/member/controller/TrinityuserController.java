package com.netpro.trinity.resource.admin.member.controller;

import java.sql.SQLException;

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

import com.netpro.ac.ACException;
import com.netpro.trinity.resource.admin.authz.dto.PermissionTable;
import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.member.entity.Trinityuser;
import com.netpro.trinity.resource.admin.member.service.TrinityuserService;
import com.netpro.trinity.resource.admin.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-user")
public class TrinityuserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityuserController.class);
		
	@Autowired
	private TrinityuserService service;
	
	@Autowired
	private AuthzService authzService;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllUsers() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isUserExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findUserByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin() || permissionTable.isAccountManager()) {
				return ResponseEntity.ok(this.service.getByUid(uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root, Admin or AccountManager' Permission!");
			}
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findUsersByFilter(@RequestBody FilterInfo filter) {
		try {
			return this.service.getByFilter(filter);
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByID")
	public ResponseEntity<?> findUsersByID(String id) {
		try {
			return ResponseEntity.ok(this.service.getByID(id));
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addRole(HttpServletRequest request, @RequestBody Trinityuser user) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin() || permissionTable.isAccountManager()) {
				return ResponseEntity.ok(this.service.add(request, user));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root, Admin or AccountManager' Permission!");
			}
		}catch(ACException e) {
			TrinityuserController.LOGGER.error("ACException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(SQLException e) {
			TrinityuserController.LOGGER.error("SQLException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editRole(HttpServletRequest request, @RequestBody Trinityuser user) {
		try {
			String useruid = user.getUseruid();
			Trinityuser old_user = this.service.getByUid(useruid);
			Trinityuser reqUser = this.service.getUserFormRequest(request);
			
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			
			if(permissionTable.isAdminEditMode()) {
				if(permissionTable.isRoot() || permissionTable.isAdmin() || permissionTable.isAccountManager()) {
					return ResponseEntity.ok(this.service.edit(request, user));
				}else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Root, Admin or AccountManager' Permission!");
				}
			}else {
				if(permissionTable.isRoot() || permissionTable.isAccountManager() || 
						old_user.getCreateduseruid().trim().equals(reqUser.getUseruid().trim()) || 
						useruid.trim().equals(reqUser.getUseruid().trim())) {
					return ResponseEntity.ok(this.service.edit(request, user));
				}else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This account is created by root or another admin!");
				}
			}
		}catch(ACException e) {
			TrinityuserController.LOGGER.error("ACException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(SQLException e) {
			TrinityuserController.LOGGER.error("SQLException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> deleteUserByUid(HttpServletRequest request, String uid) {
		try {
			Trinityuser old_user = this.service.getByUid(uid);
			Trinityuser reqUser = this.service.getUserFormRequest(request);
			
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			
			if(permissionTable.isRoot() || permissionTable.isAccountManager() || 
					old_user.getCreateduseruid().trim().equals(reqUser.getUseruid().trim()) || 
					uid.trim().equals(reqUser.getUseruid().trim())) {
				this.service.deleteByUid(uid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This account is created by root or another admin!");
			}
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/lock")
	public ResponseEntity<?> lockUserByID(HttpServletRequest request, String userid, Boolean lock) {
		try {
			Trinityuser old_user = this.service.getByID(userid);
			Trinityuser reqUser = this.service.getUserFormRequest(request);
			
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			
			if(permissionTable.isRoot() || permissionTable.isAccountManager() || 
					old_user.getCreateduseruid().trim().equals(reqUser.getUseruid().trim()) || 
					old_user.getUseruid().trim().equals(reqUser.getUseruid().trim())) {
				return ResponseEntity.ok(this.service.lockByUserID(request, userid, lock));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This account is created by root or another admin!");
			}
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
