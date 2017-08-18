package com.netpro.trinity.client.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.service.entity.LoginInfo;
import com.netpro.trinity.client.service.feign.AuthcFeign;
import com.netpro.trinity.client.service.util.LoginFlag;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/authc")
public class AuthcController {
	
	@Autowired	//自動注入ConfigReaderFeign物件
	private AuthcFeign authc;
	
	@PostMapping("/get-login")
	public LoginInfo getLogin(HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute LoginInfo info) {
		info.setRemoteip(request.getRemoteAddr());
		
		info = this.authc.findLogin(info);
		System.out.println(response.getHeader("Set-Cookie"));
		if(info.getFlag().equals(LoginFlag.CHANGE_CREDENTIALS)) {
			System.out.println(response.getContentType());
		}else if(info.getFlag().equals(LoginFlag.WARN)) {
			System.out.println(response.getContentType());
		}else if(info.getFlag().equals(LoginFlag.SUCCESS)) {
			System.out.println(response.getContentType());
		}
		
		return info;
	}
}
