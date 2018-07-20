package com.netpro.trinity.auth.authz.controller;

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

import com.netpro.trinity.auth.authz.entity.AccessRight;
import com.netpro.trinity.auth.authz.service.AuthzService;
import com.netpro.trinity.auth.feign.util.TrinityBadResponseWrapper;
import com.netpro.trinity.auth.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/authorization")
public class AuthzController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthzController.class);
		
	@Autowired
	private AuthzService service;
	
	@GetMapping("/findByPeopleUid")
	public ResponseEntity<?> findByPeopleUid(String peopleUid) {
		try {
			return ResponseEntity.ok(this.service.getByPeopleUid(peopleUid));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByObjectUid")
	public ResponseEntity<?> findByObjectUid(String objectUid) {
		try {
			return ResponseEntity.ok(this.service.getByObjectUid(objectUid));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findUserExByObjectUid")
	public ResponseEntity<?> findUserExByObjectUid(String objectUid) {
		try {
			return ResponseEntity.ok(this.service.getUserExByObjectUid(objectUid));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findRoleExByObjectUid")
	public ResponseEntity<?> findRoleExByObjectUid(String objectUid) {
		try {
			return ResponseEntity.ok(this.service.getRoleExByObjectUid(objectUid));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByAllPKs")
	public ResponseEntity<?> findByAllPKs(String peopleUid, String objectUid) {
		try {
			return ResponseEntity.ok(this.service.getByAllPKs(peopleUid, objectUid));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFunctionalPermissionByPeopleUid")
	public ResponseEntity<?> findFunctionalPermissionByPeopleUid(String peopleUid) {
		try {
			return ResponseEntity.ok(this.service.getFunctionalPermissionByPeopleUid(peopleUid));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneAccessRight(HttpServletRequest request, @RequestBody AccessRight list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyAccessRight(HttpServletRequest request, @RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.add(lists));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@PostMapping("/updateOne")
	public ResponseEntity<?> updateOneAccessRight(HttpServletRequest request, @RequestBody AccessRight list) {
		try {
			return ResponseEntity.ok(this.service.update(list));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@PostMapping("/updateMany")
	public ResponseEntity<?> updateManyAccessRight(HttpServletRequest request, @RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.update(lists));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchAccessRight(HttpServletRequest request, @RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(lists));
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@PostMapping("/updateBatch")
	public ResponseEntity<?> updateBatchAccessRight(HttpServletRequest request, @RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.updateBatch(lists));
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@PostMapping("/modifyByPeopleUid")
	public ResponseEntity<?> modifyAccessRightByPeopleUid(HttpServletRequest request, String peopleUid, @RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.modifyByPeopleUid(peopleUid, lists));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@PostMapping("/modifyByObjectUid")
	public ResponseEntity<?> modifyAccessRightByObjectUid(HttpServletRequest request, String objectUid, @RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.modifyByObjectUid(objectUid, lists));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
	}
	
	@GetMapping("/deleteByPeopleUid")
	public ResponseEntity<?> deleteAccessRightByPeopleUid(HttpServletRequest request, String peopleUid) {
		try {
			this.service.deleteByPeopleUid(peopleUid);
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
		return ResponseEntity.ok(peopleUid);
	}
	
	@GetMapping("/deleteByObjectUid")
	public ResponseEntity<?> deleteAccessRightByObjectUid(HttpServletRequest request, String objectUid) {
		try {
			this.service.deleteByObjectUid(objectUid);
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
		return ResponseEntity.ok(objectUid);
	}
	
	@GetMapping("/deleteByPKs")
	public ResponseEntity<?> deleteAccessRightByPKs(HttpServletRequest request, String peopleUid, String objectUid) {
		try {
			this.service.deleteByPKs(peopleUid, objectUid);
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}finally {
			reloadPermission(request);
		}
		return ResponseEntity.ok(peopleUid);
	}
	
	@GetMapping("/loadPermissionTableByUserId")
	public ResponseEntity<?> loadPermissionTableByUserId(String userId) {
		try {
			return ResponseEntity.ok(this.service.loadPermissionTable(userId));
		} catch (TrinityBadResponseWrapper e) {
			AuthzController.LOGGER.error("TrinityBadResponseWrapper; reason was:\n"+e.getBody());
			return ResponseEntity.status(e.getStatus()).body(e.getBody());
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findPermissionTableByUserId")
	public ResponseEntity<?> findPermissionTableByUserId(String userId) {
		try {
			return ResponseEntity.ok(this.service.getPermissionTable(userId));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/checkFuncPermission")
	public ResponseEntity<?> findFuncPermissionTableByUserId(String userId, String functionName, String permissionFlag) {
		try {
			return ResponseEntity.ok(this.service.checkFuncPermission(userId, functionName, permissionFlag));
		}catch(IllegalArgumentException e) {
			AuthzController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AuthzController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/checkObjPermission")
	public ResponseEntity<?> findObjPermissionTableByUserId(String userId, String objUid, String permissionFlag) {
		try {
			return ResponseEntity.ok(this.service.checkObjPermission(userId, objUid, permissionFlag));
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
