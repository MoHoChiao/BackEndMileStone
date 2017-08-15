package com.netpro.trinity.client.server.feign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netpro.trinity.client.server.entity.User;

import config.FeignLogConfiguration;
import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "backend-service-login", configuration = FeignLogConfiguration.class, fallbackFactory = FeignClientFallbackFactory.class)
public interface UserFeignClient {
  @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)	//簡單的Spring MVC的東西,等價於@GetMapping(value = "/findById/{id}")
  public User findById(@PathVariable("id") Long id);
  
  @RequestMapping(value = "/findByIdAndName", method = RequestMethod.GET) //簡單的Spring MVC的東西,等價於@GetMapping(value = "/findByIdAndName")
  public User findByIdAndName(@RequestParam("id") Long id, @RequestParam("name") String name);

  @RequestMapping(value = "/findByUsernameAndName", method = RequestMethod.GET) //簡單的Spring MVC的東西,等價於@GetMapping(value = "/findByUsernameAndName")
  public List<User> findByUsernameAndName(@RequestParam Map<String, Object> map);

  @RequestMapping(value = "/saveUser", method = RequestMethod.POST) //簡單的Spring MVC的東西,等價於@PostMapping(value = "/saveUser")
  public User saveUser(@RequestBody User user);
  
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)	//簡單的Spring MVC的東西,等價於@GetMapping(value = "/delete/{id}")
  public String deleteUser(@PathVariable("id") Long id);
}

/**
 * UserFeignClient的fallbackFactory类，该类需实现FallbackFactory接口，并覆写create方法
 * The fallback factory must produce instances of fallback classes that
 * implement the interface annotated by {@link FeignClient}.
 * @author 周立
 */
@Component
class FeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
  private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientFallbackFactory.class);

  @Override
  public UserFeignClient create(Throwable cause) {
    return new UserFeignClient() {
      @Override
      public User findById(Long id) {
        // 日誌最好放在各個fallback方法中，而不要直接放在create方法中
        // 否則在引用啟動時，就會打印該日誌
        FeignClientFallbackFactory.LOGGER.info("fallback; reason was:", cause);
        User user = new User();
        user.setId(-1L);
        user.setUsername("findById fall back.");
        return user;
      }

	@Override
	public User findByIdAndName(Long id, String name) {
		 FeignClientFallbackFactory.LOGGER.info("fallback; reason was:", cause);
	     User user = new User();
	     user.setId(-1L);
	     user.setUsername("findByIdAndName fall back.");
	     return user;
	}

	@Override
	public List<User> findByUsernameAndName(Map<String, Object> map) {
		FeignClientFallbackFactory.LOGGER.info("fallback; reason was:", cause);
	    User user = new User();
	    user.setId(-1L);
	    user.setUsername("findByUsernameAndName fall back.");
	    List<User> users = new ArrayList<User>();
	    users.add(user);
	    return users;
	}

	@Override
	public User saveUser(User user) {
		FeignClientFallbackFactory.LOGGER.info("fallback; reason was:", cause);
		user.setId(-1L);
		user.setUsername("saveUser fall back.");
		return user;
	}

	@Override
	public String deleteUser(Long id) {
		// TODO Auto-generated method stub
		return "deleteUser fall back.";
	}
    };
  }
}
