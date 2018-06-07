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

import com.netpro.trinity.repository.frequency.entity.WorkingCalendarList;
import com.netpro.trinity.repository.frequency.service.WorkingCalendarListService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/working-calendar-list")
public class WorkingCalendarListController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkingCalendarListController.class);
		
	@Autowired
	private WorkingCalendarListService service;
	
	@GetMapping("/findByWorkingCalendarUid")
	public ResponseEntity<?> findListByWCUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByWCUid(uid));
		}catch(IllegalArgumentException e) {
			WorkingCalendarListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			WorkingCalendarListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneWorkingCalendarList(@RequestBody WorkingCalendarList list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			WorkingCalendarListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			WorkingCalendarListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyWorkingCalendarList(String wCalendarUid, @RequestBody List<WorkingCalendarList> lists) {
		try {
			return ResponseEntity.ok(this.service.add(wCalendarUid, lists));
		}catch(IllegalArgumentException e) {
			WorkingCalendarListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			WorkingCalendarListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchWorkingCalendarList(String wCalendarUid, @RequestBody List<WorkingCalendarList> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(wCalendarUid, lists));
		}catch(Exception e) {
			WorkingCalendarListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByWorkingCalendarUid")
	public ResponseEntity<?> deleteListByWCUid(String uid) {
		try {
			this.service.deleteByWCUid(uid);
		}catch(IllegalArgumentException e) {
			WorkingCalendarListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			WorkingCalendarListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
}
