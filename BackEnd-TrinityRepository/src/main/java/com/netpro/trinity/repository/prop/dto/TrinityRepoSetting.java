package com.netpro.trinity.repository.prop.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="spring")
public class TrinityRepoSetting
{
	private final Map<String, String> jpa = new HashMap<>();
	private final Map<String, String> datasource = new HashMap<>();

    public Map<String, String> getJpa() {
        return jpa;
    }
    
    public Map<String, String> getDatasource() {
        return datasource;
    }
}