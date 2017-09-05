package com.netpro.trinity.config.service.config.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="trinity-prop-setting")
public class TrinityPropSetting {

    private final Map<String, String> trinityprop = new HashMap<>();

    public Map<String, String> getTrinityprop() {
        return trinityprop;
    }
}