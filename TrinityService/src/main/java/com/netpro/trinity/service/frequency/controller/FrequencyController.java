package com.netpro.trinity.service.frequency.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.netpro.trinity.service.dto.FilterInfo;
import com.netpro.trinity.service.frequency.entity.Frequency;
import com.netpro.trinity.service.frequency.service.FrequencyService;
import com.netpro.trinity.service.permission.feign.PermissionClient;
import com.netpro.trinity.service.util.ACUtil;

@RestController // 宣告一個Restful Web Service的Resource
@RequestMapping("/frequency")
public class FrequencyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyController.class);

	@Autowired
	private FrequencyService service;

	@Autowired
	private PermissionClient permissionClient;

	@GetMapping("/findAll")
	public ResponseEntity<?> findAllFrequencies() {
		try {
			return ResponseEntity.ok(this.service.getAll());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/findAll-without-in-category")
	public ResponseEntity<?> findAllFrequenciesWithoutInCategory() {
		try {
			return ResponseEntity.ok(this.service.getAllWithoutInCategory());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/findByUid")
	public ResponseEntity<?> findFrequencyByUid(HttpServletRequest request, String uid) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkPermission(peopleId, "frequency", uid, "view")) {
				return ResponseEntity.ok(this.service.getByUid(uid));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (IllegalArgumentException e) {
			FrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/findByName")
	public ResponseEntity<?> findFrequenciesByName(String name) {
		try {
			return ResponseEntity.ok(this.service.getByName(name));
		} catch (IllegalArgumentException e) {
			FrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/findByCategoryUid")
	public ResponseEntity<?> findFrequenciesByCategoryUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.getByCategoryUid(uid));
		} catch (IllegalArgumentException e) {
			FrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/findByFilter")
	public ResponseEntity<?> findFrequenciesByFilter(HttpServletRequest request, String categoryUid,
			@RequestBody FilterInfo filter) {
		try {
			String peopleId = ACUtil.getUserIdFromAC(request);
			if (this.permissionClient.checkFuncPermission(peopleId, "frequency", "view")) {
				return this.service.getByFilter(categoryUid, filter);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/add")
	public ResponseEntity<?> addFrequency(String categoryUid, @RequestBody Frequency freq) {
		try {
			return ResponseEntity.ok(this.service.add(categoryUid, freq));
		} catch (IllegalArgumentException e) {
			FrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/edit")
	public ResponseEntity<?> editFrequency(String categoryUid, @RequestBody Frequency freq) {
		try {
			return ResponseEntity.ok(this.service.edit(categoryUid, freq));
		} catch (IllegalArgumentException e) {
			FrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/delete")
	public ResponseEntity<?> deleteFrequencyByUid(String uid) {
		try {
			this.service.deleteByUid(uid);
		} catch (IllegalArgumentException e) {
			FrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(uid);
	}

	@GetMapping("/isExistByUid")
	public ResponseEntity<?> isFrequencyExistByUid(String uid) {
		try {
			return ResponseEntity.ok(this.service.existByUid(uid));
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/isExistByWCalendarUid")
	public ResponseEntity<?> isFrequencyExistByWCalendaruid(String wcalendaruid) {
		try {
			return ResponseEntity.ok(this.service.existByWCalendaruid(wcalendaruid));
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
