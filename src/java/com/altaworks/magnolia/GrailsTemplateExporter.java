package com.altaworks.magnolia;

import info.magnolia.module.blossom.template.TemplateExporter;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.commons.DefaultGrailsControllerClass;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsClass;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Åke Argéus
 */
public class GrailsTemplateExporter extends TemplateExporter{

	@Autowired
	GrailsApplication grailsApplication;

	private static Logger logger = Logger.getLogger(GrailsTemplateExporter.class);

	public void scan() {
		GrailsClass[] allControllerClasses = grailsApplication.getArtefacts("Controller");
		for (GrailsClass controllerClass : allControllerClasses) {
			DefaultGrailsControllerClass controller = (DefaultGrailsControllerClass) controllerClass;
			String handlerPath = controller.getViewByName("");	 // We are assuming that the action to be used as handler will be the default action
			logger.info("Registering controller on handlerpath" + handlerPath);
			Object handler = grailsApplication.getMainContext().getBean(controllerClass.getFullName());
			super.postProcessHandler(handler, handlerPath);
		}
	}

	@Override
	protected void exportTemplates() {
		scan();
		super.exportTemplates();
	}

}
