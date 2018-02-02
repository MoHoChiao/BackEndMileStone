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

import com.netpro.trinity.repository.entity.domain.jdbc.DomainResource;
import com.netpro.trinity.repository.service.domain.DomainResourceService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/domain-resource")
public class DomainResourceController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainResourceController.class);
		
	@Autowired
	private DomainResourceService service;
	
	@GetMapping("/findByDomainUid")
	public ResponseEntity<?> findResourcesByDomainUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByDomainUid(uid));
		}catch(IllegalArgumentException e) {
			DomainResourceController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainResourceController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneResource(@RequestBody DomainResource resource) {
		try {
			return ResponseEntity.ok(this.service.add(resource));
		}catch(IllegalArgumentException e) {
			DomainResourceController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainResourceController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyResources(String domainUid, @RequestBody List<DomainResource> resources) {
		try {
			return ResponseEntity.ok(this.service.add(domainUid, resources));
		}catch(IllegalArgumentException e) {
			DomainResourceController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainResourceController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchResources(String domainUid, @RequestBody List<DomainResource> resources) {
		try {
			return ResponseEntity.ok(this.service.addBatch(domainUid, resources));
		}catch(Exception e) {
			DomainResourceController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByDomainUid")
	public ResponseEntity<?> deleteResourcesByDomainUid(String uid) {
		try {
			this.service.deleteByDomainUid(uid);
		}catch(IllegalArgumentException e) {
			DomainResourceController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DomainResourceController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
