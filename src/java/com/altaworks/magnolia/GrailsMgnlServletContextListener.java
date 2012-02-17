package com.altaworks.magnolia;

import info.magnolia.cms.beans.config.PropertiesInitializer;
import info.magnolia.cms.core.SystemProperty;
import info.magnolia.cms.servlets.MgnlServletContextListener;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;

/**
 * @author Kimmo Björnsson
 */
public class GrailsMgnlServletContextListener extends MgnlServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(MgnlServletContextListener.class);

    public void contextInitialized(final ServletContextEvent sce) {
        log.info("starting magnolia grails context");
        SystemProperty.setProperty(PropertiesInitializer.class.getName(), GrailsPropertiesInitializer.class.getName());

        //SystemProperty.setProperty(SystemProperty.MAGNOLIA_APP_ROOTDIR)
        super.contextInitialized(sce);
    }
}
