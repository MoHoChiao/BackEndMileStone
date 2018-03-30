package com.netpro.trinity.repository.controller.member;

import java.lang.reflect.InvocationTargetException;

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

import com.netpro.trinity.repository.dto.filter.FilterInfo;
import com.netpro.trinity.repository.entity.member.jpa.Role;
import com.netpro.trinity.repository.service.member.RoleService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/role")
public class RoleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
		
	@Autowired
	private RoleService service;
	
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
	public ResponseEntity<?> findRoleByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(uid));
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findRolesByName(String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(name));
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
		}catch(SecurityException e) {
			RoleController.LOGGER.error("SecurityException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(NoSuchMethodException e) {
			RoleController.LOGGER.error("NoSuchMethodException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalAccessException e) {
			RoleController.LOGGER.error("IllegalAccessException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(InvocationTargetException e) {
			RoleController.LOGGER.error("InvocationTargetException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addRole(@RequestBody Role role) {
		try {
			return ResponseEntity.ok(this.service.add(role));
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editRole(@RequestBody Role role) {
		try {
			return ResponseEntity.ok(this.service.edit(role));
		}catch(IllegalArgumentException e) {
			RoleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			RoleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> deleteRoleByUid(String uid) {
		try {
			this.service.deleteByUid(uid);
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
