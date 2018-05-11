package com.netpro.trinity.repository.controller.transformrule;

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

import com.netpro.trinity.repository.entity.transformrule.jpa.Transformrule;
import com.netpro.trinity.repository.service.transformrule.TransformruleService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/transformrule")
public class TransformruleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransformruleController.class);
		
	@Autowired
	private TransformruleService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllTransformrules() {
		try {
			return ResponseEntity.ok(this.service.getAllTransformrule());
		}catch(Exception e) {
			TransformruleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByRule")
	public ResponseEntity<?> findTransformruleByRule(String rule) {
		try {
			return ResponseEntity.ok(this.service.getByRule(rule));
		}catch(IllegalArgumentException e) {
			TransformruleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TransformruleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/modify")
	public ResponseEntity<?> addTransformrule(@RequestBody Transformrule rule) {
		try {
			return ResponseEntity.ok(this.service.modify(rule));
		}catch(IllegalArgumentException e) {
			TransformruleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TransformruleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/deleteByRule")
	public ResponseEntity<?> deleteTransformruleByRule(String rule) {
		try {
			this.service.deleteByRule(rule);
		}catch(IllegalArgumentException e) {
			TransformruleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TransformruleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(rule);
	}
	
	@GetMapping("/isExistByRule")
	public ResponseEntity<?> isTransformruleExistByRule(String rule) {
		try {
			return ResponseEntity.ok(this.service.existByRule(rule));
		}catch(IllegalArgumentException e) {
			TransformruleController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			TransformruleController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
