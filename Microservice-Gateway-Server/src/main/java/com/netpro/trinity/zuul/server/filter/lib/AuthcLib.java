package com.netpro.trinity.zuul.server.filter.lib;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.netpro.ac.TrinityPrincipal;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;

@Service
public class AuthcLib {
	public String checkAuthc(HttpServletRequest request) {
		try {
			String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
			Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(accessToken);
			if(!"".equals(principal.getName()) && principal instanceof TrinityPrincipal) {
				return "Authentication Success.";
			}else {
				return "Authentication Fail!";
			}
		}catch(Exception e) {
			return "Authentication Error : " + e.getMessage();
		}
	}
}
