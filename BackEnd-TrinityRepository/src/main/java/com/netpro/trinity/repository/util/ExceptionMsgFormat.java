package com.netpro.trinity.repository.util;

import static java.lang.String.format;

public class ExceptionMsgFormat {
	public static String get(Object status, Object methodKey, String msg) {
		String message = format("status %s reading %s", status, methodKey);
    	if (msg != null) {
            message += "; content:\n" + msg;
        }
    	return message;
	}
}
