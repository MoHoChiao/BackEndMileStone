package com.netpro.trinity.client.service.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netpro.trinity.error.config.SkipTrinityBadRequestsConfiguration;
import com.netpro.trinity.error.factory.FallbackResponseFactory;
import com.netpro.trinity.service.util.entity.dto.LoginInfo_Dto;

import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "back-service-authc", configuration = SkipTrinityBadRequestsConfiguration.class, fallbackFactory = AuthcFallbackFactory.class)
public interface AuthcFeign_old {
  @RequestMapping(value = "/authc-lib/gen-authc", method = RequestMethod.POST)
  public ResponseEntity<?> genAuthc(LoginInfo_Dto info);
  
  @RequestMapping(value = "/authc-lib/remove-authc", method = RequestMethod.GET)
  public ResponseEntity<?> removeAuthc();
  
  @RequestMapping(value = "/authc-lib/find-authc", method = RequestMethod.GET)
  public ResponseEntity<?> findAuthc();
}

/**
 * ConfigReaderFeign的fallbackFactory類別，該類別需實作FallbackFactory，且覆寫create方法
 * The fallback factory must produce instances of fallback classes that
 * implement the interface annotated by {@link FeignClient}.
 */
@Component
class AuthcFallbackFactory implements FallbackFactory<AuthcFeign_old> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthcFallbackFactory.class);
	@Override
	public AuthcFeign_old create(Throwable cause) {
		return new AuthcFeign_old() {
			String methodKey = "";
	    	
			@Override
	    	public ResponseEntity<?> genAuthc(LoginInfo_Dto info) {
	    		AuthcFallbackFactory.LOGGER.error("genAuthc fallback; reason was:", cause);
	    		methodKey = "AuthcFeign#genAuthc(LoginInfo)";
	    		return FallbackResponseFactory.getFallbackResponse(cause, methodKey);
	    	}

			@Override
			public ResponseEntity<?> removeAuthc() {
				AuthcFallbackFactory.LOGGER.error("removeAuthc fallback; reason was:", cause);
				methodKey = "AuthcFeign#removeAuthc()";
	    		return FallbackResponseFactory.getFallbackResponse(cause, methodKey);
			}

			@Override
			public ResponseEntity<?> findAuthc() {
				AuthcFallbackFactory.LOGGER.error("findAuthc fallback; reason was:", cause);
				methodKey = "AuthcFeign#findAuthc()";
	    		return FallbackResponseFactory.getFallbackResponse(cause, methodKey);
			}
	    };
	}
}
