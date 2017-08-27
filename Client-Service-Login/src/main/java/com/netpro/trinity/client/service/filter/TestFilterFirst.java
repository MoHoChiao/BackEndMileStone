package com.netpro.trinity.client.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;

@Order(1)
//重点
@WebFilter(filterName = "testFilter1", urlPatterns = "/*")
public class TestFilterFirst implements Filter {
	private static final boolean CONDITION = false;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = asHttp(servletRequest);
		HttpServletResponse httpResponse = asHttp(servletResponse);
		
		String path = httpRequest.getRequestURI();
		if (path.startsWith("/authc/") || path.startsWith("/trinity-apps")) {
			filterChain.doFilter(servletRequest,servletResponse);
		} else {
			Cookie [] cookies = httpRequest.getCookies();
			if(cookies != null) {
				for (Cookie cookie : cookies) {
					System.out.println(cookie.getName()+":"+cookie.getValue());
				}
			}else {
				System.out.println("You do not have cookie!!!!");
				((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	@Override
	public void destroy() {
		
	}
	
	private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }
}