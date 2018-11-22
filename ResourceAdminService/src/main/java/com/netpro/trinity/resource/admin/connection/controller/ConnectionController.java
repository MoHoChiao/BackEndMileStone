package com.netpro.trinity.resource.admin.connection.controller;

import java.sql.SQLException;
import java.util.Map;

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

import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.connection.entity.JDBCConnection;
import com.netpro.trinity.resource.admin.connection.service.ConnectionService;
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/connection")
public class ConnectionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionController.class);
		
	@Autowired
	private ConnectionService service;
	
	@Autowired
	private AuthzService authzService;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllConnections() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findAll-without-in-category")
	public ResponseEntity<?> findAllConnectionsWithoutInCategory() {
		try {
			return ResponseEntity.ok(this.service.getAllWithoutInCategory());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByCategoryUid")
	public ResponseEntity<?> findConnectionsByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByCategoryUid(uid));
		}catch(IllegalArgumentException e) {
			ConnectionController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findConnectionByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkPermission(peopleId, "connection", uid, "view")) {
				return ResponseEntity.ok(this.service.getByUid(uid));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		}catch(IllegalArgumentException e) {
			ConnectionController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByType")
	public ResponseEntity<?> findConnByType(HttpServletRequest request, String type) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkFuncPermission(peopleId, "connection", "view")) {
				return ResponseEntity.ok(this.service.getByConnType(type));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
			
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findConnectionsByFilter(String categoryUid, @RequestBody FilterInfo filter) {
		try {
			return this.service.getByFilter(categoryUid, filter);
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addConnection(HttpServletRequest request, String categoryUid, @RequestBody Map<String, String> connMap) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkFuncPermission(peopleId, "connection", "add")) {
				return ResponseEntity.ok(this.service.add(request, categoryUid, connMap));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Add' Permission!");
			}
		}catch(IllegalArgumentException e) {
			ConnectionController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editConnection(HttpServletRequest request, String categoryUid, @RequestBody Map<String, String> connMap) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkPermission(peopleId, "connection", connMap.get("connectionuid"),  "modify")) {
				return ResponseEntity.ok(this.service.edit(categoryUid, connMap));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		}catch(IllegalArgumentException e) {
			ConnectionController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/delete")
	public ResponseEntity<?> deleteConnectionByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if(this.authzService.checkPermission(peopleId, "connection", uid,  "delete")) {
				this.service.deleteByUid(uid);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Delete' Permission!");
			}
		}catch(IllegalArgumentException e) {
			ConnectionController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@PostMapping("/test-jdbc-conn")
	public ResponseEntity<?> testJDBCConnection(HttpServletRequest request, String schema, @RequestBody JDBCConnection jdbcConn) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkFuncPermission(peopleId, "connection", "view")) {
				return ResponseEntity.ok(this.service.testJDBCConnection(schema, jdbcConn));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No 'View' Permission!");
			}
		}catch(IllegalArgumentException e) {
			ConnectionController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(SQLException e) {
			ConnectionController.LOGGER.error("SQLException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(ClassNotFoundException e) {
			ConnectionController.LOGGER.error("ClassNotFoundException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isConnectionExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findJDBCDriverInfo")
	public ResponseEntity<?> findJDBCDriverInfo() {
		try {
			return ResponseEntity.ok(this.service.getJDBCDriverInfo());
		}catch(IllegalArgumentException e) {
			ConnectionController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ConnectionController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
