package com.netpro.trinity.service.drivermanager.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.netpro.trinity.service.drivermanager.dto.DriverInfo;
import com.netpro.trinity.service.drivermanager.service.DriverManagerService;

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
	
	@GetMapping("/findAllDriverNames")
	public ResponseEntity<?> getAllDriverNames() {
		try {
			return ResponseEntity.ok(this.service.getAllDriverNames());
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
			return ResponseEntity.ok(this.service.modifyDriverYAML(info));
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
	
	@GetMapping("/exportDriverZIP")
	public ResponseEntity<?> exportDriverZIP(HttpServletRequest request, String jarName) {
		try {
			ByteArrayResource resource = new ByteArrayResource(this.service.exportDriverZIP(request));
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set("Content-Disposition","attachment;filename=jdbc.zip");

			return ResponseEntity.ok()
		            .headers(httpHeaders)
		            .contentType(MediaType.parseMediaType("application/zip"))
		            .body(resource);
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/importDriverZIP")
	public ResponseEntity<?> importDriverZIP(MultipartFile file) {
		try {
			return ResponseEntity.ok(this.service.importDriverZIP(file));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/publishDriver")
	public ResponseEntity<?> publishDriver(@RequestBody List<String> driverNames) {
		try {
			return ResponseEntity.ok(this.service.publishDriver(driverNames));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(UnknownHostException e) {
			DriverManagerController.LOGGER.error("UnknownHostException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(FileNotFoundException e) {
			DriverManagerController.LOGGER.error("FileNotFoundException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
