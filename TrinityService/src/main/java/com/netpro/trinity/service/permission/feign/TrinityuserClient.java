//package com.netpro.trinity.service.permission.feign;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.netpro.trinity.auth.authz.dto.Trinityuser;
//import com.netpro.trinity.auth.feign.util.CustomizedFeignClientConfiguration;
//
//import feign.hystrix.FallbackFactory;
///*
// * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
// * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
// * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
// */
//@FeignClient(name = "backend-trinity-repository", configuration = CustomizedFeignClientConfiguration.class, fallbackFactory = TrinityuserClientFallback.class)
//public interface TrinityuserClient {
//	
//	@RequestMapping(value = "/trinity-user/findByID", method = RequestMethod.GET)
//	public Trinityuser findUsersByID(@RequestParam("id") String id) throws Exception;
//}
//
//@Component
//class TrinityuserClientFallback implements FallbackFactory<TrinityuserClient> {
//	private static final Logger LOGGER = LoggerFactory.getLogger(TrinityuserClientFallback.class);
//	@Override
//	public TrinityuserClient create(Throwable cause) {
//		return new TrinityuserClient() {
//	    	
//			@Override
//	    	public Trinityuser findUsersByID(@RequestParam("id") String id) throws Exception {
//				TrinityuserClientFallback.LOGGER.error("TrinityuserClient#findUsersByID fallback; reason was:", cause);
//				throw new Exception(cause.getMessage());
//	    	}
//	    };
//	}
//}
//  
