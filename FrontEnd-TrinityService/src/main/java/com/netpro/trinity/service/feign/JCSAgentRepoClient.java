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
import org.springframework.web.bind.annotation.RequestParam;

import com.netpro.trinity.service.util.entity.JCSAgent;
import com.netpro.trinity.service.util.error.config.SkipTrinityBadRequestsConfiguration;

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
	public JCSAgent findAgentById(@RequestParam("id") String id) throws Exception;
	
	@RequestMapping(value = "/jcsagent/findByName", method = RequestMethod.GET)
	public List<JCSAgent> findAgentByName(@RequestParam("name") String name) throws Exception;
	
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
				JCSAgentRepoFallback.LOGGER.error("JCSAgentRepoClient#findAllAgent fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
	    	}
			
			@Override
	    	public JCSAgent findAgentById(@PathVariable("id") String id) throws Exception {
				JCSAgentRepoFallback.LOGGER.error("JCSAgentRepoClient#findAgentById fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
	    	}

			@Override
	    	public List<JCSAgent> findAgentByName(@PathVariable("name") String name) throws Exception {
				JCSAgentRepoFallback.LOGGER.error("JCSAgentRepoClient#findAgentByName fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
	    	}

			@Override
			public JCSAgent saveAgent(JCSAgent agent) throws Exception {
				JCSAgentRepoFallback.LOGGER.error("JCSAgentRepoClient#saveAgent fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
			}
	    };
	}
}
  
