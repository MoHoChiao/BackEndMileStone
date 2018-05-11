package com.netpro.trinity.repository.controller.externalrule;

import java.io.IOException;
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

import com.netpro.trinity.repository.entity.externalrule.jdbc.DmExtRule;
import com.netpro.trinity.repository.service.externalrule.DmExtRuleService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/dm-ext-rule")
public class DmExtRuleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DmExtRuleController.class);
		
	@Autowired
	private DmExtRuleService service;
	
	@GetMapping("/findByJarUid")
	public ResponseEntity<?> findRulesByDJarUid(String extJarUid) {
		try {
			return ResponseEntity.ok(this.service.getByExtJarUid(extJarUid));
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findFullClassPathsByJarUid")
	public ResponseEntity<?> findFullClassPathsByJarUid(String extJarUid) {
		try {
			return ResponseEntity.ok(this.service.getFullClassPathsByExtJarUid(extJarUid));
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneRule(@RequestBody DmExtRule rule) {
		try {
			return ResponseEntity.ok(this.service.add(rule));
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyRules(String extJarUid, @RequestBody List<DmExtRule> rules) {
		try {
			return ResponseEntity.ok(this.service.add(extJarUid, rules));
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchRules(String extJarUid, @RequestBody List<DmExtRule> rules) {
		try {
			return ResponseEntity.ok(this.service.addBatch(extJarUid, rules));
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByJarUid")
	public ResponseEntity<?> deleteJarsByJarUid(String extJarUid) {
		try {
			this.service.deleteByExtJarUid(extJarUid);
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtRuleController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(extJarUid);
	}
	
	@GetMapping("/deleteByAllPKs")
	public ResponseEntity<?> deleteJarByAllPKs(String extJarUid, String ruleName) {
		try {
			this.service.deleteByAllPKs(extJarUid, ruleName);
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtRuleController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(extJarUid + ", " + ruleName);
	}
}
