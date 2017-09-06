package com.netpro.trinity.client.service.feign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.netpro.trinity.client.service.entity.User;
import com.netpro.trinity.error.config.SkipTrinityBadRequestsConfiguration;
import com.netpro.trinity.error.factory.FallbackResponseFactory;
import com.netpro.trinity.service.util.entity.dto.Disconfig_Dto;
import com.netpro.trinity.service.util.status.ExceptionMsgFormat;

import feign.FeignException;
import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "backend-trinity-repository", configuration = SkipTrinityBadRequestsConfiguration.class, fallbackFactory = DisconfigRepoFallback.class)
public interface DisconfigRepoClient {
	@RequestMapping(value = "/disconfig/findById", method = RequestMethod.GET)
	public Disconfig_Dto findById(@RequestParam("module") String module, @RequestParam("configname") String configname) throws Exception;
	
	@RequestMapping(value = "/disconfig/findUiapPosition", method = RequestMethod.GET)
	public List<Disconfig_Dto> findUiapPosition(@RequestParam("module1") String module1, 
			@RequestParam("module2") String module2, @RequestParam("configname1") String configname1, 
			@RequestParam("configname2") String configname2) throws Exception;
  
//  @RequestMapping(value = "/findByIdAndName", method = RequestMethod.GET) //簡單的Spring MVC的東西,等價於@GetMapping(value = "/findByIdAndName")
//  public User findByIdAndName(@RequestParam("id") Long id, @RequestParam("name") String name);
//
//  @RequestMapping(value = "/findByUsernameAndName", method = RequestMethod.GET) //簡單的Spring MVC的東西,等價於@GetMapping(value = "/findByUsernameAndName")
//  public List<User> findByUsernameAndName(@RequestParam Map<String, Object> map);
//
//  @RequestMapping(value = "/saveUser", method = RequestMethod.POST) //簡單的Spring MVC的東西,等價於@PostMapping(value = "/saveUser")
//  public User saveUser(@RequestBody User user);
//  
//  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)	//簡單的Spring MVC的東西,等價於@GetMapping(value = "/delete/{id}")
//  public String deleteUser(@PathVariable("id") Long id);
}

@Component
class DisconfigRepoFallback implements FallbackFactory<DisconfigRepoClient> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DisconfigRepoFallback.class);
	@Override
	public DisconfigRepoClient create(Throwable cause) {
		return new DisconfigRepoClient() {
			String methodKey = "DisconfigRepoClient";
	    	
			@Override
	    	public Disconfig_Dto findById(@RequestParam("module") String module, @RequestParam("configname") String configname) throws Exception {
				DisconfigRepoFallback.LOGGER.error("findById fallback; reason was:", cause);
	    		methodKey += "#findById(...)";
	    		
	    		String message = null;
	    		if(cause instanceof FeignException) {
	    			FeignException feign_ex = (FeignException)cause;
	        		message = feign_ex.getMessage();
	    		}else if (cause instanceof HystrixTimeoutException){
	        		message = ExceptionMsgFormat.get(500, methodKey + " HystrixTimeoutException", cause.getMessage());
	    		}else {
	    			message = ExceptionMsgFormat.get(500, methodKey + " RutimeException", cause.getMessage());
	    		}
	    		throw new Exception(message);
	    	}

			@Override
			public List<Disconfig_Dto> findUiapPosition(String module1, String module2, String configname1, String configname2)
					throws Exception {
				DisconfigRepoFallback.LOGGER.error("findUiapPosition fallback; reason was:", cause);
	    		methodKey += "#findUiapPosition(...)";
	    		
	    		String message = null;
	    		if(cause instanceof FeignException) {
	    			FeignException feign_ex = (FeignException)cause;
	        		message = feign_ex.getMessage();
	    		}else if (cause instanceof HystrixTimeoutException){
	        		message = ExceptionMsgFormat.get(500, methodKey + " HystrixTimeoutException", cause.getMessage());
	    		}else {
	    			message = ExceptionMsgFormat.get(500, methodKey + " RutimeException", cause.getMessage());
	    		}
	    		throw new Exception(message);
			}
	    };
	}
}
  
