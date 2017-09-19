package com.netpro.trinity.service.util.error.factory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.netpro.trinity.service.util.tool.ExceptionMsgFormat;

import feign.FeignException;

public class FallbackResponseFactory {
	public static ResponseEntity<?> getFallbackResponse(Throwable cause, String methodKey) {		
		HttpStatus httpcode = null;
		String message = null;
		if(cause instanceof FeignException) {
			FeignException feign_ex = (FeignException)cause;
    		httpcode = HttpStatus.valueOf(feign_ex.status());
    		message = feign_ex.getMessage();
		}else if (cause instanceof HystrixTimeoutException){
    		httpcode = HttpStatus.INTERNAL_SERVER_ERROR;
    		message = ExceptionMsgFormat.get(500, methodKey + " HystrixTimeoutException", cause.getMessage());
		}else {
			httpcode = HttpStatus.INTERNAL_SERVER_ERROR;
			message = ExceptionMsgFormat.get(500, methodKey + " RutimeException", cause.getMessage());
		}
		return ResponseEntity.status(httpcode).body(message);
	}
}
