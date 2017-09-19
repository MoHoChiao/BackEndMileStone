package com.netpro.trinity.repository.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.dao.JCSAgentDao;
import com.netpro.trinity.service.util.entity.JCSAgent;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/jcsagent")
public class JCSAgentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JCSAgentController.class);
		
	@Autowired
	private JCSAgentDao dao;

	@GetMapping("/findAll")
	public List<JCSAgent> findAll() {
		try {
			return this.dao.findAll();
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			throw e;
		}
	}
  
	@GetMapping("/findById/{id}")
	public JCSAgent findById(@PathVariable String id) {
		try {
			return this.dao.findOne(id);
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			throw e;
		}
	}
  
	@GetMapping("/findByName/{name}")
	public List<JCSAgent> findByName(@PathVariable String name) {
		try {
			return this.dao.findByAgentname(name.toUpperCase());
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			throw e;
		}
	}
  
	@PostMapping("/save")
	public JCSAgent save(@RequestBody JCSAgent agent) {
		try {
			return this.dao.save(agent);
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			throw e;
		}
	}
//  
//  @GetMapping("/delete/{id}") //對應client端的一個Http Get請求,其參數為id
//  public String delete(@PathVariable Long id) {
//	this.userRepository.delete(id);  //使用JPA,根據ID刪除一筆資料
//    return "Delete Success!";
//  }
}
