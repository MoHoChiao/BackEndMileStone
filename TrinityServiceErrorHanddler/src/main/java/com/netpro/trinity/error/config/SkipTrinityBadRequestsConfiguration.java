package com.netpro.trinity.error.config;

import static java.lang.String.format;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import com.netpro.trinity.error.exception.TrinityBadResponseWrapper;

import feign.Util;
import feign.codec.ErrorDecoder;
import feign.codec.ErrorDecoder.Default;

@Configuration
public class SkipTrinityBadRequestsConfiguration {
	private final ErrorDecoder defaultErrorDecoder = new Default();
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response.status() >= 400 && response.status() <= 499) {
            	String body = "Bad request";
                try {
                    body = Util.toString(response.body().asReader());
                } catch (Exception ignored) {}
                
                HttpHeaders httpHeaders = new HttpHeaders();
                response.headers().forEach((k, v) -> httpHeaders.add("feign-" + k, StringUtils.join(v,",")));
            	
            	String message = format("status %s reading %s", response.status(), methodKey);
            	if (response.body() != null) {
                    message += "; content:\n" + body;
                }
            	
                return new TrinityBadResponseWrapper(response.status(), httpHeaders, message);
            }else {
            	return defaultErrorDecoder.decode(methodKey, response);
            }
        };
    }
}