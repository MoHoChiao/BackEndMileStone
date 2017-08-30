package com.netpro.trinity.client.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.service.feign.ConfigReaderFeign;
import com.netpro.trinity.error.exception.TrinityBadResponseWrapper;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-apps-model")
public class ConfigReaderController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigReaderController.class);
	
	@Autowired	//自動注入ConfigReaderFeign物件
	private ConfigReaderFeign configReader;
	
	@GetMapping("/get-apps-model")
	public ResponseEntity<?> getAppsModel() {
		try {
			return this.configReader.findAppsModel();
		}catch (TrinityBadResponseWrapper e) {
			ConfigReaderController.LOGGER.error("TrinityBadResponseWrapper; reason was:\n"+e.getBody());
			return ResponseEntity.status(e.getStatus()).body(e.getBody());
	    }
	}
}
