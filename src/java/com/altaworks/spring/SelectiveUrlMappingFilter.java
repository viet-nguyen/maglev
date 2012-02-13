package com.altaworks.spring;

import com.altaworks.magnolia.UrlMappingFilterVoter;
import org.codehaus.groovy.grails.web.mapping.UrlMappingInfo;
import org.codehaus.groovy.grails.web.mapping.UrlMappingsHolder;
import org.codehaus.groovy.grails.web.mapping.filter.UrlMappingsFilter;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Åke Argéus
 */
public class SelectiveUrlMappingFilter extends UrlMappingsFilter {

	private UrlPathHelper urlHelper = new UrlPathHelper();

	private final String[] MAGNOLIA_URLS = {"/.magnolia/", "/.magnolia/**", "/.resources", "/.resources/**"};
	private final AntPathMatcher matcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		UrlMappingsHolder holder = WebUtils.lookupUrlMappings(getServletContext());
		String uri = urlHelper.getPathWithinApplication(request);
		if (!isUriExcluded(holder, uri) && !matchesMagnoliaUrls(uri)) {
			UrlMappingInfo match = holder.match(uri);
			if (match != null) {
				try {
					UrlMappingFilterVoter.bypass();
					super.doFilterInternal(request, response, filterChain);
				} finally {
					UrlMappingFilterVoter.remove();
				}
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private boolean matchesMagnoliaUrls(String uri) {
		for (String s : MAGNOLIA_URLS) {
			if (matcher.match(s, uri))
				return true;
		}
		return false;
	}
}
