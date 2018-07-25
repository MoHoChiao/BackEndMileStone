package com.netpro.trinity.service.permission.feign;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.netpro.trinity.service.feign.util.CustomizedFeignClientConfiguration;
import com.netpro.trinity.service.permission.dto.AccessRight;

import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "auth-service", configuration = CustomizedFeignClientConfiguration.class, fallbackFactory = PermissionClientFallback.class)
public interface PermissionClient {
	
	@RequestMapping(value = "/authorization/modifyByPeopleUid", method = RequestMethod.POST)
	public List<AccessRight> modifyByPeopleUid(@RequestParam("peopleUid") String peopleUid, @RequestBody List<AccessRight> lists) throws Exception;
	
	@RequestMapping(value = "/authorization/modifyByObjectUid", method = RequestMethod.POST)
	public List<AccessRight> modifyByObjectUid(@RequestParam("objectUid") String objectUid, @RequestBody List<AccessRight> lists) throws Exception;
	
	@RequestMapping(value = "/authorization/deleteByPeopleUid", method = RequestMethod.GET)
	public void deleteByPeopleUid(@RequestParam("peopleUid") String peopleUid) throws Exception;
	
	@RequestMapping(value = "/authorization/deleteByObjectUid", method = RequestMethod.GET)
	public void deleteByObjectUid(@RequestParam("objectUid") String objectUid) throws Exception;
}

@Component
class PermissionClientFallback implements FallbackFactory<PermissionClient> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionClientFallback.class);
	@Override
	public PermissionClient create(Throwable cause) {
		return new PermissionClient() {
	    	
			@Override
			public List<AccessRight> modifyByPeopleUid(@RequestParam("peopleUid") String peopleUid, @RequestBody List<AccessRight> lists)
					throws Exception {
				PermissionClientFallback.LOGGER.error("PermissionClient#modifyByPeopleUid fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
			}

			@Override
			public List<AccessRight> modifyByObjectUid(@RequestParam("objectUid") String objectUid, @RequestBody List<AccessRight> lists)
					throws Exception {
				PermissionClientFallback.LOGGER.error("PermissionClient#modifyByObjectUid fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
			}

			@Override
			public void deleteByPeopleUid(@RequestParam("peopleUid") String peopleUid) throws Exception {
				PermissionClientFallback.LOGGER.error("PermissionClient#deleteByPeopleUid fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
			}

			@Override
			public void deleteByObjectUid(String objectUid) throws Exception {
				PermissionClientFallback.LOGGER.error("PermissionClient#deleteByObjectUid fallback; reason was:", cause);
				throw new Exception(cause.getMessage());
			}
	    };
	}
}
  