package com.netpro.trinity.repository.controller.drivermanager;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.repository.dto.drivermanager.DriverInfo;
import com.netpro.trinity.repository.service.drivermanager.DriverManagerService;

@RestController  //宣告一個Restful Web Service的Resource
@RefreshScope
@RequestMapping("/driver-manager")
public class DriverManagerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerController.class);
	
	@Autowired
	private DriverManagerService service;
	
	@GetMapping("/findDriversProp")
	public ResponseEntity<?> findDriversProp(String driverName) {
		try {
			return ResponseEntity.ok(this.service.getDriversProp(driverName));
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findJarFilesByDriverName")
	public ResponseEntity<?> findJarFilesByDriverName(String driverName) {
		try {
			return ResponseEntity.ok(this.service.getJarFilesByDriverName(driverName));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findDriverClassByDriverName")
	public ResponseEntity<?> findDriverClassByDriverName(String driverName) {
		try {
			return ResponseEntity.ok(this.service.getDriverClassByDriverName(driverName));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addDriverFolderAndProp")
	public ResponseEntity<?> addDriverFolderAndProp(String driverName, String driverURL, MultipartFile[] files) {
		try {
			return ResponseEntity.ok(this.service.addDriverFolderAndProp(driverName, driverURL, files));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addJarFileByDriverName")
	public ResponseEntity<?> addJarFileByDriverName(String driverName, MultipartFile[] files) {
		try {
			return ResponseEntity.ok(this.service.addJarFileByDriverName(driverName, files));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/modifyDriverProp")
	public ResponseEntity<?> modifyDriverProp(@RequestBody DriverInfo info) {
		try {
			return ResponseEntity.ok(this.service.modifyDriverProp(info));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteJarFile")
	public ResponseEntity<?> deleteJarFile(String driverName, String jarName) {
		try {
			return ResponseEntity.ok(this.service.deleteJarFile(driverName, jarName));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteDriverFolderAndProp")
	public ResponseEntity<?> deleteDriverFolderAndProp(String driverName) {
		try {
			Boolean delFlag = this.service.deleteDriverFolder(driverName);
			if(delFlag) {
				return ResponseEntity.ok(this.service.deleteDriverProp(driverName));
			}else {
				throw new Exception("Delete Driver Folder Fail!");
			}
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteDriverFolder")
	public ResponseEntity<?> deleteDriverFolder(String driverName) {
		try {
			return ResponseEntity.ok(this.service.deleteDriverFolder(driverName));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteDriverProp")
	public ResponseEntity<?> deleteDriverProp(String driverName) {
		try {
			return ResponseEntity.ok(this.service.deleteDriverProp(driverName));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
