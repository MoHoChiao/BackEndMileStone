package com.netpro.trinity.client.service;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
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
 * 宣告這是一個Feign Client,表示啟動Feign Client
 */
@EnableFeignClients
/*
 * 啟動Hystrix
 */
@EnableHystrix
/*
 * for filter
 */
public class ClientServiceLogin {
	private static final Logger LOG = LoggerFactory.getLogger(ClientServiceLogin.class);

	@Value("${spring.datasource.driver-class-name}")
	private String dbDriverClassName;

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;
	
	@Value("${spring.datasource.tomcat.max-active}")
	private Integer max_active;
	
	@Value("${spring.datasource.tomcat.initial-size}")
	private Integer initial_size;
	
	@Value("${spring.datasource.tomcat.max-wait}")
	private Integer max_wait;

	@Bean
	public DataSource dataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(dbDriverClassName);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);
		dataSource.setMaxActive(max_active);
		dataSource.setInitialSize(initial_size);
		dataSource.setMaxWait(max_wait);
		LOG.info("special for trinity apps dataSource url: " + dataSource.getUrl());
		return dataSource;
	}
	
//	@Bean
//	public EmbeddedServletContainerFactory servletContainerFactory() {
//	    return new TomcatEmbeddedServletContainerFactory() {
//
//	        @Override
//	        protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
//	                Tomcat tomcat) {
//	            // Ensure that the webapps directory exists
//	            new File(tomcat.getServer().getCatalinaBase(), "webapps").mkdirs();
//
//	            try {
//	            	System.setProperty("trinity.prop", "D:\\Trinity\\DISServer\\cfg\\trinity.properties");
//	            	System.setProperty("software.dir", "D:\\Trinity\\DISServer\\data\\software");
//	            	System.setProperty("DATABASE_DRIVER", dbDriverClassName);
//	            	System.setProperty("DATABASE_URL", dbUrl);
//	            	System.setProperty("DATABASE_USER", dbUsername);
//	            	System.setProperty("DATABASE_PASSWORD", dbPassword);
////	            	System.setProperty("com.netpro.dis.server.config.dir", "D:\\Trinity\\DISServer\\cfg");
////	            	System.setProperty("log.dir", "D:\\MyWork\\log");
//	            	
////	            	Context context0 = tomcat.addWebapp("/software", "D:\\MyWork\\micro_services\\com.netpro.trinity.home.war");
////	            	context0.setParentClassLoader(getClass().getClassLoader());
//	            	
//	                Context context1 = tomcat.addWebapp("/WebTaskConsole", "D:\\MyWork\\micro_services\\WebTaskConsole.war");
//	                context1.setParentClassLoader(getClass().getClassLoader());
//	                
//	                Context context2 = tomcat.addWebapp("/OperationLog", "D:\\MyWork\\micro_services\\OperationLog.war");
//	                context2.setParentClassLoader(getClass().getClassLoader());
//	                
//	                Context context3 = tomcat.addWebapp("/DisUI", "D:\\MyWork\\micro_services\\DisUI.war");
//	                context3.setParentClassLoader(getClass().getClassLoader());
//	            } catch (ServletException ex) {
//	                throw new IllegalStateException("Failed to add webapp", ex);
//	            }
//	            return super.getTomcatEmbeddedServletContainer(tomcat);
//	        }
//
//	    };
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(ClientServiceLogin.class, args);
	}
}
