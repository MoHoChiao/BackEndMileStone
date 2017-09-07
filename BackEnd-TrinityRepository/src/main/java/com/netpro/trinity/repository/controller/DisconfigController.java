package com.netpro.trinity.repository.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netpro.trinity.repository.dao.DisconfigDao;
import com.netpro.trinity.repository.entity.Disconfig_Entity;
import com.netpro.trinity.repository.entity.Disconfig_PKs;

@RestController  //宣告一個Restful Web Service的Resource
@RequestMapping("/disconfig")
public class DisconfigController {
	@Autowired  //自動注入DisconfigDao物件
	private DisconfigDao dao;

	@GetMapping("/findById")
	public Disconfig_Entity findById(Disconfig_Entity dto) {
		Disconfig_PKs pks = new Disconfig_PKs(dto.getModule(), dto.getConfigname());
		Disconfig_Entity findOne = this.dao.findOne(pks);
		return findOne;
	}
  
	@GetMapping("/findServicePosition")
	public List<Disconfig_Entity> findUiapPosition(String module1, String module2, String configname1, String configname2) {
		List<Disconfig_Entity> uiapPos = this.dao.findUiapPosition(module1, module2, configname1, configname2);
		return uiapPos;
	}
	
//  @GetMapping("/findByIdAndName") //對應client端的一個Http Get請求
//  public Disconfig_Entity findByIdAndName(Disconfig_Entity entity) {
//	  Disconfig_Entity value = this.userRepository.findByIdAndName(user.getId(), user.getName());  //使用JPA,根據Id及Name找到一筆資料
//	  return value;
//  }
//  
//  @GetMapping("/findByUsernameAndName") //對應client端的一個Http Get請求
//  public List<User> findByUsernameAndName(User user) {
//	  List<User> values = this.userRepository.withUsernameAndNameQuery(user.getUsername(), user.getName());  //使用JPA,根據Username及Name找到資料
//	  return values;
//  }
//
//  @PostMapping("/saveUser") //對應client端的一個Http Post請求
//  public User saveUser(@RequestBody User user) {
//	  User value = this.userRepository.save(user);
//    return value;
//  }
//  
//  @GetMapping("/delete/{id}") //對應client端的一個Http Get請求,其參數為id
//  public String delete(@PathVariable Long id) {
//	this.userRepository.delete(id);  //使用JPA,根據ID刪除一筆資料
//    return "Delete Success!";
//  }
}
