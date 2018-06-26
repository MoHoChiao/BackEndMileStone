package com.netpro.trinity.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/*
 * Spring Boot啟動的核心,它會開啟所有的自動配置以及載入相關的annotation(@Bean,@Entity...)進入Spring IOC Container
 * @SpringBootApplication為@Configuration,@EnableAutoConfiguration,@ComponentScan這些annotation的組合式annotation
 * @SpringBootApplication程式所在的package,其底下所有類別及子package底下所有類別都會被套用掃瞄,載入所有的@Bean
 */
@SpringBootApplication
/*
 * 宣告這是一個Eureka Server
 */
@EnableEurekaServer
public class MicroserviceCloudServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCloudServerApplication.class, args);
	}
}
