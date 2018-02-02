package com.netpro.trinity.repository.controller.Domain;

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

import com.netpro.trinity.repository.entity.domain.jdbc.DomainVariable;
import com.netpro.trinity.repository.service.domain.DomainVariableService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/domain-variable")
public class DomainVariableController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainVariableController.class);
		
	@Autowired
	private DomainVariableService service;
	
	@GetMapping("/findByDomainUid")
	public ResponseEntity<?> findVarsByDomainUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByDomainUid(uid));
		}catch(IllegalArgumentException e) {
			DomainVariableController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainVariableController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneVar(@RequestBody DomainVariable var) {
		try {
			return ResponseEntity.ok(this.service.add(var));
		}catch(IllegalArgumentException e) {
			DomainVariableController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainVariableController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyVars(String domainUid, @RequestBody List<DomainVariable> vars) {
		try {
			return ResponseEntity.ok(this.service.add(domainUid, vars));
		}catch(IllegalArgumentException e) {
			DomainVariableController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainVariableController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchVars(String domainUid, @RequestBody List<DomainVariable> vars) {
		try {
			return ResponseEntity.ok(this.service.addBatch(domainUid, vars));
		}catch(Exception e) {
			DomainVariableController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByDomainUid")
	public ResponseEntity<?> deleteVarsByDomainUid(String uid) {
		try {
			this.service.deleteByDomainUid(uid);
		}catch(IllegalArgumentException e) {
			DomainVariableController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainVariableController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
