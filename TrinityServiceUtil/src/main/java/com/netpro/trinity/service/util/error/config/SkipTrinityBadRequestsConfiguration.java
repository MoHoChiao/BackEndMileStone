package com.netpro.trinity.service.util.error.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import com.netpro.trinity.service.util.error.exception.TrinityBadResponseWrapper;
import com.netpro.trinity.service.util.tool.ExceptionMsgFormat;

import feign.Util;
import feign.codec.ErrorDecoder;
import feign.codec.ErrorDecoder.Default;

@Configuration
public class SkipTrinityBadRequestsConfiguration {
	private final ErrorDecoder defaultErrorDecoder = new Default();
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response.status() >= 400 && response.status() <= 500) {
            	String body = "Bad request";
                try {
                    body = Util.toString(response.body().asReader());
                } catch (Exception ignored) {}
                
                HttpHeaders httpHeaders = new HttpHeaders();
                response.headers().forEach((k, v) -> httpHeaders.add("feign-" + k, StringUtils.join(v,",")));
            	
                String message = ExceptionMsgFormat.get(response.status(), methodKey, body);
            	
                return new TrinityBadResponseWrapper(response.status(), httpHeaders, message);
            }else {
            	return defaultErrorDecoder.decode(methodKey, response);
            }
        };
    }
}