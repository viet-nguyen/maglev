package com.altaworks.magnolia;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.blossom.BlossomModule;
import info.magnolia.module.blossom.annotation.DialogFactory;
import info.magnolia.module.blossom.annotation.Paragraph;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.module.blossom.context.MagnoliaLocaleResolver;
import info.magnolia.module.blossom.dialog.BlossomDialogRegistry;
import info.magnolia.module.blossom.dialog.DialogExporter;
import info.magnolia.module.blossom.dispatcher.BlossomDispatcher;
import info.magnolia.module.blossom.paragraph.BlossomParagraphRegistry;
import info.magnolia.module.blossom.paragraph.ParagraphDescriptionBuilder;
import info.magnolia.module.blossom.support.ForwardRequestWrapper;
import info.magnolia.module.blossom.support.IncludeRequestWrapper;
import info.magnolia.module.blossom.template.BlossomTemplateRegistry;
import info.magnolia.module.blossom.template.TemplateDescriptionBuilder;
import info.magnolia.module.blossom.urimapping.AnnotatedVirtualURIMappingExporter;
import info.magnolia.module.blossom.urimapping.VirtualURIMappingExporter;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.commons.DefaultGrailsControllerClass;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsClass;
import org.codehaus.groovy.grails.web.pages.GroovyPageOutputStack;
import org.codehaus.groovy.grails.web.servlet.GrailsDispatcherServlet;
import org.codehaus.groovy.grails.web.servlet.WrappedResponseHolder;
import org.codehaus.groovy.grails.web.util.IncludeResponseWrapper;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.LocaleResolver;

import javax.jcr.RepositoryException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GrailsBlossomDispatcherServlet extends GrailsDispatcherServlet implements BlossomDispatcher {

	@Override
	public HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
		return super.getHandlerAdapter(handler);
	}

	@Override
	protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) throws BeansException {
		System.out.println("I am here now");

		Class<?> contextClass = getContextClass();
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Servlet with name '" + getServletName() +
					"' will try to create custom WebApplicationContext context of class '" +
					contextClass.getName() + "'" + ", using parent context [" + parent + "]");
		}
		if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
			throw new ApplicationContextException(
					"Fatal initialization error in servlet with name '" + getServletName() +
							"': custom WebApplicationContext class [" + contextClass.getName() +
							"] is not of type ConfigurableWebApplicationContext");
		}
		ConfigurableWebApplicationContext wac =
				(ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

		// Assign the best possible id value.
		ServletContext sc = getServletContext();
		String servletContextName = sc.getServletContextName();
		if (servletContextName != null) {
			wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + servletContextName +
					"." + getServletName());
		} else {
			wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + getServletName());
		}

		wac.setParent(parent);
		wac.setServletContext(getServletContext());
		wac.setServletConfig(getServletConfig());
		wac.setNamespace(getNamespace());
		wac.setConfigLocation(getContextConfigLocation());
