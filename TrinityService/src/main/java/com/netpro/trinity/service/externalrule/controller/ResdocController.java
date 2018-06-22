package com.netpro.trinity.service.externalrule.controller;

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

import com.netpro.trinity.service.externalrule.entity.Resdoc;
import com.netpro.trinity.service.externalrule.entity.ResdocPKs;
import com.netpro.trinity.service.externalrule.service.ResdocService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/resdoc")
public class ResdocController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResdocController.class);
		
	@Autowired
	private ResdocService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllResDoc() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		}catch(Exception e) {
			ResdocController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByPKs")
	public ResponseEntity<?> findResDocByPKs(ResdocPKs pks) {
		try {
			return ResponseEntity.ok(this.service.getByPKs(pks));
		}catch(IllegalArgumentException e) {
			ResdocController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ResdocController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/modify")
	public ResponseEntity<?> modifyResDoc(@RequestBody Resdoc doc) {
		try {
			return ResponseEntity.ok(this.service.modify(doc));
		}catch(IllegalArgumentException e) {
			ResdocController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ResdocController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/editResNameOnly")
	public ResponseEntity<?> editResNameOnly(String newResName, @RequestBody Resdoc doc) {
		try {
			return ResponseEntity.ok(this.service.editResNameOnly(newResName, doc));
		}catch(IllegalArgumentException e) {
			ResdocController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ResdocController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByPKs")
	public ResponseEntity<?> deleteResDocByRule(ResdocPKs pks) {
		try {
			this.service.deleteByPKs(pks);
		}catch(IllegalArgumentException e) {
			ResdocController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ResdocController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(pks.getModule() + 
				", " + pks.getResname() + ", " + pks.getResname());
	}
	
	@GetMapping("/deleteByModule")
	public ResponseEntity<?> deleteResDocByRule(String module) {
		try {
			this.service.deleteByModule(module);
		}catch(IllegalArgumentException e) {
			ResdocController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ResdocController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(module);
	}
	
	@GetMapping("/deleteByResName")
	public ResponseEntity<?> deleteResDocByResName(String resName) {
		try {
			this.service.deleteByName(resName);
		}catch(IllegalArgumentException e) {
			ResdocController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ResdocController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(resName);
	}
}
