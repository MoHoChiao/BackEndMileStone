package com.netpro.trinity.service.feign;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.netpro.trinity.service.util.entity.JCSAgent;
import com.netpro.trinity.service.util.error.config.SkipTrinityBadRequestsConfiguration;
import com.netpro.trinity.service.util.tool.ExceptionMsgFormat;

import feign.FeignException;
import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "backend-trinity-repository", configuration = SkipTrinityBadRequestsConfiguration.class, fallbackFactory = JCSAgentRepoFallback.class)
public interface JCSAgentRepoClient {
	@RequestMapping(value = "/jcsagent/findAll", method = RequestMethod.GET)
	public List<JCSAgent> findAllAgent() throws Exception;
	
	@RequestMapping(value = "/jcsagent/findById", method = RequestMethod.GET)
	public JCSAgent findAgentById(@PathVariable("id") String id) throws Exception;
	
	@RequestMapping(value = "/jcsagent/findByName", method = RequestMethod.GET)
	public List<JCSAgent> findAgentByName(@PathVariable("name") String name) throws Exception;
	
	@RequestMapping(value = "/jcsagent/save", method = RequestMethod.POST)
	public JCSAgent saveAgent(@RequestBody JCSAgent agent) throws Exception;
}

@Component
class JCSAgentRepoFallback implements FallbackFactory<JCSAgentRepoClient> {
	private static final Logger LOGGER = LoggerFactory.getLogger(JCSAgentRepoFallback.class);
	@Override
	public JCSAgentRepoClient create(Throwable cause) {
		return new JCSAgentRepoClient() {
	    	
			@Override
	    	public List<JCSAgent> findAllAgent() throws Exception {
				JCSAgentRepoFallback.LOGGER.error("findAll fallback; reason was:", cause);
				String methodKey = "JCSAgentRepoClient#findAll(...)";
	    		
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
	    	public JCSAgent findAgentById(@PathVariable("id") String id) throws Exception {
				JCSAgentRepoFallback.LOGGER.error("findById fallback; reason was:", cause);
				String methodKey = "JCSAgentRepoClient#findById(...)";
	    		
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
	    	public List<JCSAgent> findAgentByName(@PathVariable("name") String name) throws Exception {
				JCSAgentRepoFallback.LOGGER.error("findByName fallback; reason was:", cause);
				String methodKey = "JCSAgentRepoClient#findByName(...)";
	    		
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
			public JCSAgent saveAgent(JCSAgent agent) throws Exception {
				JCSAgentRepoFallback.LOGGER.error("save fallback; reason was:", cause);
				String methodKey = "JCSAgentRepoClient#save(...)";
	    		
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
  
