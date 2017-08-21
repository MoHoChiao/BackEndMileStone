package com.netpro.trinity.authc.service.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.authc.service.authc_lib.AuthcLib;
import com.netpro.trinity.authc.service.entity.LoginInfo;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/authc-lib")
public class AuthcLibController {
	
	@Autowired	//自動注入DataSource物件
	private AuthcLib authcLib;
	
	@PostMapping("/find-access-token")
	public LoginInfo findLogin(HttpServletResponse response, @RequestBody LoginInfo info) {
		return authcLib.authcLogin(response, info);
	}
}
