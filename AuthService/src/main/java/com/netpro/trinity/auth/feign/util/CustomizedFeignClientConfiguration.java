package com.netpro.trinity.auth.feign.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;

import feign.RequestInterceptor;
import feign.Util;
import feign.codec.ErrorDecoder;
import feign.codec.ErrorDecoder.Default;

@Configuration
public class CustomizedFeignClientConfiguration {
	private final ErrorDecoder defaultErrorDecoder = new Default();
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response.status() >= 400 && response.status() <= 500) {
            	System.out.println(response.status());
            	String body = "Bad request";
                try {
                    body = Util.toString(response.body().asReader());
                } catch (Exception ignored) {
                	ignored.printStackTrace();
                }
                
                HttpHeaders httpHeaders = new HttpHeaders();
                response.headers().forEach((k, v) -> httpHeaders.add("feign-" + k, StringUtils.join(v,",")));
            	
                String message = ExceptionMsgFormat.get(response.status(), methodKey, body);
            	
                return new TrinityBadResponseWrapper(response.status(), httpHeaders, message);
            }else {
            	return defaultErrorDecoder.decode(methodKey, response);
            }
        };
    }
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
        	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        	String accessToken = CookieUtils.getCookieValue(attributes.getRequest(), TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
        	requestTemplate.header("Cookie", "access_token=" + accessToken);
        };
    }
}