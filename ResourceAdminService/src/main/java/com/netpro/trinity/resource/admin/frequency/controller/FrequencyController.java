package com.netpro.trinity.resource.admin.frequency.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.netpro.trinity.resource.admin.authz.service.AuthzService;
import com.netpro.trinity.resource.admin.dto.FilterInfo;
import com.netpro.trinity.resource.admin.frequency.entity.Frequency;
import com.netpro.trinity.resource.admin.frequency.entity.FrequencyCategory;
import com.netpro.trinity.resource.admin.frequency.service.FrequencyCategoryService;
import com.netpro.trinity.resource.admin.frequency.service.FrequencyService;
import com.netpro.trinity.resource.admin.util.ACUtil;
import com.netpro.trinity.resource.admin.util.JsTreeItem;

@RestController // 宣告一個Restful Web Service的Resource
@RequestMapping("/frequency")
public class FrequencyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyController.class);

	@Autowired
	private FrequencyService service;
	
	@Autowired
	private FrequencyCategoryService fcService;

	@Autowired
	private AuthzService authzService;

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
			if (this.authzService.checkPermission(peopleId, "frequency", uid, "view")) {
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
			if (this.authzService.checkFuncPermission(peopleId, "frequency", "view")) {
				return this.service.getByFilter(categoryUid, filter);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You do not have 'View' Permission!");
			}
		} catch (Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/findForTree")
	public ResponseEntity<?> findForTree(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		String cate_icon = (String) map.get("l1_icon");
		String freq_icon = (String) map.get("l2_icon");
		List<String> freqList = (List<String>) map.get("frequencyUids");
		
		try {
			List<JsTreeItem> nodeList = new ArrayList<JsTreeItem>();
			List<FrequencyCategory> cateList = this.fcService.getAll();
			
			for (FrequencyCategory fc : cateList) {
				JsTreeItem node = new JsTreeItem();
				node.setId(fc.getFreqcategoryuid());
				node.setName(fc.getFreqcategoryname());
				node.setIcon(cate_icon);
				
				List<JsTreeItem> childList = new ArrayList<JsTreeItem>();
				List<Frequency> fList = this.service.getByCategoryUid(fc.getFreqcategoryuid());
				for (Frequency f : fList) {
					JsTreeItem childNode = new JsTreeItem();
					childNode.setId(f.getFrequencyuid());
					childNode.setName(f.getFrequencyname());
					childNode.setIcon(freq_icon);
					
					if (freqList.contains(f.getFrequencyuid())) {
						childNode.setDisabled(true);
					}
					
					childList.add(childNode);
				}
				
				node.setChildren(childList);
				nodeList.add(node);
			}
			
			List<Frequency> fList = this.service.getAllWithoutInCategory();
			for (Frequency f : fList) {
				JsTreeItem node = new JsTreeItem();
				node.setId(f.getFrequencyuid());
				node.setName(f.getFrequencyname());
				node.setIcon(freq_icon);
				
				if (freqList.contains(f.getFrequencyuid())) {
					node.setDisabled(true);;
				}
				
				nodeList.add(node);
			}
			
			return ResponseEntity.ok(nodeList);
		} catch (IllegalArgumentException e) {
			FrequencyController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
