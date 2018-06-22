package com.netpro.trinity.service.notification.controller;

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

import com.netpro.trinity.service.notification.entity.Notification;
import com.netpro.trinity.service.notification.service.NotificationService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/notification")
public class NotificationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
		
	@Autowired
	private NotificationService service;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllNotifications(Boolean withoutDetail) {
		try {
			return ResponseEntity.ok(this.service.getAll(withoutDetail));
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByUid")
	public ResponseEntity<?> findNotificationByUid(Boolean withoutDetail, String notificationUid) {
		try {
			return ResponseEntity.ok(this.service.getByUid(withoutDetail, notificationUid));
		}catch(IllegalArgumentException e) {
			NotificationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findNotificationsByName(Boolean withoutDetail, String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(withoutDetail, name));
		}catch(IllegalArgumentException e) {
			NotificationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/modify")
	public ResponseEntity<?> modifyNotification(@RequestBody Notification notice) {
		try {
			return ResponseEntity.ok(this.service.modify(notice));
		}catch(IllegalArgumentException e) {
			NotificationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addNotification(@RequestBody Notification notice) {
		try {
			return ResponseEntity.ok(this.service.add(notice));
		}catch(IllegalArgumentException e) {
			NotificationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/edit")
	public ResponseEntity<?> editNotification(@RequestBody Notification notice) {
		try {
			return ResponseEntity.ok(this.service.edit(notice));
		}catch(IllegalArgumentException e) {
			NotificationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public ResponseEntity<?> deleteNotificationByUid(String uid) {
		try {
			this.service.deleteByUid(uid);
		}catch(IllegalArgumentException e) {
			NotificationController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}
	
	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isNotificationExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		}catch(Exception e) {
			NotificationController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
