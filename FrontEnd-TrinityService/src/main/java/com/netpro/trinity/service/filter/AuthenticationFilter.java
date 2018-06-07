package com.netpro.trinity.service.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.netpro.ac.TrinityPrincipal;
import com.netpro.ac.util.CookieUtils;
import com.netpro.ac.util.TrinityWebV2Utils;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String URI = httpRequest.getRequestURI();
		
		if(!URI.endsWith("authc-lib/gen-authc") && !URI.endsWith("authc-lib/find-authc") && !URI.endsWith("trinity-prop-setting/find-all-apps")) {
			String checkResult = this.checkAuthentication(httpRequest);
			if(!checkResult.equals("Authentication Success.")) {
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Get the fuck off my service!!");
			}else {
				chain.doFilter(request, response);
			}
		}else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
 
	private String checkAuthentication(HttpServletRequest request) {
		try {
			String accessToken = CookieUtils.getCookieValue(request, TrinityWebV2Utils.CNAME_ACCESS_TOKEN);
			Principal principal = TrinityWebV2Utils.doValidateAccessTokenAndReturnPrincipal(accessToken);
			if(!"".equals(principal.getName()) && principal instanceof TrinityPrincipal) {
				return "Authentication Success.";
			}else {
				return "Authentication Fail!";
			}
		}catch(Exception e) {
			AuthenticationFilter.LOGGER.error("Exception; reason was:", e);
			return "Authentication Error : " + e.getMessage();
		}
	}
}