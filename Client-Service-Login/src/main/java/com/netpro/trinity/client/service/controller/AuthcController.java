package com.netpro.trinity.client.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.service.feign.AuthcFeign;
import com.netpro.trinity.error.exception.TrinityBadResponseWrapper;
import com.netpro.trinity.service.util.entity.dto.LoginInfo;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/authc")
public class AuthcController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthcController.class);
	
	@Autowired	//自動注入AuthcFeign物件
	private AuthcFeign authc;
	
	@PostMapping("/gen-token")
	public ResponseEntity<?> genToken(HttpServletRequest request, @RequestBody LoginInfo info) {
		info.setRemoteip(request.getRemoteAddr());
		try {
			return this.authc.genAuthc(info);
		}catch (TrinityBadResponseWrapper e) {
			AuthcController.LOGGER.error("TrinityBadResponseWrapper; reason was:\n"+e.getBody());
			return ResponseEntity.status(e.getStatus()).body(e.getBody());
	    }
	}
	
	@GetMapping("/remvoe-token")
	public ResponseEntity<?> removeAuthc(HttpServletResponse response) {
		return this.authc.removeAuthc();
	}
	
	@GetMapping("/find-token")
	public ResponseEntity<?> findAuthc(HttpServletRequest request) {
		return this.authc.findAuthc();
	}
}
