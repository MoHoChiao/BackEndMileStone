package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

@Configuration
public class FeignLogConfiguration {
  @Bean
  Logger.Level feignLoggerLevel() {
	  //return Logger.Level.NONE;	//此為默認值,不需要寫這支配置類別來特別設定
	  //return Logger.Level.BASIC;	//表示只顯示基本資訊
	  //return Logger.Level.HEADERS;//可顯示額外的header資訊
	  return Logger.Level.FULL;	//顯示完整資訊
  }
}
