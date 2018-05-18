package com.netpro.trinity.repository.controller.externalrule;

import java.io.IOException;
import java.util.zip.ZipException;

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
	public ResponseEntity<?> findRulesByJarUid(String extJarUid) {
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
	
	@GetMapping("/findByAllPKs")
	public ResponseEntity<?> findRuleByAllPKs(String extJarUid, String ruleName) {
		try {
			return ResponseEntity.ok(this.service.getByAllPKs(extJarUid, ruleName));
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
	
	@GetMapping("/findNonSettingRulesByJarUid")
	public ResponseEntity<?> findNonSettingRulesByJarUid(String extJarUid) {
		try {
			return ResponseEntity.ok(this.service.getNonSettingRulesByExtJarUid(extJarUid));
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(ZipException e) {
			DmExtRuleController.LOGGER.error("ZipException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtRuleController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findSettingRulesByPackageUid")
	public ResponseEntity<?> findSettingRulesByPackageUid(String packageUid) {
		try {
			return ResponseEntity.ok(this.service.getSettingRulesByPackageUid(packageUid));
		}catch(IllegalArgumentException e) {
			DmExtRuleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(ZipException e) {
			DmExtRuleController.LOGGER.error("ZipException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DmExtRuleController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DmExtRuleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addRule(@RequestBody DmExtRule rule) {
		try {
			return ResponseEntity.ok(this.service.addRule(rule));
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
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editRule(String targetRuleName, @RequestBody DmExtRule rule) {
		try {
			return ResponseEntity.ok(this.service.editRule(targetRuleName, rule));
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
