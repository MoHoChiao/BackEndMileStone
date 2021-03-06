package com.netpro.trinity.resource.admin.authn.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.netpro.trinity.resource.admin.authn.dto.LoginInfo;
import com.netpro.trinity.resource.admin.authn.service.AuthnService;
import com.netpro.trinity.resource.admin.util.NetworkUtil;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/authentication")
public class AuthnController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthnController.class);
		
	@Autowired	//自動注入authnService物件
	private AuthnService authnService;
	
	@PostMapping("/gen-authn")
	public ResponseEntity<?> genAuthn(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginInfo info) {
		info.setRemoteip(NetworkUtil.getRemoteIP(request));
		try {
			return ResponseEntity.ok(authnService.genAuthn(response, info.getRemoteip(), info.getAccount(), info.getPsw()));
		} catch (SQLException e) {
			AuthnController.LOGGER.error("SQLException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			AuthnController.LOGGER.error("IllegalArgumentException; reason was:", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (IllegalAccessException e) {
			AuthnController.LOGGER.error("IllegalAccessException; reason was:", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			AuthnController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@GetMapping("/find-authn")
	public ResponseEntity<?> findAuthn(HttpServletRequest request) {
		return ResponseEntity.ok(authnService.findAuthn(request));
	}
	
	@GetMapping("/remove-authn")
	public ResponseEntity<?> removeAuthn(HttpServletResponse response) {
		return ResponseEntity.status(HttpStatus.OK).body(authnService.removeAuthn(response));
	}
	
	@PostMapping("/reset-authn")
	public ResponseEntity<?> resetAuthn(HttpServletRequest request, HttpServletResponse response, @RequestBody String psw) {
		try {
			return ResponseEntity.ok(authnService.resetAuthn(request, response, request.getRemoteAddr(), psw));
		} catch (SQLException e) {
			AuthnController.LOGGER.error("SQLException; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			AuthnController.LOGGER.error("IllegalArgumentException; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (IllegalAccessException e) {
			AuthnController.LOGGER.error("IllegalAccessException; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			AuthnController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
