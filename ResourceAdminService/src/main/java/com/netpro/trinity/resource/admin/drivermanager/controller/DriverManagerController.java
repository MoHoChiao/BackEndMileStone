package com.netpro.trinity.resource.admin.drivermanager.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.netpro.trinity.resource.admin.authz.dto.PermissionTable;
import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.drivermanager.dto.DriverInfo;
import com.netpro.trinity.resource.admin.drivermanager.service.DriverManagerService;
import com.netpro.trinity.resource.admin.util.ACUtil;

@RestController // 宣告一個Restful Web Service的Resource
@RequestMapping("/driver-manager")
public class DriverManagerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerController.class);

	@Autowired
	private DriverManagerService service;

	@Autowired
	private AuthzService authzService;

	@GetMapping("/findDriversProp")
	public ResponseEntity<?> findDriversProp(HttpServletRequest request, String driverName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.getDriversProp(driverName));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/findAllDriverNames")
	public ResponseEntity<?> getAllDriverNames(HttpServletRequest request) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.getAllDriverNames());
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/findJarFilesByDriverName")
	public ResponseEntity<?> findJarFilesByDriverName(HttpServletRequest request, String driverName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.getJarFilesByDriverName(driverName));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/findDriverClassByDriverName")
	public ResponseEntity<?> findDriverClassByDriverName(HttpServletRequest request, String driverName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.getDriverClassByDriverName(driverName));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/addDriverFolderAndProp")
	public ResponseEntity<?> addDriverFolderAndProp(HttpServletRequest request, String driverName, String driverURL, MultipartFile[] files) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.addDriverFolderAndProp(driverName, driverURL, files));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/addJarFileByDriverName")
	public ResponseEntity<?> addJarFileByDriverName(HttpServletRequest request, String driverName, MultipartFile[] files) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.addJarFileByDriverName(driverName, files));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/modifyDriverProp")
	public ResponseEntity<?> modifyDriverProp(HttpServletRequest request, @RequestBody DriverInfo info) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.modifyDriver(info));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/deleteJarFile")
	public ResponseEntity<?> deleteJarFile(HttpServletRequest request, String driverName, String jarName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.deleteJarFile(driverName, jarName));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/deleteDriverFolderAndProp")
	public ResponseEntity<?> deleteDriverFolderAndProp(HttpServletRequest request, String driverName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				Boolean delFlag = this.service.deleteDriverFolder(driverName);
				if (delFlag) {
					return ResponseEntity.ok(this.service.deleteDriverProp(driverName));
				} else {
					throw new Exception("Delete Driver Folder Fail!");
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/deleteDriverFolder")
	public ResponseEntity<?> deleteDriverFolder(HttpServletRequest request, String driverName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.deleteDriverFolder(driverName));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/deleteDriverProp")
	public ResponseEntity<?> deleteDriverProp(HttpServletRequest request, String driverName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.deleteDriverProp(driverName));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/exportDriverZIP")
	public ResponseEntity<?> exportDriverZIP(HttpServletRequest request, String jarName) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				ByteArrayResource resource = new ByteArrayResource(this.service.exportDriverZIP(request));

				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.set("Content-Disposition", "attachment;filename=jdbc.zip");

				return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.parseMediaType("application/zip"))
						.body(resource);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/importDriverZIP")
	public ResponseEntity<?> importDriverZIP(HttpServletRequest request, MultipartFile file) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.importDriverZIP(file));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/publishDriver")
	public ResponseEntity<?> publishDriver(HttpServletRequest request, @RequestBody Map<String, List<String>> params) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			PermissionTable permissionTable = this.authzService.getPermissionTable(peopleId);
			if (permissionTable.isRoot()) {
				return ResponseEntity.ok(this.service.publishDriver(params));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have Permission!");
			}
		} catch (IllegalArgumentException e) {
			DriverManagerController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (UnknownHostException e) {
			DriverManagerController.LOGGER.error("UnknownHostException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (FileNotFoundException e) {
			DriverManagerController.LOGGER.error("FileNotFoundException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IOException e) {
			DriverManagerController.LOGGER.error("IOException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		} catch (Exception e) {
			DriverManagerController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
