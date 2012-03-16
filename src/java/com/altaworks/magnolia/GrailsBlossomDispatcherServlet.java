package com.altaworks.magnolia;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.blossom.context.MagnoliaLocaleResolver;
import info.magnolia.module.blossom.dialog.DialogExporter;
import info.magnolia.module.blossom.dispatcher.BlossomDispatcher;
import info.magnolia.module.blossom.dispatcher.BlossomDispatcherAwareBeanPostProcessor;
import info.magnolia.module.blossom.dispatcher.BlossomDispatcherInitializedEvent;
import info.magnolia.module.blossom.support.BeanFactoryUtils;
import info.magnolia.module.blossom.support.ForwardRequestWrapper;
import info.magnolia.module.blossom.support.IncludeRequestWrapper;
import info.magnolia.module.blossom.urimapping.AnnotatedVirtualURIMappingExporter;
import info.magnolia.module.blossom.urimapping.VirtualURIMappingExporter;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.codehaus.groovy.grails.web.pages.GroovyPageOutputStack;
import org.codehaus.groovy.grails.web.servlet.GrailsDispatcherServlet;
import org.codehaus.groovy.grails.web.servlet.WrappedResponseHolder;
import org.codehaus.groovy.grails.web.util.IncludeResponseWrapper;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GrailsBlossomDispatcherServlet extends GrailsDispatcherServlet implements BlossomDispatcher, BeanFactoryPostProcessor {

	@Override
	protected Object getDefaultStrategy(ApplicationContext context, Class strategyInterface) throws BeansException {
		if (strategyInterface.equals(LocaleResolver.class)) {
			return super.createDefaultStrategy(context, MagnoliaLocaleResolver.class);
		}
		return super.getDefaultStrategy(context, strategyInterface);
	}

	@Override
	protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {
		wac.addBeanFactoryPostProcessor(this);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Assert.isInstanceOf(BeanDefinitionRegistry.class, beanFactory);

		BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

		beanFactory.addBeanPostProcessor(new BlossomDispatcherAwareBeanPostProcessor(this));

		BeanFactoryUtils.registerBeanIfMissing(beanFactory, registry, GrailsTemplateExporter.class);
		BeanFactoryUtils.registerBeanIfMissing(beanFactory, registry, DialogExporter.class);
		BeanFactoryUtils.registerBeanIfMissing(beanFactory, registry, AnnotatedVirtualURIMappingExporter.class);
		BeanFactoryUtils.registerBeanIfMissing(beanFactory, registry, VirtualURIMappingExporter.class);
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() throws BeansException {
		WebApplicationContext wac = super.initWebApplicationContext();
		wac.publishEvent(new BlossomDispatcherInitializedEvent(this));
		return wac;
	}

	@Override
	public HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
		return super.getHandlerAdapter(handler);
	}


	public void forward(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contextPath = request.getContextPath();

		request = new ForwardRequestWrapper(request, contextPath + path, contextPath, path, null);

		MgnlContext.push(request, response);
		try {
			super.service(request, response);
		} finally {
			MgnlContext.pop();
		}
	}


	public void include(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String contextPath = request.getContextPath();

		request = new IncludeRequestWrapper(request, contextPath + path, contextPath, path, null, request.getQueryString());

		MgnlContext.push(request, response);
		try {


			HttpServletResponse wrapped = WrappedResponseHolder.getWrappedResponse();
			response = wrapped != null ? wrapped : response;
			final IncludeResponseWrapper responseWrapper = new IncludeResponseWrapper(response);
			WrappedResponseHolder.setWrappedResponse(responseWrapper);
			super.service(request, responseWrapper);

			GroovyPageOutputStack.currentWriter().write(String.valueOf(responseWrapper.getContent()));
			WrappedResponseHolder.setWrappedResponse(wrapped);

		} finally {
			MgnlContext.pop();
		}
	}

	@Override
	protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent) throws BeansException {

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

		postProcessWebApplicationContext(wac);
		wac.refresh();

		return wac;

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
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		session.setFlushMode(FlushMode.AUTO);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}

}





