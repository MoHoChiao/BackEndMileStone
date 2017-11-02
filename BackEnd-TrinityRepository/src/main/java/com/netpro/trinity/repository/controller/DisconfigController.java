package com.netpro.trinity.repository.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.jpa.dao.DisconfigDao;
import com.netpro.trinity.repository.jpa.entity.Disconfig;
import com.netpro.trinity.repository.jpa.entity.DisconfigPKs;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/disconfig")
public class DisconfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisconfigController.class);
	
	@Autowired
	private DisconfigDao dao;

	@GetMapping("/findById")
	public Disconfig findById(DisconfigPKs pks) {
		try {
			return this.dao.findOne(pks);
		} catch (Exception e) {
			DisconfigController.LOGGER.error("Exception; reason was:", e);
			throw e;
		}
	}
  
	@GetMapping("/findServicePosition")
	public List<Disconfig> findUiapPosition(String module1, String module2, String configname1, String configname2) {
		try {
			return this.dao.findUiapPosition(module1, module2, configname1, configname2);
		} catch (Exception e) {
			DisconfigController.LOGGER.error("Exception; reason was:", e);
			throw e;
		}
	}
}
