package com.netpro.trinity.service.util.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class FieldEmptyException extends HystrixBadRequestException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FieldEmptyException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
