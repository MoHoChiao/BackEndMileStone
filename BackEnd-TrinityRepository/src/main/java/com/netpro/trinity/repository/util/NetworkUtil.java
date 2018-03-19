package com.netpro.trinity.repository.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtil.class);
	
	public static String getRemoteIP(HttpServletRequest request) {
		String ip="";
		if(request.getRemoteAddr().equals("0:0:0:0:0:0:0:1") || request.getRemoteAddr().equals("127.0.0.1")) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			}catch(UnknownHostException e) {
				NetworkUtil.LOGGER.error("Exception; reason was:", e);
			}
		}else {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
