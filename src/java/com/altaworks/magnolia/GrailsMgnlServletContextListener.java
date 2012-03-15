package com.altaworks.magnolia;

import com.google.inject.Stage;
import info.magnolia.cms.beans.config.ConfigLoader;
import info.magnolia.cms.core.SystemProperty;
import info.magnolia.init.MagnoliaConfigurationProperties;
import info.magnolia.init.MagnoliaInitPaths;
import info.magnolia.init.MagnoliaServletContextListener;
import info.magnolia.logging.Log4jConfigurer;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.objectfactory.configuration.ComponentConfiguration;
import info.magnolia.objectfactory.configuration.ComponentProviderConfiguration;
import info.magnolia.objectfactory.configuration.ComponentProviderConfigurationBuilder;
import info.magnolia.objectfactory.configuration.ImplementationConfiguration;
import info.magnolia.objectfactory.guice.GuiceComponentProvider;
import info.magnolia.objectfactory.guice.GuiceComponentProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;

/**
 * @author Kimmo Björnsson
 */
public class GrailsMgnlServletContextListener extends MagnoliaServletContextListener {

	private ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContext = sce.getServletContext();
		super.contextInitialized(sce);
	}

	@Override
	protected ComponentProviderConfiguration getPlatformComponents() {
		ComponentProviderConfiguration platformComponents = super.getPlatformComponents();
		platformComponents.registerInstance(ModuleDefinitionReader.class, new GrailsModuleDefinitionReader());
		return platformComponents;
	}


}
