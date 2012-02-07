package com.altaworks.magnolia;

import info.magnolia.module.InstallContextImpl;
import info.magnolia.module.model.reader.ModuleDefinitionReader;
import info.magnolia.objectfactory.Components;

/**
 * Created by IntelliJ IDEA.
 * User: kimmo
 * Date: 2011-07-09
 * Time: 21:28
 * To change this template use File | Settings | File Templates.
 */
public class GrailsModuleManager extends info.magnolia.module.ModuleManagerImpl {

    public GrailsModuleManager() {

        super(new InstallContextImpl(), Components.getSingleton(ModuleDefinitionReader.class));

    }

}
