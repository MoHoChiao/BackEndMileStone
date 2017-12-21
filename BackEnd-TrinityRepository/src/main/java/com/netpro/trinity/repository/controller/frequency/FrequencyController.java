package com.netpro.trinity.repository.controller.frequency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.service.frequency.FrequencyService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/frequency")
public class FrequencyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyController.class);
		
	@Autowired
	private FrequencyService service;
	
	@GetMapping("/isExistByConnectionuid")
	public ResponseEntity<?> isFrequencyExistByWCalendaruid(String wcalendaruid) {
		try {
			return ResponseEntity.ok(this.service.existByWCalendaruid(wcalendaruid));
		}catch(Exception e) {
			FrequencyController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
