package com.netpro.trinity.repository.controller.objectalias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.service.objectalias.ObjectAliasService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/objectalias")
public class ObjectAliasController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectAliasController.class);
		
	@Autowired
	private ObjectAliasService service;
	
	@GetMapping("/isExistByObjectuid")
	public ResponseEntity<?> isJobExistByFilesourceuid(String objectuid) {
		try {
			return ResponseEntity.ok(this.service.existByObjectuid(objectuid));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
