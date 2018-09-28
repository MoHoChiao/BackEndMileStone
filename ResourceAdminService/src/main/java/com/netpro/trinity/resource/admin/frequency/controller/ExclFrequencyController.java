package com.netpro.trinity.resource.admin.frequency.controller;

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
import com.netpro.trinity.resource.admin.frequency.entity.ExclFrequency;
import com.netpro.trinity.resource.admin.frequency.service.ExclFrequencyService;
import com.netpro.trinity.resource.admin.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/excl-frequency")
public class ExclFrequencyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExclFrequencyController.class);
		
	@Autowired
	private ExclFrequencyService service;
	
	@Autowired
	private AuthzService authzService;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllExcludeFrequency(HttpServletRequest request, Boolean withoutDetail) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkFuncPermission(peopleId, "frequency", "view")) {
				return ResponseEntity.ok(this.service.getAll(withoutDetail));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findExcludeFrequencyById(Boolean withoutDetail, String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(withoutDetail, uid));
		}catch(IllegalArgumentException e) {
			ExclFrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByGlobal")
	public ResponseEntity<?> findExcludeFrequencyByGlobal(Boolean withoutDetail) {
		try {
			return ResponseEntity.ok(this.service.getByGlobal(withoutDetail));
		}catch(IllegalArgumentException e) {
			ExclFrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByName")
	public ResponseEntity<?> findExcludeFrequencyByName(Boolean withoutDetail, String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(withoutDetail, name));
		}catch(IllegalArgumentException e) {
			ExclFrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findExcludeFrequencyByFilter(HttpServletRequest request, Boolean withoutDetail, @RequestBody FilterInfo filter) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkFuncPermission(peopleId, "frequency", "view")) {
				return this.service.getByFilter(withoutDetail, filter);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		}catch(Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/add")
	public ResponseEntity<?> addExcludeFrequency(HttpServletRequest request, @RequestBody ExclFrequency excl) {
		try {

			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkFuncPermission(peopleId, "frequency", "add")) {
				return ResponseEntity.ok(this.service.add(excl));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Add' Permission!");
			}
		} catch (Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editExcludeFrequency(HttpServletRequest request, @RequestBody ExclFrequency excl) {
		try {
			
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkPermission(peopleId, "frequency", excl.getExcludefrequencyuid(), "modify")) {
				return ResponseEntity.ok(this.service.edit(excl));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		}catch(IllegalArgumentException e) {
			ExclFrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/modifyGlobal")
	public ResponseEntity<?> modifyGlobalExcludeFrequency(@RequestBody ExclFrequency excl) {
		try {
			return ResponseEntity.ok(this.service.modifyGlobal(excl));
		}catch(Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> deleteExcludeFrequencyByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkPermission(peopleId, "frequency", uid, "delete")) {
				this.service.deleteByUid(uid);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Delete' Permission!");
			}
		} catch (Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isExcludeFrequencyExistByUid(HttpServletRequest request, String uid) {
		try {

			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.authzService.checkFuncPermission(peopleId, "frequency", "view")) {
				return ResponseEntity.ok(this.service.existByUid(uid));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			ExclFrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
