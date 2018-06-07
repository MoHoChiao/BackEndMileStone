package com.netpro.trinity.repository.frequency.controller;

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

import com.netpro.trinity.repository.frequency.entity.FrequencyList;
import com.netpro.trinity.repository.frequency.service.FrequencyListService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/frequency-list")
public class FrequencyListController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyListController.class);
		
	@Autowired
	private FrequencyListService service;
	
	@GetMapping("/findByFrequencyUid")
	public ResponseEntity<?> findListByFreqUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findDistinctDateByFrequencyUid")
	public ResponseEntity<?> findDistinctDateListByFreqUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getDistinctDateByFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findDistinctTimeByFrequencyUid")
	public ResponseEntity<?> findDistinctTimeListByFreqUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getDistinctTimeByFrequencyUid(uid));
		}catch(IllegalArgumentException e) {
			FrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneFrequencyList(@RequestBody FrequencyList list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			FrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyFrequencyList(String freqUid, @RequestBody List<FrequencyList> lists) {
		try {
			return ResponseEntity.ok(this.service.add(freqUid, lists));
		}catch(IllegalArgumentException e) {
			FrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchFrequencyList(String freqUid, @RequestBody List<FrequencyList> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(freqUid, lists));
		}catch(Exception e) {
			FrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByFrequencyUid")
	public ResponseEntity<?> deleteListByFreqUid(String uid) {
		try {
			this.service.deleteByFrequencyUid(uid);
		}catch(IllegalArgumentException e) {
			FrequencyListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			FrequencyListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
