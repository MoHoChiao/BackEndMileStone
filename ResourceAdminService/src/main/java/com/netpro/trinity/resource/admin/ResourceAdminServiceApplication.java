package com.netpro.trinity.resource.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.netpro.trinity.resource.admin.util.Crypto;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
public class ResourceAdminServiceApplication extends SpringBootServletInitializer {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResourceAdminServiceApplication.class);

	// DISServer/cfg/trinity.properties
	@Value("${database.driver}")
	private String dbDriverClassName;
	@Value("${database.url}")
	private String dbUrl;
	@Value("${database.username}")
	private String dbUsername;
	@Value("${database.password}")
	private String dbPassword;
	@Value("${trinity.encrypt.key}")
	private String encryptKey;
	
	// application.yml
	@Value("${spring.datasource.tomcat.max-active}")
	private Integer max_active;
	@Value("${spring.datasource.tomcat.initial-size}")
	private Integer initial_size;
	@Value("${spring.datasource.tomcat.max-wait}")
	private Integer max_wait;
	
	@Bean
	public HikariDataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(dbDriverClassName);
//		dataSource.setUrl(dbUrl);	// tomcat-jdbc
		dataSource.setJdbcUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(Crypto.decryptPassword(dbPassword, encryptKey));
//		dataSource.setMaxActive(max_active);	// tomcat-jdbc
		dataSource.setMaximumPoolSize(max_active);
//		dataSource.setInitialSize(initial_size);	// tomcat-jdbc
//		dataSource.setMaxWait(max_wait);	// tomcat-jdbc
		dataSource.setConnectionTimeout(max_wait);
		LOG.info("special for trinity apps dataSource url: " + dataSource.getJdbcUrl());
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
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ResourceAdminServiceApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ResourceAdminServiceApplication.class, args);
	}
}
