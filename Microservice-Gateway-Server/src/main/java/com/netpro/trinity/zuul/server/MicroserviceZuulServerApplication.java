package com.netpro.trinity.zuul.server;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.netpro.trinity.zuul.server.filter.AuditingFilter;
import com.netpro.trinity.zuul.server.filter.AuthenticationFilter;

/*
 * Spring Boot啟動的核心,它會開啟所有的自動配置以及載入相關的annotation(@Bean,@Entity...)進入Spring IOC Container
 * @SpringBootApplication為@Configuration,@EnableAutoConfiguration,@ComponentScan這些annotation的組合式annotation
 * @SpringBootApplication程式所在的package,其底下所有類別及子package底下所有類別都會被套用掃瞄,載入所有的@Bean
 */
@SpringBootApplication
/*
 * 宣告這是一個Zuul Gateway Server
 */
@EnableZuulProxy
/*
 * 宣告這是一個Eureka Client, 或可以寫@EnableDiscoveryClient, 在一定是Eureka Server的情況下兩者等價
 * @EnableEurekaClient表示就是Eureka Server的client而已
 * @EnableDiscoveryClient表示其它種微服務註冊/發現之server也能正常執行
 */
@EnableEurekaClient
public class MicroserviceZuulServerApplication {
//	@Bean
//	public AuthenticationFilter authFilter() {
//		return new AuthenticationFilter();
//	}
//	
//	@Bean
//	public AuditingFilter auditingFilter() {
//		return new AuditingFilter();
//	}
	
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("OPTIONS");
	    config.addAllowedMethod("HEAD");
	    config.addAllowedMethod("GET");
	    config.addAllowedMethod("PUT");
	    config.addAllowedMethod("POST");
	    config.addAllowedMethod("DELETE");
	    config.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}
	
	@Bean  
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("300MB");
        factory.setMaxRequestSize("300MB");
//        if(mkFileTempDir("microservice-work/microservice-gateway-server/work/temp")) {
//        	factory.setLocation("microservice-work/microservice-gateway-server/work/temp");
//        }else {
//        	factory.setLocation("/");
//        }
//        if(mkFileTempDir("D:/microservice-work/microservice-gateway-server/work/temp")) {
//        	factory.setLocation("D:/microservice-work/microservice-gateway-server/work/temp");
//        }else {
//        	factory.setLocation("/");
//        }
        
        return factory.createMultipartConfig();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(MicroserviceZuulServerApplication.class, args);
	}
	
	private boolean mkFileTempDir(String path) {
		File f = new File(path);
		if(f.exists())
			return true;
		
		return f.mkdirs();
	}
}
