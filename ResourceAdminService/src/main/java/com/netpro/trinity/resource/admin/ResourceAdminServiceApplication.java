package com.netpro.trinity.resource.admin;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.netpro.trinity.resource.admin.util.Crypto;

@SpringBootApplication
public class ResourceAdminServiceApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResourceAdminServiceApplication.class);

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

	@Value("${encrypt.key}")
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
	
	public static void main(String[] args) {
		SpringApplication.run(ResourceAdminServiceApplication.class, args);
	}
}
