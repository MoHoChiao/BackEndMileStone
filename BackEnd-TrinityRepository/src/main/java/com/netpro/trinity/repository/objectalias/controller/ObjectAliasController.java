package com.netpro.trinity.repository.objectalias.controller;

import java.util.List;

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

import com.netpro.trinity.repository.objectalias.entity.ObjectAlias;
import com.netpro.trinity.repository.objectalias.service.ObjectAliasService;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/objectalias")
public class ObjectAliasController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectAliasController.class);
		
	@Autowired
	private ObjectAliasService service;
	
	@GetMapping("/findExtraByParentUid")
	public ResponseEntity<?> findAliasExtraInfoByParentUid(String parentUid) {
		try {
			return ResponseEntity.ok(this.service.getExByParentUid(parentUid));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findByParentUid")
	public ResponseEntity<?> findAliasByParentUid(String parentUid) {
		try {
			return ResponseEntity.ok(this.service.getByParentUid(parentUid));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addOne")
	public ResponseEntity<?> addOneObjectAlias(@RequestBody ObjectAlias list) {
		try {
			return ResponseEntity.ok(this.service.add(list));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/addMany")
	public ResponseEntity<?> addManyObjectAlias(String parentUid, @RequestBody List<ObjectAlias> lists) {
		try {
			return ResponseEntity.ok(this.service.add(parentUid, lists));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/updateOne")
	public ResponseEntity<?> updateOneObjectAlias(@RequestBody ObjectAlias list) {
		try {
			return ResponseEntity.ok(this.service.update(list));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/modify")
	public ResponseEntity<?> modifyObjectAlias(String parentUid, @RequestBody List<ObjectAlias> lists) {
		try {
			return ResponseEntity.ok(this.service.modify(parentUid, lists));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/deleteByParentUid")
	public ResponseEntity<?> deleteAliasByParentUid(String parentUid) {
		try {
			this.service.deleteByParentUid(parentUid);
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(parentUid);
	}
	
	@GetMapping("/deleteByObjectUid")
	public ResponseEntity<?> deleteAliasByObjectUid(String objectUid) {
		try {
			this.service.deleteByObjectUid(objectUid);
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(objectUid);
	}
	
	@GetMapping("/deleteByPKs")
	public ResponseEntity<?> deleteAliasByPKs(String parentUid, String aliasName) {
		try {
			this.service.deleteByPKs(parentUid, aliasName);
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.ok(parentUid);
	}
	
	@GetMapping("/isExistByObjectuid")
	public ResponseEntity<?> isAliasExistByObjectUid(String objectUid) {
		try {
			return ResponseEntity.ok(this.service.existByObjectuid(objectUid));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isExistByPKs")
	public ResponseEntity<?> isAliasExistByPKs(String parentUid, String aliasName) {
		try {
			return ResponseEntity.ok(this.service.existByPKs(parentUid, aliasName));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/isObjectExistByObjectUidAndType")
	public ResponseEntity<?> isObjectExistByObjectUidAndType(String objectUid, String aliasType) {
		try {
			return ResponseEntity.ok(this.service.existByObjectUidAndType(objectUid, aliasType));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/findObjectNameByObjectUidAndType")
	public ResponseEntity<?> findObjectNameByObjectUidAndType(String objectUid, String aliasType) {
		try {
			return ResponseEntity.ok(this.service.getObjectNameByObjectUidAndType(objectUid, aliasType));
		}catch(IllegalArgumentException e) {
			ObjectAliasController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}catch(Exception e) {
			ObjectAliasController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
