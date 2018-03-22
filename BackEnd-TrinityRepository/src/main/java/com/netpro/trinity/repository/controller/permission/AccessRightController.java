package com.netpro.trinity.repository.controller.permission;

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

import com.netpro.trinity.repository.entity.permission.jdbc.AccessRight;
import com.netpro.trinity.repository.service.permission.AccessRightService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/access-right")
public class AccessRightController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccessRightController.class);
		
	@Autowired
	private AccessRightService service;
	
	@GetMapping("/findByPeopleUid")
	public ResponseEntity<?> findByPeopleUid(String peopleUid) {
		try {
			return ResponseEntity.ok(this.service.getByPeopleUid(peopleUid));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByObjectUid")
	public ResponseEntity<?> findByObjectUid(String objectUid) {
		try {
			return ResponseEntity.ok(this.service.getByObjectUid(objectUid));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByAllPKs")
	public ResponseEntity<?> findByAllPKs(String peopleUid, String objectUid) {
		try {
			return ResponseEntity.ok(this.service.getByAllPKs(peopleUid, objectUid));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFunctionalPermissionByPeopleUid")
	public ResponseEntity<?> findFunctionalPermissionByPeopleUid(String peopleUid) {
		try {
			return ResponseEntity.ok(this.service.getFunctionalPermissionByPeopleUid(peopleUid));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneAccessRight(@RequestBody AccessRight list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyAccessRight(@RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.add(lists));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/updateOne")
	public ResponseEntity<?> updateOneAccessRight(@RequestBody AccessRight list) {
		try {
			return ResponseEntity.ok(this.service.update(list));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/updateMany")
	public ResponseEntity<?> updateManyAccessRight(@RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.update(lists));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchAccessRight(@RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(lists));
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/updateBatch")
	public ResponseEntity<?> updateBatchAccessRight(@RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.updateBatch(lists));
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/modifyByPeopleUid")
	public ResponseEntity<?> modifyAccessRightByPeopleUid(String peopleUid, @RequestBody List<AccessRight> lists) {
		try {
			return ResponseEntity.ok(this.service.modifyByPeopleUid(peopleUid, lists));
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByPeopleUid")
	public ResponseEntity<?> deleteAccessRightByPeopleUid(String peopleUid) {
		try {
			this.service.deleteByPeopleUid(peopleUid);
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(peopleUid);
	}
	
	@GetMapping("/deleteByObjectUid")
	public ResponseEntity<?> deleteAccessRightByObjectUid(String objectUid) {
		try {
			this.service.deleteByObjectUid(objectUid);
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(objectUid);
	}
	
	@GetMapping("/deleteByPKs")
	public ResponseEntity<?> deleteAccessRightByPKs(String peopleUid, String objectUid) {
		try {
			this.service.deleteByPKs(peopleUid, objectUid);
		}catch(IllegalArgumentException e) {
			AccessRightController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			AccessRightController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(peopleUid);
	}
}
