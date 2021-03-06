package com.netpro.trinity.gateway.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netpro.trinity.gateway.util.ACUtil;

public class AuthenticationFilter extends ZuulFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";
	
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
		if(!URI.endsWith("authentication/gen-authn") && !URI.endsWith("authentication/find-authn") && !URI.endsWith("trinity-prop-setting/find-all-apps")) {
			if(!ACUtil.checkAuthentication(httpRequest)) {
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				ctx.setResponseBody("Authentication Fail!");
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
