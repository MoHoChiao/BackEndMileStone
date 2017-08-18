package com.netpro.trinity.config.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.config.service.config.entity.TrinityAppsConfig;
import com.netpro.trinity.config.service.config.entity.TrinityAppsConfig.App;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-apps-setting")
public class TrinityAppsModelController {
	
	@Autowired	//自動注入LoadBalancerClient物件
	private TrinityAppsConfig appsConfig;
	  
	@GetMapping("/find-apps-model")
	public List<App> findAppsModel() {
		System.out.println("getModel");
		return this.appsConfig.getApps();
	}
}
