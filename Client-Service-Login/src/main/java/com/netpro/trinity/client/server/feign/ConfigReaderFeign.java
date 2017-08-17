package com.netpro.trinity.client.server.feign;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netpro.trinity.client.server.entity.App;

import config.FeignLogConfiguration;
import feign.hystrix.FallbackFactory;
/*
 * name:一個任意的客戶端服務名稱,即Eureka服務註冊列表中的服務
 * configuration:可設定Feign Client的配置類別,這個配置主要是控制logging的層級
 * fallbackFactory:這裡是Feign結合Hystrix的fall back function之配置
 */
@FeignClient(name = "back-service-config-reader", configuration = FeignLogConfiguration.class, fallbackFactory = ConfigReaderFallbackFactory.class)
public interface ConfigReaderFeign {
  @RequestMapping(value = "/trinity-apps-setting/find-apps-model", method = RequestMethod.GET)
  public List<App> findAppsModel();
}

/**
 * ConfigReaderFeign的fallbackFactory類別，該類別需實作FallbackFactory，且覆寫create方法
 * The fallback factory must produce instances of fallback classes that
 * implement the interface annotated by {@link FeignClient}.
 */
@Component
class ConfigReaderFallbackFactory implements FallbackFactory<ConfigReaderFeign> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigReaderFallbackFactory.class);

  @Override
  public ConfigReaderFeign create(Throwable cause) {
    return new ConfigReaderFeign() {

    	@Override
    	public List<App> findAppsModel() {
    		ConfigReaderFallbackFactory.LOGGER.error("fallback; reason was:", cause);
    		App app = new App();
    		app.setName("Error !");
    		app.setAlt("Error !");
    		app.setCls("Error !");
    		app.setImg("Error !");
    		app.setModel("Error !");
    		List<String> desc = new ArrayList<String>();
    		desc.add("Fall Back Function");
    		desc.add("Class: ConfigReaderFallbackFactory");
    		desc.add("Class: ConfigReaderFeign");
    		desc.add("Method: findAppsModel");
    		desc.add("Service: back-service-config-reader");
    		app.setDesc(desc);
    		List<App> apps = new ArrayList<App>();
    		apps.add(app);
    		return apps;
    	}
    };
  }
}
