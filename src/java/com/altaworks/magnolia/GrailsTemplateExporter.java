package com.altaworks.magnolia;

import info.magnolia.module.blossom.annotation.Area;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.module.blossom.dispatcher.BlossomDispatcher;
import info.magnolia.module.blossom.dispatcher.BlossomDispatcherInitializedEvent;
import info.magnolia.module.blossom.template.*;
import info.magnolia.objectfactory.Components;
import info.magnolia.rendering.template.registry.TemplateDefinitionRegistry;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.commons.DefaultGrailsControllerClass;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

/**
 * @author Åke Argéus
 */
public class GrailsTemplateExporter extends TemplateExporter {

	@Autowired
	GrailsApplication grailsApplication;

	private DetectedHandlersMetaData detectedHandlers = new DetectedHandlersMetaData();

	private static Logger logger = Logger.getLogger(GrailsTemplateExporter.class);

	private BlossomDispatcher dispatcher;

	private TemplateDefinitionBuilder templateDefinitionBuilder = new TemplateDefinitionBuilder();

	public void setBlossomDispatcher(BlossomDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public static GrailsTemplateExporter instance;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof BlossomDispatcherInitializedEvent && event.getSource() == dispatcher) {
			instance = this;
			exportTemplates();
		}
	}

	public static void reload() {
		if (instance != null)
			instance.exportTemplates();
	}


	@Override
	protected void exportTemplates() {
		detectedHandlers = new DetectedHandlersMetaData();

		GrailsClass[] allControllerClasses = grailsApplication.getArtefacts("Controller");
		for (GrailsClass controllerClass : allControllerClasses) {
			addController(controllerClass);
		}

		for (HandlerMetaData template : detectedHandlers.getTemplates()) {
			BlossomTemplateDefinition definition = templateDefinitionBuilder.buildTemplateDefinition(dispatcher, detectedHandlers, template);
			Components.getComponent(TemplateDefinitionRegistry.class).register(new BlossomTemplateDefinitionProvider(definition));
			if (StringUtils.isEmpty(definition.getDialog())) {
				registerTemplateDialog(definition);
			}
			registerDialogFactories(definition);
			registerAreaDialogs(definition.getAreas().values());
		}
		// We're done with this structure so there's no need to keep it around
		detectedHandlers = null;
	}

	private void addController(GrailsClass controllerClass) {
		DefaultGrailsControllerClass controller = (DefaultGrailsControllerClass) controllerClass;
		String handlerPath = controller.getViewByName("");	 // We are assuming that the action to be used as handler will be the default action
		logger.info("Registering controller on handlerpath" + handlerPath);
		Object handler = grailsApplication.getMainContext().getBean(controllerClass.getFullName());

		if (handler.getClass().isAnnotationPresent(Area.class)) {
			detectedHandlers.addArea(new HandlerMetaData(handler, handlerPath));
		} else if (handler.getClass().isAnnotationPresent(Template.class)) {
			detectedHandlers.addTemplate(new HandlerMetaData(handler, handlerPath));
		}
	}

}
