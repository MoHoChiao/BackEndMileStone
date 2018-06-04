package com.netpro.trinity.repository.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TransactionFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
        System.out.println(
          "Starting a transaction for req : " +  
          req.getRequestURI());
  
        chain.doFilter(request, response);
        System.out.println(
          "Committing a transaction for req : " + 
          req.getRequestURI());
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
 
    // other methods 
}