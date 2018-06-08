package com.netpro.trinity.repository.member.controller;

import java.util.List;

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

import com.netpro.trinity.repository.member.entity.RoleMember;
import com.netpro.trinity.repository.member.service.RoleMemberService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/role-member")
public class RoleMemberController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleMemberController.class);
		
	@Autowired
	private RoleMemberService service;
	
	@GetMapping("/findAllUserUids")
	public ResponseEntity<?> findAllUserUids() {
		try {
			return ResponseEntity.ok(this.service.getAllUserUids());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findUserUidsByRoleUid")
	public ResponseEntity<?> findUserUidsByRoleUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getUserUidsByRoleUid(uid));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findRoleUidsByUserUid")
	public ResponseEntity<?> findRoleUidsByUserUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getRoleUidsByUserUid(uid));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFullNameByRoleUid")
	public ResponseEntity<?> findUserFullNameByRoleUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getUserFullNameByRoleUid(uid));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOne(@RequestBody RoleMember rm) {
		try {
			return ResponseEntity.ok(this.service.add(rm));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addMany(String roleUid, @RequestBody List<RoleMember> lists) {
		try {
			return ResponseEntity.ok(this.service.add(roleUid, lists));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatch(String roleUid, @RequestBody List<RoleMember> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(roleUid, lists));
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByUserUid")
	public ResponseEntity<?> deleteMemberByUserUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByUserUid(uid));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByRoleUid")
	public ResponseEntity<?> deleteMemberByRoleUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByRoleUid(uid));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByPKUids")
	public ResponseEntity<?> deleteMemberByPKUids(String roleUid, String userUid) {
		try {
			return ResponseEntity.ok(this.service.deleteByPKUids(roleUid, userUid));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByRoleUid")
	public ResponseEntity<?> isMemberExistByRoleUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByRoleUid(uid));
		}catch(IllegalArgumentException e) {
			RoleMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
