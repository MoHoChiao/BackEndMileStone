package com.netpro.trinity.client.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.client.server.entity.App;
import com.netpro.trinity.client.server.feign.ConfigReaderFeign;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
@RequestMapping("/trinity-apps-model")
public class ConfigReaderController {
	
	@Autowired	//自動注入ConfigReaderFeign物件
	private ConfigReaderFeign configReader;
	
	@GetMapping("/get-apps-model")
	public List<App> getAppsModel() {
		return this.configReader.findAppsModel();
	}
}
