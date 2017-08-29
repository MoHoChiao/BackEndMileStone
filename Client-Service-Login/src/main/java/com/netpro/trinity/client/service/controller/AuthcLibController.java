package com.netpro.trinity.client.service.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.service.lib.AuthcLib;
import com.netpro.trinity.service.util.entity.dto.LoginInfo;
import com.netpro.trinity.service.util.status.ExceptionMsgFormat;

@CrossOrigin
@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/authc-lib")
public class AuthcLibController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthcLibController.class);
	
	@Autowired	//自動注入AuthcLib物件
	private AuthcLib authcLib;
	
	@PostMapping("/gen-authc")
	public ResponseEntity<?> genAuthc(HttpServletResponse response, @RequestBody LoginInfo info) {
		String methodKey = "AuthcLibController#genAuthc()";
		try {
			return ResponseEntity.ok(authcLib.genAuthc(response, info.getRemoteip(), info.getAccount(), info.getPsw()));
		} catch (SQLException e) {
			AuthcLibController.LOGGER.error("SQLException; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
		} catch (IllegalArgumentException e) {
			AuthcLibController.LOGGER.error("IllegalArgumentException; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionMsgFormat.get(400, methodKey, e.getMessage()));
		} catch (IllegalAccessException e) {
			AuthcLibController.LOGGER.error("IllegalAccessException; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionMsgFormat.get(400, methodKey, e.getMessage()));
		} catch (Exception e) {
			AuthcLibController.LOGGER.error("Exception; reason was:", e.getCause());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getMessage()));
		}
	}
	
	@GetMapping("/remove-authc")
	public ResponseEntity<?> removeAuthc(HttpServletResponse response) {
		return ResponseEntity.status(HttpStatus.OK).body(authcLib.removeAuthc(response));
	}
	
	@GetMapping("/find-authc")
	public ResponseEntity<?> findAuthc(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authcLib.findAuthc(request));
	}
}
