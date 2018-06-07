package com.netpro.trinity.zuul.server.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netpro.trinity.zuul.server.filter.lib.AuthcLib;

public class AuthenticationFilter extends ZuulFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";
	
	@Autowired
	private AuthcLib authcLib;
	
	@Override
	public boolean shouldFilter() {
//		RequestContext ctx = RequestContext.getCurrentContext();
	    // only forward to errorPath if it hasn't been forwarded to already
//	    return ctx.getThrowable() != null
//	            && !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false);
		
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest httpRequest = ctx.getRequest();
		
		String URI = httpRequest.getRequestURI();
		
		if(!URI.endsWith("authc-lib/gen-authc") && !URI.endsWith("authc-lib/find-authc")) {
			String authResult = authcLib.checkAuthc(httpRequest);
			if(!authResult.equals("Authentication Success.")) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				ctx.setResponseBody(authResult);
			}
		}
		
		String info = String.format("Send %s request to %s", httpRequest.getMethod(), httpRequest.getRequestURL().toString());
		AuthenticationFilter.LOGGER.info(info);
		return null;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
