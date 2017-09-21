package com.netpro.trinity.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.service.feign.JCSAgentRepoClient;
import com.netpro.trinity.service.util.entity.JCSAgent;
import com.netpro.trinity.service.util.error.exception.TrinityBadResponseWrapper;
import com.netpro.trinity.service.util.tool.ExceptionMsgFormat;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/agent")
public class AgentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);
	
	@Autowired	//自動注入DisconfigRepoClient物件
	private JCSAgentRepoClient repo;
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll() {
		String methodKey = "AgentController#findAll(...)";
		try {
			return ResponseEntity.ok(this.repo.findAllAgent());
		} catch (Exception e) {
			AgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
  
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		String methodKey = "AgentController#findById(...)";
		try {
			return ResponseEntity.ok(this.repo.findAgentById(id));
		} catch (Exception e) {
			AgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
  
	@GetMapping("/findByName/{name}")
	public ResponseEntity<?> findByName(@PathVariable String name) {
		String methodKey = "AgentController#findByName(...)";
		try {
			return ResponseEntity.ok(this.repo.findAgentByName(name));
		} catch (Exception e) {
			AgentController.LOGGER.error("Exception; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
  
	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody JCSAgent agent) {
		String methodKey = "AgentController#save(...)";
		try {
			return ResponseEntity.ok(this.repo.saveAgent(agent));
		} catch (TrinityBadResponseWrapper e) {
			AgentController.LOGGER.error("TrinityBadResponseWrapper; reason was:\n"+e.getBody());
			return ResponseEntity.status(e.getStatus()).body(e.getBody());
		} catch (Exception e) {
			AgentController.LOGGER.error("RuntimeException; reason was:", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionMsgFormat.get(500, methodKey, e.getCause().toString()));
		}
	}
}
