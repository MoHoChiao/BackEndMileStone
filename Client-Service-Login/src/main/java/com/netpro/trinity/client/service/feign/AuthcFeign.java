package com.netpro.trinity.client.service.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netpro.trinity.entity.LoginInfo;
import com.netpro.trinity.status.TrinityServiceStatus;

import config.FeignLogConfiguration;
import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "back-service-authc", configuration = FeignLogConfiguration.class, fallbackFactory = AuthcFallbackFactory.class)
public interface AuthcFeign {
  @RequestMapping(value = "/authc-lib/find-access-token", method = RequestMethod.POST)
  public ResponseEntity<LoginInfo> findAccessToken(LoginInfo info);
}

/**
 * ConfigReaderFeign的fallbackFactory類別，該類別需實作FallbackFactory，且覆寫create方法
 * The fallback factory must produce instances of fallback classes that
 * implement the interface annotated by {@link FeignClient}.
 */
@Component
class AuthcFallbackFactory implements FallbackFactory<AuthcFeign> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthcFallbackFactory.class);

  @Override
  public AuthcFeign create(Throwable cause) {
    return new AuthcFeign() {

    	@Override
    	public ResponseEntity<LoginInfo> findAccessToken(LoginInfo info) {
    		AuthcFallbackFactory.LOGGER.error("fallback; reason was:", cause);
    		
    		info.setStatus(TrinityServiceStatus.ERROR);
    		info.setMsg(cause.getMessage());
    		return new ResponseEntity<LoginInfo>(info, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    };
  }
}
