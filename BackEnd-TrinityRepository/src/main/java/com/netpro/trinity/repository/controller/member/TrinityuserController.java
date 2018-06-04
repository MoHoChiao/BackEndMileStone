package com.netpro.trinity.repository.controller.member;

import java.lang.reflect.InvocationTargetException;
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
import com.netpro.trinity.repository.dto.filter.FilterInfo;
import com.netpro.trinity.repository.entity.member.jpa.Trinityuser;
import com.netpro.trinity.repository.service.member.TrinityuserService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-user")
public class TrinityuserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityuserController.class);
		
	@Autowired
	private TrinityuserService service;
	
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
	public ResponseEntity<?> findUserByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(uid));
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findUsersByName(String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(name));
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
	
	@GetMapping("/findByType")
	public ResponseEntity<?> findUsersByType(String type) {
		try {
			return ResponseEntity.ok(this.service.getByUserType(type));
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
		}catch(SecurityException e) {
			TrinityuserController.LOGGER.error("SecurityException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(NoSuchMethodException e) {
			TrinityuserController.LOGGER.error("NoSuchMethodException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalAccessException e) {
			TrinityuserController.LOGGER.error("IllegalAccessException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(InvocationTargetException e) {
			TrinityuserController.LOGGER.error("InvocationTargetException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
			user = this.service.add(request, user);
			return ResponseEntity.ok(user);
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
			return ResponseEntity.ok(this.service.edit(request, user));
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
	public ResponseEntity<?> deleteUserByUid(String uid) {
		try {
			this.service.deleteByUid(uid);
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
			return ResponseEntity.ok(this.service.lockByUserID(request, userid, lock));
		}catch(IllegalArgumentException e) {
			TrinityuserController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TrinityuserController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
