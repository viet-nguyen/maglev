package com.altaworks.spring;

import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class SmartDelegatingFilterProxy extends DelegatingFilterProxy {

	private boolean applicationContextAvailable = false;
	private final Object monitor = new Object();

	public SmartDelegatingFilterProxy() {
		setTargetFilterLifecycle(true);

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		if (isWebApplicationContextAvailable())
			super.doFilter(request, response, filterChain);
		else
			filterChain.doFilter(request, response);
	}

	private boolean isWebApplicationContextAvailable() {
		synchronized (monitor) {
			if (!applicationContextAvailable) {
				applicationContextAvailable = findWebApplicationContext() != null;
			}
			return applicationContextAvailable;
		}
	}
}
