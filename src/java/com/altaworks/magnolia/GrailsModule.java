package com.altaworks.magnolia;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;
import info.magnolia.module.blossom.module.BlossomModuleSupport;
import org.codehaus.groovy.grails.web.context.GrailsContextLoader;

/**
 * Module class that starts and stops Spring when called by Magnolia.
 */
public class GrailsModule extends BlossomModuleSupport implements ModuleLifecycle {

	public static GrailsBlossomDispatcherServlet grailsBlossomDispatcherServlet;

	public void start(ModuleLifecycleContext moduleLifecycleContext) {
		initRootWebApplicationContext(new GrailsContextLoader());
		grailsBlossomDispatcherServlet = new GrailsBlossomDispatcherServlet();
		initDispatcherServlet(grailsBlossomDispatcherServlet, "blossom", "classpath:/blossom-servlet.xml");
	}

	public void stop(ModuleLifecycleContext moduleLifecycleContext) {
		destroyDispatcherServlets();
		closeRootWebApplicationContext();
	}

}
