package com.altaworks.magnolia;

import info.magnolia.init.MagnoliaServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kimmo Björnsson
 */
public class GrailsMgnlServletContextListener extends MagnoliaServletContextListener {

	public void contextInitialized(final ServletContextEvent sce) {
		//SystemProperty.setProperty(PropertiesInitializer.class.getName(), GrailsPropertiesInitializer.class.getName());

		//SystemProperty.setProperty(SystemProperty.MAGNOLIA_APP_ROOTDIR)
		super.contextInitialized(sce);
	}

	@Override
	protected List<String> getPlatformComponentsResources() {
		List<String> platformComponentsResources = super.getPlatformComponentsResources();
		List<String> components = new ArrayList<String>();
		components.addAll(platformComponentsResources);
		components.add("/platform-components.xml");
		return components;
	}
}
