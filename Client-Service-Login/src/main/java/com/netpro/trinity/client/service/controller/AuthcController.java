package com.netpro.trinity.client.service.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.service.feign.AuthcFeign;
import com.netpro.trinity.service.entity.LoginInfo;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/authc")
public class AuthcController {
	
	@Autowired	//自動注入AuthcFeign物件
	private AuthcFeign authc;
	
	@PostMapping("/gen-access-token")
	public ResponseEntity<?> getLogin(HttpServletRequest request, @RequestBody LoginInfo info) {
		info.setRemoteip(request.getRemoteAddr());
		return this.authc.findAccessToken(info);
		
//		if(info.getStatus().equals(TrinityServiceStatus.SUCCESS))
//			return ResponseEntity.ok(info);
//		else if(info.getStatus().equals(TrinityServiceStatus.ERROR)) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(info);
//		}else if(info.getStatus().equals(TrinityServiceStatus.EMPTY)) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(info);
//		}else if(info.getStatus().equals(TrinityServiceStatus.CHANGE_CREDENTIALS)) {
//			return ResponseEntity.status(HttpStatus.).body(info);
//		}
//		return info;
	}
}
