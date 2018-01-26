package com.netpro.trinity.repository.controller.frequency;

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

import com.netpro.trinity.repository.entity.frequency.jdbc.ExclFrequencyList;
import com.netpro.trinity.repository.service.frequency.ExclFrequencyListService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/excl-frequency-list")
public class ExclFrequencyListController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExclFrequencyListController.class);
		
	@Autowired
	private ExclFrequencyListService service;
	
	@GetMapping("/findByExcludeFrequencyUid")
	public ResponseEntity<?> findListByExcludeFrequencyUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByExclFreqUid(uid));
		}catch(IllegalArgumentException e) {
			ExclFrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneExcludeFrequencyList(@RequestBody ExclFrequencyList list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			ExclFrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyExcludeFrequencyList(String excludeFrequencyUid, @RequestBody List<ExclFrequencyList> lists) {
		try {
			return ResponseEntity.ok(this.service.add(excludeFrequencyUid, lists));
		}catch(IllegalArgumentException e) {
			ExclFrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchExcludeFrequencyList(String excludeFrequencyUid, @RequestBody List<ExclFrequencyList> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(excludeFrequencyUid, lists));
		}catch(Exception e) {
			ExclFrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByExcludeFrequencyUid")
	public ResponseEntity<?> deleteListByExcludeFrequencyUid(String uid) {
		try {
			this.service.deleteByExclFreqUid(uid);
		}catch(IllegalArgumentException e) {
			ExclFrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ExclFrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
