package com.netpro.trinity.client.server.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.netpro.trinity.client.server.entity.User;
import com.netpro.trinity.client.server.feign.UserFeignClient;

@CrossOrigin
@RestController		//宣告一個Restful Web Service的Resource
public class TestRestController {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestRestController.class); //引入log4j
	
  @Autowired	//自動注入UserFeignClient物件
  private UserFeignClient userFeignClient;

  /**
   * 测试URL：http://localhost:8010/user/getById/1
   * @param Long id
   * @return User
   */
  @GetMapping("/user/getById/{id}")	//對應client端的一個Http Get請求,路徑為/user/getById,其路徑參數為id
  public User getById(@PathVariable Long id) {
    return this.userFeignClient.findById(id);	//使用Feign Client來建立一個http連線到provider的呼叫
  }
  
  /**
   * 测试URL：http://localhost:8010/user/getByIdAndName?id=1&name=LeoLiu
   * @param User user
   * @return User
   */
  @GetMapping("/user/getByIdAndName")	//對應client端的一個Http Get請求,路徑為/user/getByIdAndName,其GET請求參數為id,name
  public User getByIdAndName(User user) {
    return this.userFeignClient.findByIdAndName(user.getId(), user.getName());	//使用Feign Client來建立一個http連線到provider的呼叫
  }
  
  /**
   * 测试URL：http://localhost:8010/user/getByUsernameAndName?username=account1&name=LeoLiu
   * @param User user
   * @return User
   */
  @GetMapping("/user/getByUsernameAndName")	//對應client端的一個Http Get請求,路徑為/user/getByUsernameAndName,其GET請求參數為username,name
  public List<User> getByUsernameAndName(User user) {
	Map<String, Object> map = Maps.newHashMap();
	map.put("name", user.getName());
	map.put("username", user.getUsername());
    return this.userFeignClient.findByUsernameAndName(map);	//使用Feign Client來建立一個http連線到provider的呼叫
  }
  
  /**
   * 测试URL：http://localhost:8010/user/remove/1
   * @param Long id
   * @return User
   */
  @GetMapping("/user/remove/{id}")	//對應client端的一個Http Get請求,路徑為/user/remove,其路徑參數為id
  public String remove(@PathVariable Long id) {
    return this.userFeignClient.deleteUser(id);	//使用Feign Client來建立一個http連線到provider的呼叫
  }
  
  /**
   * 测试URL：http://localhost:8010/user/save
   * @param User user
   * @return User
   */
  @PostMapping("/user/save")	//對應client端的一個Http Post請求,路徑為/user/add
  public User add(@RequestBody User user) {
    return this.userFeignClient.saveUser(user);	//使用Feign Client來建立一個http連線到provider的呼叫
  }
  
  
  @Autowired	//自動注入DiscoveryClient物件
  private DiscoveryClient discoveryClient;
  
  /**
   * 利用DiscoveryClient找user-provider這個服務的資訊
   * @return user-provider這個服務的instance
   */
  @GetMapping("/user-instance")
  public List<ServiceInstance> showInfo() {
    return this.discoveryClient.getInstances("user-provider");
  }
  
  @Autowired	//自動注入LoadBalancerClient物件
  private LoadBalancerClient loadBalancerClient;
  
  /**
   * 利用LoadBalancerClient找user-provider這個服務的資訊
   */
  @GetMapping("/log-user-instance")
  public void logUserInstance() {
    ServiceInstance serviceInstance = this.loadBalancerClient.choose("user-provider");
    // 顯示這個呼叫,當前選擇的是provider的那一個instance
    TestRestController.LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
  }
}
