package com.netpro.trinity.auth.authz.feign;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netpro.trinity.auth.feign.util.CustomizedFeignClientConfiguration;

import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "trinity-service", configuration = CustomizedFeignClientConfiguration.class, fallbackFactory = RoleMemberClientFallback.class)
public interface RoleMemberClient {
	
	@RequestMapping(value = "/role-member/findRoleUidsByUserUid", method = RequestMethod.GET)
	public List<String> findRoleUidsByUserUid(@RequestParam("uid") String uid) throws Exception;
}

@Component
class RoleMemberClientFallback implements FallbackFactory<RoleMemberClient> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityuserClientFallback.class);
	@Override
	public RoleMemberClient create(Throwable cause) {
		return new RoleMemberClient() {
	    	
			@Override
			public List<String> findRoleUidsByUserUid(String uid)
					throws Exception {
				RoleMemberClientFallback.LOGGER.error("RoleMemberClient#findRoleUidsByUserUid fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
			}
	    };
	}
}
  
