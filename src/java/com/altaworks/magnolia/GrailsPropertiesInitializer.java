package com.altaworks.magnolia;

import groovy.util.ConfigObject;
import info.magnolia.cms.beans.config.PropertiesInitializer;
import info.magnolia.cms.core.SystemProperty;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

/**
 * @author Kimmo Björnsson
 * @author Åke Argéus
 */
public class GrailsPropertiesInitializer extends PropertiesInitializer {

	@Override
	public void loadBeanProperties() {
		super.loadBeanProperties();
		SystemProperty.setProperty(ModuleDefinitionReader.class.getName(), GrailsModuleDefinitionReader.class.getName());
		SystemProperty.setProperty(ModuleManager.class.getName(), GrailsModuleManager.class.getName());
	}

	@Override
	public void overloadWithSystemProperties() {
		super.overloadWithSystemProperties();
		ConfigObject config = ConfigurationHolder.getConfig();
		Properties properties = config.toProperties();
		for (Object o : properties.keySet()) {
			if (o instanceof String && properties.getProperty((String) o) instanceof String) {
				SystemProperty.setProperty((String) o, properties.getProperty((String) o));
			}
		}

	}
}
