package com.netpro.trinity.repository.controller.member;

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

import com.netpro.trinity.repository.entity.member.jdbc.GroupMember;
import com.netpro.trinity.repository.service.member.GroupMemberService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/group-member")
public class GroupMemberController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupMemberController.class);
		
	@Autowired
	private GroupMemberService service;
	
	@GetMapping("/findAllUserUids")
	public ResponseEntity<?> findAllUserUids() {
		try {
			return ResponseEntity.ok(this.service.getAllUserUids());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findUserUidsByGroupUid")
	public ResponseEntity<?> findUserUidsByGroupUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getUserUidsByGroupUid(uid));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFullNameByGroupUid")
	public ResponseEntity<?> findUserFullNameByGroupUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getUserFullNameByGroupUid(uid));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOne(@RequestBody GroupMember gm) {
		try {
			return ResponseEntity.ok(this.service.add(gm));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addMany(String groupUid, @RequestBody List<GroupMember> lists) {
		try {
			return ResponseEntity.ok(this.service.add(groupUid, lists));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatch(String groupUid, @RequestBody List<GroupMember> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(groupUid, lists));
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByUserUid")
	public ResponseEntity<?> deleteMemberByUserUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByUserUid(uid));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByGroupUid")
	public ResponseEntity<?> deleteMemberByGroupUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.deleteByGroupUid(uid));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByPKUids")
	public ResponseEntity<?> deleteMemberByPKUids(String groupUid, String userUid) {
		try {
			return ResponseEntity.ok(this.service.deleteByPKUids(groupUid, userUid));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByGroupUid")
	public ResponseEntity<?> isMemberExistByGroupUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByGroupUid(uid));
		}catch(IllegalArgumentException e) {
			GroupMemberController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			GroupMemberController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
