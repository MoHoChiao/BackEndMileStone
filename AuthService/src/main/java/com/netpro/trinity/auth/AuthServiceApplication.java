package com.netpro.trinity.auth;

import org.apache.tomcat.jdbc.pool.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.netpro.trinity.auth.util.Crypto;

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
public class AuthServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(AuthServiceApplication.class);

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

	@Value("${trinity-prop-setting.encrypt.key}")
	private String encryptKey;
	
	@Bean
	public DataSource dataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(dbDriverClassName);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(Crypto.decryptPassword(dbPassword, encryptKey));
		dataSource.setMaxActive(max_active);
		dataSource.setInitialSize(initial_size);
		dataSource.setMaxWait(max_wait);
		LOG.info("special for trinity apps dataSource url: " + dataSource.getUrl());
		return dataSource;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}
