package com.altaworks.magnolia;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.Task;

import java.util.List;

/**
 * VersionHandler for the module.
 */
public class GrailsModuleVersionHandler extends DefaultModuleVersionHandler {


	@Override
	protected List<Task> getBasicInstallTasks(InstallContext installContext) {
		List<Task> basicInstallTasks = super.getBasicInstallTasks(installContext);
		basicInstallTasks.add(new BootstrapGrailsResources());
		return basicInstallTasks;
	}

}
