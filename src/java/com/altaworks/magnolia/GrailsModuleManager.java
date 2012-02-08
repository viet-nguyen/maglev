package com.altaworks.magnolia;

import info.magnolia.module.InstallContextImpl;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.objectfactory.Components;

/**
 *@author Kimmo Björnsson
 */
public class GrailsModuleManager extends info.magnolia.module.ModuleManagerImpl {

    public GrailsModuleManager() {

        super(new InstallContextImpl(), Components.getSingleton(ModuleDefinitionReader.class));

    }

}
