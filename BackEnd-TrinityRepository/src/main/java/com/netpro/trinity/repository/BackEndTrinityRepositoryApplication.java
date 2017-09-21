package com.netpro.trinity.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/*
 * Spring Boot啟動的核心,它會開啟所有的自動配置以及載入相關的annotation(@Bean,@Entity...)進入Spring IOC Container
 * @SpringBootApplication為@Configuration,@EnableAutoConfiguration,@ComponentScan這些annotation的組合式annotation
 * @SpringBootApplication程式所在的package,其底下所有類別及子package底下所有類別都會被套用掃瞄,載入所有的@Bean
 */
@SpringBootApplication
/*
 * 宣告這是一個Eureka Client, 或可以寫@EnableDiscoveryClient, 在一定是Eureka Server的情況下兩者等價
 * @EnableEurekaClient表示就是Eureka Server的client而已
 * @EnableDiscoveryClient表示其它種微服務註冊/發現之server也能正常執行
 */
@EnableEurekaClient
/*
 * 掃瞄Entity Bean的Package及其下的Sub-Package
 */
//@EntityScan( basePackages = {"com.netpro.trinity.service.util.entity"} )
public class BackEndTrinityRepositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndTrinityRepositoryApplication.class, args);
	}
}
