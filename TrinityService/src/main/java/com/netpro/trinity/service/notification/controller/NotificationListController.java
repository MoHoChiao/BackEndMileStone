package com.netpro.trinity.service.notification.controller;

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

import com.netpro.trinity.service.notification.entity.NotificationList;
import com.netpro.trinity.service.notification.service.NotificationListService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/notification-list")
public class NotificationListController {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationListController.class);
		
	@Autowired
	private NotificationListService service;
	
	@GetMapping("/findByNotificationUid")
	public ResponseEntity<?> findByNotifyUid(String notificationUid) {
		try {
			return ResponseEntity.ok(this.service.getByNotifyUid(notificationUid));
		}catch(IllegalArgumentException e) {
			NotificationListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findUserExtraByNotificationUid")
	public ResponseEntity<?> findUserExtraByNotifyUid(String notificationUid) {
		try {
			return ResponseEntity.ok(this.service.getUserExByNotifyUid(notificationUid));
		}catch(IllegalArgumentException e) {
			NotificationListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findGroupExtraByNotificationUid")
	public ResponseEntity<?> findGroupExtraByNotifyUid(String notificationUid) {
		try {
			return ResponseEntity.ok(this.service.getGroupExByNotifyUid(notificationUid));
		}catch(IllegalArgumentException e) {
			NotificationListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneNotifyList(@RequestBody NotificationList list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			NotificationListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyNotifyList(String notificationUid, @RequestBody List<NotificationList> lists) {
		try {
			return ResponseEntity.ok(this.service.add(notificationUid, lists));
		}catch(IllegalArgumentException e) {
			NotificationListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addBatch")
	public ResponseEntity<?> addBatchNotifyList(String notificationUid, @RequestBody List<NotificationList> lists) {
		try {
			return ResponseEntity.ok(this.service.addBatch(notificationUid, lists));
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByNotificationUid")
	public ResponseEntity<?> deleteListByNotifyUid(String notificationUid) {
		try {
			this.service.deleteByNotifyUid(notificationUid);
		}catch(IllegalArgumentException e) {
			NotificationListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(notificationUid);
	}
	
	@GetMapping("/deleteByDestinationUid")
	public ResponseEntity<?> deleteListByDestinationUid(String destinationUid) {
		try {
			this.service.deleteByDestinationUid(destinationUid);
		}catch(IllegalArgumentException e) {
			NotificationListController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			NotificationListController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(destinationUid);
	}
}
