package com.netpro.trinity.repository.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.dao.JCSAgentDao;
import com.netpro.trinity.repository.entity.JCSAgent;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/jcsagent")
public class JCSAgentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JCSAgentController.class);
		
	@Autowired
	private JCSAgentDao dao;

	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(Integer number, Integer size, String order, String orderField) {
		try {
			PageRequest page = null;
			if(size != null) {
				page = new PageRequest(number, size,new Sort(new Order(Direction.ASC, "agentname")));
			}
			if(page == null)
				return ResponseEntity.ok(this.dao.findAll());
			else
				return ResponseEntity.ok(this.dao.findAll(page));
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findById")
	public ResponseEntity<?> findById(String id) {
		try {
			return ResponseEntity.ok(this.dao.findOne(id));
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@GetMapping("/findByName")
	public ResponseEntity<?> findByName(String name) {
		try {
			return ResponseEntity.ok(this.dao.findByAgentname(name.toUpperCase()));
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
  
	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody JCSAgent agent) {
		try {
			return ResponseEntity.ok(this.dao.save(agent));
		}catch(Exception e) {
			JCSAgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
//  
//  @GetMapping("/delete/{id}") //對應client端的一個Http Get請求,其參數為id
//  public String delete(@PathVariable Long id) {
//	this.userRepository.delete(id);  //使用JPA,根據ID刪除一筆資料
//    return "Delete Success!";
//  }
}
