package com.netpro.trinity.service.externalrule.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.externalrule.dto.Publication;
import com.netpro.trinity.service.externalrule.entity.Dmextpackage;
import com.netpro.trinity.service.externalrule.service.DmExtPackageService;
import com.netpro.trinity.service.permission.feign.PermissionClient;
import com.netpro.trinity.service.util.ACUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/dm-ext-package")
public class DmExtPackageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtPackageController.class);
		
	@Autowired
	private DmExtPackageService service;
	
	@Autowired
	private PermissionClient permissionClient;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllPackages(HttpServletRequest request, Boolean withoutDetail) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "view")) {
				return ResponseEntity.ok(this.service.getAll(withoutDetail));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/findByFilter")
	public ResponseEntity<?> findByFilter(HttpServletRequest request, @RequestBody FilterInfo filter) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "view")) {
				return this.service.getByFilter(filter);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findPackageByUid(HttpServletRequest request, Boolean withoutDetail, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "view")) {
				return ResponseEntity.ok(this.service.getByUid(withoutDetail, uid));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	// TODO permission
	@GetMapping("/findByName")
	public ResponseEntity<?> findPackagesByName(Boolean withoutDetail, String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(withoutDetail, name));
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findPublicationsByAgentUid")
	public ResponseEntity<?> findPublicationsByAgentUid(HttpServletRequest request, String agentUid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "view")) {
				return ResponseEntity.ok(this.service.getPublicationsByAgentUid(agentUid));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addPackage(HttpServletRequest request, @RequestBody Dmextpackage p) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "edit")) {
				return ResponseEntity.ok(this.service.addPackage(p));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editPackage(HttpServletRequest request, @RequestBody Dmextpackage new_p) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "edit")) {
				return ResponseEntity.ok(this.service.editPackage(new_p));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/import")
	public ResponseEntity<?> importPackage(HttpServletRequest request, MultipartFile file) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "edit")) {
				return ResponseEntity.ok(this.service.importPackage(file));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/publish")
	public ResponseEntity<?> publishPackage(HttpServletRequest request, @RequestBody List<Publication> publishedRules) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "edit")) {
				return ResponseEntity.ok(this.service.publishPackage(publishedRules));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> deletePackageByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "edit")) {
				this.service.deleteByUid(uid);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'Edit' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isPackageExistByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "view")) {
				return ResponseEntity.ok(this.service.existByUid(uid));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByName")
	public ResponseEntity<?> isPackageExistByName(HttpServletRequest request, String packageName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "extrule", "view")) {
				return ResponseEntity.ok(this.service.existByName(packageName));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
