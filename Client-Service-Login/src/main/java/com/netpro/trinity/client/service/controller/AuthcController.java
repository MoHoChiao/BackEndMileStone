package com.netpro.trinity.client.service.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.service.entity.LoginInfo;
import com.netpro.trinity.client.service.feign.AuthcFeign;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/authc")
public class AuthcController {
	
	@Autowired	//自動注入ConfigReaderFeign物件
	private AuthcFeign authc;
	
	@PostMapping("/gen-access-token")
	public LoginInfo getLogin(HttpServletRequest request, @RequestBody LoginInfo info) {
		info.setRemoteip(request.getRemoteAddr());
		info = this.authc.findLogin(info);
		return info;
	}
}
