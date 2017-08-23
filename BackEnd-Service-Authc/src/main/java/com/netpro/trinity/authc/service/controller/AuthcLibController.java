package com.netpro.trinity.authc.service.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.authc.service.authc_lib.AuthcLib;
import com.netpro.trinity.service.util.entity.dto.LoginInfo;
import com.netpro.trinity.service.util.exception.AuthcException;
import com.netpro.trinity.service.util.exception.FieldEmptyException;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/authc-lib")
public class AuthcLibController {
	
	@Autowired	//自動注入AuthcLib物件
	private AuthcLib authcLib;
	
	@PostMapping("/find-access-token")
	public ResponseEntity<?> findLogin(HttpServletResponse response, @RequestBody LoginInfo info) {
		
		try {
			return ResponseEntity.ok(authcLib.authcLogin(response, info.getRemoteip(), info.getAccount(), info.getPsw()));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (FieldEmptyException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (AuthcException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
