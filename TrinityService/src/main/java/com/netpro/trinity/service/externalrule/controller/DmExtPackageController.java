package com.netpro.trinity.service.externalrule.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.service.externalrule.dto.Publication;
import com.netpro.trinity.service.externalrule.entity.Dmextpackage;
import com.netpro.trinity.service.externalrule.service.DmExtPackageService;

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
  
	@GetMapping("/findPublicationsByAgentUid")
	public ResponseEntity<?> findPublicationsByAgentUid(String agentUid) {
		try {
			return ResponseEntity.ok(this.service.getPublicationsByAgentUid(agentUid));
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
			return ResponseEntity.ok(this.service.addPackage(p));
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editPackage(@RequestBody Dmextpackage new_p) {
		try {
			return ResponseEntity.ok(this.service.editPackage(new_p));
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(NoSuchAlgorithmException e) {
			DmExtPackageController.LOGGER.error("NoSuchAlgorithmException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtPackageController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/import")
	public ResponseEntity<?> importPackage(MultipartFile file) {
		try {
			return ResponseEntity.ok(this.service.importPackage(file));
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(FileNotFoundException e) {
			DmExtPackageController.LOGGER.error("FileNotFoundException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(NoSuchAlgorithmException e) {
			DmExtPackageController.LOGGER.error("NoSuchAlgorithmException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtPackageController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/publish")
	public ResponseEntity<?> publishPackage(@RequestBody List<Publication> publishedRules) {
		try {
			return ResponseEntity.ok(this.service.publishPackage(publishedRules));
		}catch(IllegalArgumentException e) {
			DmExtPackageController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(NoSuchAlgorithmException e) {
			DmExtPackageController.LOGGER.error("NoSuchAlgorithmException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtPackageController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtPackageController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
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
