import com.altaworks.magnolia.ContentMap
import com.altaworks.magnolia.GrailsTemplateRegistry
import info.magnolia.cms.core.AggregationState
import info.magnolia.cms.core.Content
import info.magnolia.context.MgnlContext
import info.magnolia.module.blossom.BlossomModule
import info.magnolia.module.blossom.annotation.Paragraph
import info.magnolia.module.blossom.annotation.Template
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import com.altaworks.magnolia.GrailsModule

class MaglevGrailsPlugin {
    // the plugin version
    def version = "0.2-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [core: "2.0.0 > *"]

    // since we need to remove grails dispatcher, which is added by controllers-plugin
    def loadAfter = ['controllers']

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/**",
            "grails-app/controllers/**",
            "grails-app/domain/**",
            "grails-app/services/**",
            "grails-app/i18n/**",
            "web-app/css/**",
            "web-app/images/**",
            "web-app/logs/**",
            "web-app/repositories/**",
            "web-app/js/**"
    ]

    def author = "Kimmo Björnsson"
    def authorEmail = "kimmo@kokaihop.se"
    def title = "Maglev - Magnolia Grails Plugin"
    def description = '''\\
Runs Magnolia CMS as a plugin in Grails
'''
    def watchedResources = ["file:./grails-app/controllers/**/*Controller.groovy",
            "file:./plugins/*/grails-app/controllers/**/*Controller.groovy"]

    def documentation = "http://grails.org/plugin/grails-mgnl"

    def doWithWebDescriptor = { xml ->
        def contextParam = xml.'context-param'

        contextParam[contextParam.size() - 1] + {
            'listener' {
                'listener-class'(com.altaworks.magnolia.GrailsMgnlServletContextListener.name)
            }
        }

        contextParam[contextParam.size() - 1] + {
            'listener' {
                'listener-class'(info.magnolia.module.blossom.support.ServletContextExposingContextListener.name)
            }
        }

        contextParam[contextParam.size() - 1] + {
            'filter' {
                'filter-name'('magnoliaFilterChain')
                'filter-class'(info.magnolia.cms.filters.MgnlMainFilter.name)
            }
        }

    }

    def doWithSpring = {
        blossomConfiguration(info.magnolia.module.blossom.BlossomConfiguration);

        String parameter = ServletContextHolder.getServletContext().getInitParameter("smartDelegatingFilters")
        if (parameter != null) {
            String[] split = parameter.split(";")
            split.each {
                if (it != null) {
                    String[] filterDef = it.split(",")
                    "${filterDef[0]}"(this.getClass().getClassLoader().loadClass(filterDef[1]))
                }
            }
        }

        urlMapping(com.altaworks.spring.SelectiveUrlMappingFilter)

    }

    def doWithDynamicMethods = { ctx ->
        def grailsApplication = ctx.getBean('grailsApplication')
        addMagnoliaPropertiesToTemplatesAndParagraphs(grailsApplication)
    }

    private def addMagnoliaPropertiesToTemplatesAndParagraphs(grailsApplication) {

        grailsApplication.controllerClasses.each {controllerClass ->
            if (controllerClass.getClazz().isAnnotationPresent(Template.class)) {
                controllerClass.metaClass.getTemplateContent = {
                    if (MgnlContext.isWebContext())
                        return MgnlContext.getAggregationState().getMainContent();

                }
                controllerClass.metaClass.getTemplateContentMap = {
                    if (MgnlContext.isWebContext())
                        return new ContentMap(MgnlContext.getAggregationState().getMainContent().getJCRNode());
                }
            }
            if (controllerClass.getClazz().isAnnotationPresent(Paragraph.class)) {
                controllerClass.metaClass.getContent = {
                    if (MgnlContext.isWebContext())
                        return MgnlContext.getAggregationState().getCurrentContent();
                }
                controllerClass.metaClass.getContentMap = {
                    if (MgnlContext.isWebContext())
                        return new ContentMap(MgnlContext.getAggregationState().getCurrentContent().getJCRNode());
                }
            }
            if ((controllerClass.getClazz().isAnnotationPresent(Template.class)) || (controllerClass.getClazz().isAnnotationPresent(Paragraph.class))) {
                controllerClass.metaClass.getUser = {
                    if (MgnlContext.isWebContext())
                        return MgnlContext.getUser();
                }
                controllerClass.metaClass.getAggregationState = {
                    if (MgnlContext.isWebContext())
                        return MgnlContext.getAggregationState();
                }
                controllerClass.metaClass.getWebContext = {
                    if (MgnlContext.isWebContext())
                        return MgnlContext.getWebContext();
                }
                controllerClass.metaClass.getContext = {
                    if (MgnlContext.isWebContext())
                        return MgnlContext.getInstance();
                }
                controllerClass.metaClass.getContentMap = {
                    if (MgnlContext.isWebContext())
                        return new ContentMap(MgnlContext.getAggregationState().getCurrentContent().getJCRNode());
                }
            }

        }
    }

    def doWithApplicationContext = { applicationContext ->
        def templateRegistry = new GrailsTemplateRegistry();
        templateRegistry.afterPropertiesSet();
        def config = applicationContext.getBean("blossomConfiguration")
        config.setTemplateRegistry(templateRegistry);
    }

    def onChange = { event ->
        BlossomModule.getParagraphRegistry().getParagraphs().clear()
        BlossomModule.getParagraphRegistry().afterPropertiesSet()
        GrailsModule.grailsBlossomDispatcherServlet.registerControllers()

        def grailsApplication = event.ctx.getBean('grailsApplication')
        addMagnoliaPropertiesToTemplatesAndParagraphs(grailsApplication)
    }

}
