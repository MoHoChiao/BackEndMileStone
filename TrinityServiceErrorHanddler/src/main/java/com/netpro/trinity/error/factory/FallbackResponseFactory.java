package com.netpro.trinity.error.factory;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.netflix.hystrix.exception.HystrixTimeoutException;

import feign.FeignException;

public class FallbackResponseFactory {
	public static ResponseEntity<?> getFallbackResponse(Throwable cause) {		
		HttpStatus httpcode = null;
		String message = null;
		if(cause instanceof FeignException) {
			FeignException feign_ex = (FeignException)cause;
    		httpcode = HttpStatus.valueOf(feign_ex.status());
    		message = feign_ex.getMessage();
		}else if (cause instanceof HystrixTimeoutException){
    		httpcode = HttpStatus.INTERNAL_SERVER_ERROR;
			message = format("status %s HystrixTimeoutException caused by backend service not found", 500);
			message += "; content:\n" + cause.getMessage();
		}else {
			httpcode = HttpStatus.INTERNAL_SERVER_ERROR;
			message = format("status %s Maybe %s", 500, "RutimeException");
			message += "; content:\n" + cause.getMessage();
		}
		return ResponseEntity.status(httpcode).body(message);
	}
}
