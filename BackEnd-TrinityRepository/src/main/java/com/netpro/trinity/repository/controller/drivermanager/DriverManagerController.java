package com.netpro.trinity.repository.controller.drivermanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.prop.TrinityDataJDBC;
import com.netpro.trinity.repository.prop.TrinityDataJDBC.JDBCDriverInfo;
import com.netpro.trinity.repository.service.drivermanager.DriverManagerService;

@RestController  //宣告一個Restful Web Service的Resource
@RefreshScope
@RequestMapping("/driver-manager")
public class DriverManagerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerController.class);
	
	@Autowired
	private DriverManagerService service;
	
	@GetMapping("/findDriversInfo")
	public ResponseEntity<?> findDriversInfo(String driverName) {
		try {
			return ResponseEntity.ok(this.service.getDriversInfo(driverName));
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findDriverJarFiles")
	public ResponseEntity<?> findDriverJarFiles(String driverName) {
		try {
			return ResponseEntity.ok(this.service.getDriverJarFiles(driverName));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteDriverJarFile")
	public ResponseEntity<?> deleteDriverJarFile(String driverName, String jarName) {
		try {
			return ResponseEntity.ok(this.service.deleteDriverJarFile(driverName, jarName));
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/addtest")
	public ResponseEntity<?> addtest() {
		try {
			return ResponseEntity.ok(this.service.addDriverInfo());
		}catch(IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
//	@Value("${trinity-data-jdbc.info.DB2}")
//    private Object metadata1;
//	
//    @RequestMapping("/metadata1")
//    Object getMessage() {
//        return this.metadata1;
//    }
}
