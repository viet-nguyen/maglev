package com.altaworks.magnolia;

import info.magnolia.cms.beans.config.PropertiesInitializer;
import info.magnolia.cms.core.SystemProperty;
import info.magnolia.module.ModuleManager;
import info.magnolia.module.model.reader.ModuleDefinitionReader;

/**
 * Created by IntelliJ IDEA.
 * User: kimmo
 * Date: 2011-07-09
 * Time: 22:09
 * To change this template use File | Settings | File Templates.
 */
public class GrailsPropertiesInitializer extends PropertiesInitializer {

    @Override
    public void loadBeanProperties() {
        super.loadBeanProperties();
        SystemProperty.setProperty(ModuleDefinitionReader.class.getName(), GrailsModuleDefinitionReader.class.getName());
        SystemProperty.setProperty(ModuleManager.class.getName(), GrailsModuleManager.class.getName());
    }

}
