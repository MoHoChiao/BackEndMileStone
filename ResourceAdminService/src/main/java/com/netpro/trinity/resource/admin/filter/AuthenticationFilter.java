package com.netpro.trinity.resource.admin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.netpro.trinity.resource.admin.util.ACUtil;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {	
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
		if(URI.indexOf("/src/assets") == -1 && URI.indexOf("/dist/build") == -1 && !URI.endsWith("/") && !URI.endsWith("/index.html")
				&& !URI.endsWith("trinity-prop-setting/find-all-apps") && !URI.endsWith("authentication/gen-authn") && !URI.endsWith("authentication/find-authn")) {
			if(!ACUtil.checkAuthentication(httpRequest)) {
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
}