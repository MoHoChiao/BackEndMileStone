package com.netpro.trinity.service.feign.util;

import org.springframework.http.HttpHeaders;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class TrinityBadResponseWrapper extends HystrixBadRequestException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int status;
    private final HttpHeaders headers;
    private final String body;

    public TrinityBadResponseWrapper(int status, HttpHeaders headers, String body) {
        super("Bad request");
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

	public int getStatus() {
		return status;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}
}