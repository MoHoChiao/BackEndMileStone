package com.netpro.trinity.auth.util;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netpro.ac.TrinityPrincipal;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;

public class ACUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ACUtil.class);
	
	public static Boolean checkAuthentication(HttpServletRequest request) {
		try {
			String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
			return TrinityWebV2Utils.isValidAccessToken(accessToken);
		}catch(Exception e) {
			ACUtil.LOGGER.error("Authentication Exception; reason was:", e);
			return false;
		}
	}
	
	public static String getUserIdFromAC(HttpServletRequest request) {
		String userId = "";
		try {
			String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
			Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(accessToken);
			if(!"".equals(principal.getName()) && principal instanceof TrinityPrincipal)
				userId = principal.getName();
		}catch(Exception e) {
			ACUtil.LOGGER.error("getUserIdFromAC Exception; reason was:", e);
		}
		
		return userId;
	}
}
