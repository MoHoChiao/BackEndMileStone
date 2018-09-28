package com.netpro.trinity.resource.admin.job.controller;

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
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.job.entity.Busentity;
import com.netpro.trinity.resource.admin.job.service.BusentityService;
import com.netpro.trinity.resource.admin.util.ACUtil;

/*
 * 此處各位方法的permission部份, 不完整, 目前只填入Resource Admin會用到的權限而已
 * 以後若是加入JFDesigner部份, 則需要再作出相對的修改 (但是就開發的力度而言, 或許...永遠也不會有這一天^^")
 */

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/busentity")
public class BusentityController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusentityController.class);
		
	@Autowired
	private BusentityService service;
	
	@Autowired
	private AuthzService authzService;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllEntities(HttpServletRequest request, Boolean withAlias) {
		try {
			if(null != withAlias && withAlias) {
				String peopleId = ACUtil.getUserIdFromAC(request);
				if(!this.authzService.checkFuncPermission(peopleId, "alias", "view")) {
					withAlias = false;
				}
			}
			return ResponseEntity.ok(this.service.getAll(withAlias));
		}catch(Exception e) {
			BusentityController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isEntityExistByUid(HttpServletRequest request, String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			BusentityController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findEntityByUid(HttpServletRequest request, Boolean withAlias, String uid) {
		try {
			if(null != withAlias && withAlias) {
				String peopleId = ACUtil.getUserIdFromAC(request);
				if(!this.authzService.checkFuncPermission(peopleId, "alias", "view")) {
					withAlias = false;
				}
			}
			return ResponseEntity.ok(this.service.getByUid(withAlias, uid));
		}catch(IllegalArgumentException e) {
			BusentityController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findEntitiesByFilter(HttpServletRequest request, Boolean withAlias, @RequestBody FilterInfo filter) {
		try {
			if(null != withAlias && withAlias) {
				String peopleId = ACUtil.getUserIdFromAC(request);
				if(!this.authzService.checkFuncPermission(peopleId, "alias", "view")) {
					withAlias = false;
				}
			}
			return this.service.getByFilter(withAlias, filter);
		}catch(Exception e) {
			BusentityController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/add")
	public ResponseEntity<?> addEntity(@RequestBody Busentity entity) {
		try {
			return ResponseEntity.ok(this.service.add(entity));
		}catch(IllegalArgumentException e) {
			BusentityController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editEntity(@RequestBody Busentity entity) {
		try {
			return ResponseEntity.ok(this.service.edit(entity));
		}catch(IllegalArgumentException e) {
			BusentityController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/delete")
	public ResponseEntity<?> deleteEntityByUid(String uid) {
		try {
			this.service.deleteByUid(uid);
		}catch(IllegalArgumentException e) {
			BusentityController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			BusentityController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
