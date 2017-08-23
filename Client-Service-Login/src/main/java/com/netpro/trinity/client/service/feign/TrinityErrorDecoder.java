package com.netpro.trinity.client.service.feign;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;

import com.netpro.trinity.service.util.exception.FieldEmptyException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class TrinityErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
    	if (response.status() >= 400 && response.status() <= 499) {
        	String body = "Bad request";
            try {
                body = IOUtils.toString(response.body().asReader());
            } catch (Exception ignored) {}
            System.out.println(body+"/////////////////////");
            HttpHeaders httpHeaders = new HttpHeaders();
            response.headers().forEach((k, v) -> httpHeaders.add("feign-" + k, StringUtils.join(v,",")));
            System.out.println(httpHeaders);
            return new FieldEmptyException("aaaaaaaaaaaaaa");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }

}