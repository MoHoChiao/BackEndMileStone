package com.netpro.trinity.service.feign;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.netpro.trinity.error.config.SkipTrinityBadRequestsConfiguration;
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
	
	@RequestMapping(value = "/disconfig/findServicePosition", method = RequestMethod.GET)
	public List<Disconfig_Dto> findServicePosition(@RequestParam("module1") String module1, 
			@RequestParam("module2") String module2, @RequestParam("configname1") String configname1, 
			@RequestParam("configname2") String configname2) throws Exception;
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
			public List<Disconfig_Dto> findServicePosition(String module1, String module2, String configname1, String configname2)
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
  
