package com.netpro.trinity.service.externalrule.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.service.externalrule.service.DmExtJarService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/dm-ext-jar")
public class DmExtJarController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtJarController.class);
		
	@Autowired
	private DmExtJarService service;
	
	@GetMapping("/findByJarUid")
	public ResponseEntity<?> findJarByJarUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(uid));
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByPackageUid")
	public ResponseEntity<?> findJarsByPackageUid(String packageUid) {
		try {
			return ResponseEntity.ok(this.service.getByPackageUid(packageUid));
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByFileName")
	public ResponseEntity<?> findJarsByFileName(String fileName) {
		try {
			return ResponseEntity.ok(this.service.getByFileName(fileName));
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addJar(String packageUid, String description, MultipartFile file) {
		try {
			return ResponseEntity.ok(this.service.addJar(packageUid, description, file));
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/editDescriptionOnly")
	public ResponseEntity<?> editDescriptionOnly(String extJarUid, String newDesc) {
		try {
			return ResponseEntity.ok(this.service.editDescriptionOnly(extJarUid, newDesc));
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByPackageUid")
	public ResponseEntity<?> deleteJarByPackageUid(String packageUid) {
		try {
			this.service.deleteByPackageUid(packageUid);
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtJarController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(packageUid);
	}
	
	@GetMapping("/deleteByJarUid")
	public ResponseEntity<?> deleteJarByJarUid(String uid) {
		try {
			this.service.deleteByUid(uid);
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtJarController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/isExistByJarUid")
	public ResponseEntity<?> isJarExistByJarUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(IllegalArgumentException e) {
			DmExtJarController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtJarController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
