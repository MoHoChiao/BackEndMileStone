package com.netpro.trinity.repository;

//import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
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
	
	@Bean  
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("300MB");
        factory.setMaxRequestSize("300MB");
//        if(mkFileTempDir("microservice-work/backend-trinity-repository/work/temp")) {
//        	factory.setLocation("microservice-work/backend-trinity-repository/work/temp");
//        }else {
//        	factory.setLocation("/");
//        }
//        if(mkFileTempDir("D:/microservice-work/backend-trinity-repository/work/temp")) {
//        	factory.setLocation("D:/microservice-work/backend-trinity-repository/work/temp");
//        }else {
//        	factory.setLocation("/");
//        }
        
        return factory.createMultipartConfig();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(BackEndTrinityRepositoryApplication.class, args);
	}
	
//	private boolean mkFileTempDir(String path) {
//		File f = new File(path);
//		if(f.exists())
//			return true;
//		
//		return f.mkdirs();
//	}
}
