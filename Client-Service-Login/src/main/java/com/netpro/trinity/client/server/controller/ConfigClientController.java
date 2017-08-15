package com.netpro.trinity.client.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClientController {
	@Value("${eureka.instance.metadata-map.my-metadata1}")
    private String metadata1;
	
    @RequestMapping("/metadata1")
    String getMessage() {
        return this.metadata1;
    }
}
