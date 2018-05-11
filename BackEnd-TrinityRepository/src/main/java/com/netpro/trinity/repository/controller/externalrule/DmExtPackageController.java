package com.netpro.trinity.repository.controller.externalrule;

import java.io.IOException;

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

import com.netpro.trinity.repository.entity.externalrule.jpa.Dmextpackage;
import com.netpro.trinity.repository.service.externalrule.DmExtPackageService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/dm-ext-package")
public class DmExtPackageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtPackageController.class);
		
	@Autowired
	private DmExtPackageService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllPackages(Boolean withoutDetail) {
		try {
			return ResponseEntity.ok(this.service.getAll(withoutDetail));
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findPackageByUid(Boolean withoutDetail, String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(withoutDetail, uid));
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findPackagesByName(Boolean withoutDetail, String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(withoutDetail, name));
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/add")
	public ResponseEntity<?> addPackage(@RequestBody Dmextpackage p) {
		try {
			return ResponseEntity.ok(this.service.add(p));
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
//	
//	@PostMapping("/edit")
//	public ResponseEntity<?> editDomain(@RequestBody Domain domain) {
//		try {
//			return ResponseEntity.ok(this.service.edit(domain));
//		}catch(IllegalArgumentException e) {
//			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}catch(Exception e) {
//			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//		}
//	}
  
	@GetMapping("/delete")
	public ResponseEntity<?> deletePackageByUid(String uid) {
		try {
			this.service.deleteByUid(uid);
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtPackageController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isPackageExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByName")
	public ResponseEntity<?> isPackageExistByName(String packageName) {
		try {
			return ResponseEntity.ok(this.service.existByName(packageName));
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
