package com.netpro.trinity.repository.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.dao.DisconfigDao;
import com.netpro.trinity.service.util.entity.Disconfig;
import com.netpro.trinity.service.util.entity.DisconfigPKs;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/disconfig")
public class DisconfigController {
	@Autowired  //自動注入DisconfigDao物件
	private DisconfigDao dao;

	@GetMapping("/findById")
	public Disconfig findById(DisconfigPKs pks) {
		Disconfig findOne = this.dao.findOne(pks);
		return findOne;
	}
  
	@GetMapping("/findServicePosition")
	public List<Disconfig> findUiapPosition(String module1, String module2, String configname1, String configname2) {
		List<Disconfig> uiapPos = this.dao.findUiapPosition(module1, module2, configname1, configname2);
		return uiapPos;
	}
}
