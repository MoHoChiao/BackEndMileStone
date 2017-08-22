package com.netpro.trinity.client.service.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.service.feign.AuthcFeign;
import com.netpro.trinity.service.entity.LoginInfo;
import com.netpro.trinity.service.status.TrinityServiceStatus;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/authc")
public class AuthcController {
	
	@Autowired	//自動注入AuthcFeign物件
	private AuthcFeign authc;
	
	@PostMapping("/gen-access-token")
	public ResponseEntity<LoginInfo> getLogin(HttpServletRequest request, @RequestBody LoginInfo info) {
		info.setRemoteip(request.getRemoteAddr());
		info = this.authc.findAccessToken(info).getBody();
		if(info.getStatus().equals(TrinityServiceStatus.SUCCESS))
			return new ResponseEntity<LoginInfo>(info, HttpStatus.OK);
		else if(info.getStatus().equals(TrinityServiceStatus.ERROR)) {
			return null;
		}else if(info.getStatus().equals(TrinityServiceStatus.SUCCESS)) {
			return null;
		}else if(info.getStatus().equals(TrinityServiceStatus.SUCCESS)) {
			return null;
		}
		return null;
	}
}
