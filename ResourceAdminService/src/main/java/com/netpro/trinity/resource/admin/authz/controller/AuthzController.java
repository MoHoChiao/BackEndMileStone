package com.netpro.trinity.resource.admin.authz.controller;

import java.util.List;
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
import com.netpro.trinity.resource.admin.authz.entity.AccessRight;
import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/authorization")
public class AuthzController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthzController.class);
		
	@Autowired
	private AuthzService service;
	
	@GetMapping("/findUserExByObjectUid")
	public ResponseEntity<?> findUserExByObjectUid(HttpServletRequest request, String objectUid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.service.checkObjPermission(peopleId, objectUid, "view") || //當此Object是具有view權限時
					(null != objectUid && objectUid.indexOf("$") > -1 && this.service.checkFuncPermission(peopleId, "alias", "view"))) //當此object是alias obejct時 
			{
				return ResponseEntity.ok(this.service.getUserExByObjectUid(objectUid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findRoleExByObjectUid")
	public ResponseEntity<?> findRoleExByObjectUid(HttpServletRequest request, String objectUid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.service.checkObjPermission(peopleId, objectUid, "view") || //當此Object是具有view權限時
					(null != objectUid && objectUid.indexOf("$") > -1 && this.service.checkFuncPermission(peopleId, "alias", "view"))) //當此object是alias obejct時 
			{
				return ResponseEntity.ok(this.service.getRoleExByObjectUid(objectUid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFunctionalPermissionByPeopleUid")
	public ResponseEntity<?> findFunctionalPermissionByPeopleUid(HttpServletRequest request, String peopleUid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin()) {
				return ResponseEntity.ok(this.service.getFunctionalPermissionByPeopleUid(peopleUid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	//this api may just for setting functional permission
	@PostMapping("/modifyByPeopleUid")
	public ResponseEntity<?> modifyAccessRightByPeopleUid(HttpServletRequest request, String peopleUid, @RequestBody List<AccessRight> lists) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin()) {
				return ResponseEntity.ok(this.service.modifyByPeopleUid(peopleUid, lists));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	//	this api for jcsagent, connection, alias permission
	@PostMapping("/modifyByObjectUid")
	public ResponseEntity<?> modifyAccessRightByObjectUid(HttpServletRequest request, String objectUid, @RequestBody List<AccessRight> lists) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(!this.service.existByObjectUid(objectUid) || //當此Object不存在權限記錄時(這是專門為了某些物件在建立時, 就會預設塞一條權限記錄, 另如JCSAgent...等等)
					(this.service.checkObjPermission(peopleId, objectUid, "modify") && this.service.checkObjPermission(peopleId, objectUid, "grant")) || //當此Object是具有Edit及Grant權限時
					(null != objectUid && objectUid.indexOf("$") > -1 && this.service.checkFuncPermission(peopleId, "alias", "modify"))) //當此object是alias obejct時 
			{
				return ResponseEntity.ok(this.service.modifyByObjectUid(objectUid, lists));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit or Grant' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			/*
			 * modifyAccessRightByObjectUid, 此方法需要即時的更新記億體中的permission資料, 以供前端使用
			 * 因為使用者是有可能可以自己改變自己對object的permisson, 例如jcsagent, connection, frequency...等等Object
			 */
			reloadPermission(request);
		}
	}
	
	@GetMapping("/deleteByPeopleUid")
	public ResponseEntity<?> deleteAccessRightByPeopleUid(HttpServletRequest request, String peopleUid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			if(permissionTable.isRoot() || permissionTable.isAdmin()) {
				this.service.deleteByPeopleUid(peopleUid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Admin' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(peopleUid);
	}
	
	@GetMapping("/deleteByObjectUid")
	public ResponseEntity<?> deleteAccessRightByObjectUid(HttpServletRequest request, String objectUid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.service.checkObjPermission(peopleId, objectUid, "delete") || 
					(null != objectUid && objectUid.indexOf("$") > -1 && this.service.checkFuncPermission(peopleId, "alias", "delete"))) //當此object是alias obejct時 
			{
				this.service.deleteByObjectUid(objectUid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Delete' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(objectUid);
	}
	
	@GetMapping("/deleteByAliasParentUid")
	public ResponseEntity<?> deleteAccessRightByAliasParentUid(HttpServletRequest request, String parentUid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.service.checkFuncPermission(peopleId, "alias", "delete")) //this method, only for alias permission delete
			{
				this.service.deleteByAliasParentUid(parentUid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Delete' Permission!");
			}
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(parentUid);
	}
	
	@GetMapping("/loadPermissionTable")
	public ResponseEntity<?> loadPermissionTableByUserId(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			return ResponseEntity.ok(this.service.loadPermissionTable(peopleId));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findPermissionTable")
	public ResponseEntity<?> findPermissionTable(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			return ResponseEntity.ok(this.service.getPermissionTable(peopleId));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/checkFuncPermission")
	public ResponseEntity<?> cehckFuncPermission(HttpServletRequest request, String functionName, String permissionFlag) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			return ResponseEntity.ok(this.service.checkFuncPermission(peopleId, functionName, permissionFlag));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/checkObjPermission")
	public ResponseEntity<?> checkObjPermission(HttpServletRequest request, String objUid, String permissionFlag) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			return ResponseEntity.ok(this.service.checkObjPermission(peopleId, objUid, permissionFlag));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/checkPermission")
	public ResponseEntity<?> checkPermissionTable(HttpServletRequest request, String functionName, String objUid, String permissionFlag) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			return ResponseEntity.ok(this.service.checkFuncPermission(peopleId, functionName, permissionFlag) && 
					this.service.checkObjPermission(peopleId, objUid, permissionFlag));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isRoot")
	public ResponseEntity<?> checkRootPermission(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			return ResponseEntity.ok(permissionTable.isRoot());
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isAdmin")
	public ResponseEntity<?> checkAdminPermission(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			return ResponseEntity.ok(permissionTable.isAdmin());
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isRootOrAdmin")
	public ResponseEntity<?> checkRootOrAdminPermission(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			return ResponseEntity.ok(permissionTable.isRoot() || permissionTable.isAdmin());
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isRootOrAccountManager")
	public ResponseEntity<?> checkRootOrAccountManagerPermission(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			return ResponseEntity.ok(permissionTable.isRoot() || permissionTable.isAccountManager());
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isRootOrAdminOrAccountManager")
	public ResponseEntity<?> checkRootOrAdminOrAccountManagerPermission(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			return ResponseEntity.ok(permissionTable.isRoot() || permissionTable.isAdmin() || permissionTable.isAccountManager());
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isAdminEditMode")
	public ResponseEntity<?> checkAdminEditModePermission(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.service.getPermissionTable(peopleId);
			return ResponseEntity.ok(permissionTable.isAdminEditMode());
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	private void reloadPermission(HttpServletRequest request) {
		String userId = ACUtil.getUserIdFromAC(request);
		if(null != userId && !userId.trim().isEmpty()) {
			try {
				this.service.loadPermissionTable(userId);
			} catch (IllegalArgumentException e) {
				AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			} catch (Exception e) {
				AuthzController.LOGGER.error("Exception; reason was:", e);
			}
		}
	}
}
