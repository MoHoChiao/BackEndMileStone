package com.netpro.trinity.zuul.server.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AuditingFilter extends ZuulFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditingFilter.class);
	
	protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
	    // only forward to errorPath if it hasn't been forwarded to already
	    return ctx.getThrowable() != null
	            && !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false);
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest httpRequest = ctx.getRequest();
		HttpServletResponse httpResponse = ctx.getResponse();
		if(httpRequest.getRequestURI().indexOf("/jcsagent/findAll") > -1) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
			ctx.setResponseBody("Fuck you out!");
		}
		
		String info = String.format("Send %s request to %s", httpRequest.getMethod(), httpRequest.getRequestURL().toString());
		System.out.println(info);
		AuditingFilter.LOGGER.info(info);
		return null;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "post";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
