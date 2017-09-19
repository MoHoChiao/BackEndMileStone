package com.netpro.trinity.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.service.dto.prop.TrinitySysSetting;
import com.netpro.trinity.service.util.tool.ExceptionMsgFormat;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-sys-setting")
public class TrinitySysController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinitySysController.class);
	
	private String methodKey = "TrinitySysController";
	
	@Autowired
	private TrinitySysSetting trinitySys;
	
	@GetMapping("/find-all-dirs")
	public ResponseEntity<?> findAllDirs() {
		methodKey += "#findAllDirs(...)";
		try {
			return ResponseEntity.ok(trinitySys.getDir());
		}catch (Exception e) {
			TrinitySysController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
	    }
	}
}