//		wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));

		postProcessWebApplicationContext(wac);
		wac.refresh();

		return wac;

	}

	public void forward(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		bindHibernateSession();
		String contextPath = request.getContextPath();

		request = new ForwardRequestWrapper(request, contextPath + path, contextPath, path, null);
//		System.out.println("In forward -- before for " + path);

		MgnlContext.push(request, response);
		try {
			super.service(request, response);
		} finally {
			MgnlContext.pop();
//			unbindHibernateSession();
		}
//		System.out.println("In forward -- after for " + path);
	}

	private void unbindHibernateSession() {
		SessionFactory sessionFactory = (SessionFactory) ApplicationHolder.getApplication().getMainContext().getBean("sessionFactory");
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
		try {
			if (!FlushMode.MANUAL.equals(sessionHolder.getSession().getFlushMode())) {
				sessionHolder.getSession().flush();
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("Cannot flush Hibernate Sesssion, error will be ignored", e);
		} finally {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			SessionFactoryUtils.closeSession(sessionHolder.getSession());
			if (logger.isDebugEnabled()) logger.debug("Hibernate Session is unbounded from Job thread and closed");
		}
	}

	private void bindHibernateSession() {
		SessionFactory sessionFactory = (SessionFactory) ApplicationHolder.getApplication().getMainContext().getBean("sessionFactory");
		System.out.println("sessionFactory : " + sessionFactory.getClass());
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.AUTO);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		System.out.println("Hibernate Session is bounded to forward thread");
	}

	public void include(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contextPath = request.getContextPath();
//		System.out.println("In include -- before for " + path);

		request = new IncludeRequestWrapper(request, contextPath + path, contextPath, path, null, request.getQueryString());

		MgnlContext.push(request, response);
		try {


			HttpServletResponse wrapped = WrappedResponseHolder.getWrappedResponse();
			response = wrapped != null ? wrapped : response;
			final IncludeResponseWrapper responseWrapper = new IncludeResponseWrapper(response);
			WrappedResponseHolder.setWrappedResponse(responseWrapper);
			super.service(request, responseWrapper);
//            super.service(request, response);

			GroovyPageOutputStack.currentWriter().write(String.valueOf(responseWrapper.getContent()));
			WrappedResponseHolder.setWrappedResponse(wrapped);

		} finally {
			MgnlContext.pop();
		}
//		System.out.println("In include -- after for " + path);
	}


	@Override
	protected Object getDefaultStrategy(ApplicationContext context, Class strategyInterface) throws BeansException {
		if (strategyInterface.equals(LocaleResolver.class))
			return super.createDefaultStrategy(context, MagnoliaLocaleResolver.class);
		return super.getDefaultStrategy(context, strategyInterface);
	}

	public void registerControllers() {
		GrailsApplication grailsApplication = ApplicationHolder.getApplication();
		GrailsClass[] allControllerClasses = grailsApplication.getArtefacts("Controller");
		for (GrailsClass controllerClass : allControllerClasses) {
			DefaultGrailsControllerClass controller = (DefaultGrailsControllerClass) controllerClass;
			String handlerPath = controller.getViewByName("");	 // We are assuming that the action to be used as handler will be the default action
			System.out.println("The handlerPath is :   " + handlerPath);
			Object handler = grailsApplication.getMainContext().getBean(controllerClass.getFullName());
			//register Template
			registerTemplatesToMagnolia(controllerClass, handlerPath, handler);
			//register Template
			registerParagraphsToMagnolia(controllerClass, handlerPath, handler);

			registerDialog(controllerClass, handler, controllerClass.getFullName());

		}
	}

	private void registerDialog(GrailsClass controllerClass, Object bean, String beanName) {
		if (controllerClass.getClazz().isAnnotationPresent(DialogFactory.class)) {
			System.out.println("Found Annotation DialogFactory");
			try {
				BlossomDialogRegistry dialogRegistry = BlossomModule.getDialogRegistry();
				dialogRegistry.registerDialogFactory(bean);
			} catch (RepositoryException e) {
				logger.error("Unable to register dialog factory [" + bean.getClass().getName() + "] with bean name [" + beanName + "]", e);
			}
		}
	}

	private void registerParagraphsToMagnolia(GrailsClass controllerClass, String handlerPath, Object handler) {
		if (controllerClass.getClazz().isAnnotationPresent(Paragraph.class)) {
			try {
				BlossomParagraphRegistry paragraphRegistry = BlossomModule.getParagraphRegistry();
				ParagraphDescriptionBuilder descriptionBuilder = new ParagraphDescriptionBuilder();
				String name = descriptionBuilder.buildDescription(this, handler, handlerPath).getName();
				if (paragraphRegistry.getParagraph(name) == null)
					paragraphRegistry.registerParagraph(this, handler, handlerPath);
			} catch (RepositoryException e) {
				logger.error("Unable to register paragraph [" + handler.getClass().getName() + "] with handlerPath [" + handlerPath + "]", e);
			}
		}
	}

	private void registerTemplatesToMagnolia(GrailsClass controllerClass, String handlerPath, Object handler) {
		if (controllerClass.getClazz().isAnnotationPresent(Template.class)) {
			System.out.println("Template Registry ");
			try {
				BlossomTemplateRegistry templateRegistry = BlossomModule.getTemplateRegistry();
				TemplateDescriptionBuilder descriptionBuilder = new TemplateDescriptionBuilder();
				String name = descriptionBuilder.buildDescription(this, handler, handlerPath).getName();
				if (templateRegistry.getTemplate(name) == null)
					templateRegistry.registerTemplate(this, handler, handlerPath);
			} catch (RepositoryException e) {
				logger.error("Unable to register template [" + handler.getClass().getName() + "] with handlerPath [" + handlerPath + "]", e);
			}
			try {
				BlossomModule.getDialogRegistry().registerDialogFactories(handler);
			} catch (RepositoryException e) {
				logger.error("Unable to register dialog factories within template [" + handler.getClass().getName() + "] with handlerPath [" + handlerPath + "]", e);
			}
		}
	}


	@Override
	protected void onRefresh
			(ApplicationContext
					 context) throws BeansException {

		super.onRefresh(context);
		context.getAutowireCapableBeanFactory().createBean(DialogExporter.class);
		context.getAutowireCapableBeanFactory().createBean(AnnotatedVirtualURIMappingExporter.class);
		context.getAutowireCapableBeanFactory().createBean(VirtualURIMappingExporter.class);
		registerControllers();
	}
}





