import com.altaworks.magnolia.GrailsTemplateExporter
import info.magnolia.context.MgnlContext
import info.magnolia.jcr.util.ContentMap
import info.magnolia.module.blossom.annotation.Area
import info.magnolia.module.blossom.annotation.Template
import info.magnolia.module.blossom.render.RenderContext
import org.codehaus.groovy.grails.commons.ControllerArtefactHandler
import org.codehaus.groovy.grails.web.context.ServletContextHolder

class MaglevGrailsPlugin {
    // the plugin version
    def version = "0.3.5-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [core: "2.0.0 > *"]

    // since we need to remove grails dispatcher, which is added by controllers-plugin
    def loadAfter = ['controllers', 'webxml']

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

    def documentation = "https://github.com/Bonheur/maglev/wiki"

    def doWithWebDescriptor = { xml ->
        def contextParam = xml.'context-param'
        contextParam[contextParam.size() - 1] + {
            'listener' {
                'listener-class'(com.altaworks.magnolia.GrailsMgnlServletContextListener.name)
            }
        }

        contextParam[contextParam.size() - 1] + {
            'filter' {
                'filter-name'('magnoliaFilterChain')
                'filter-class'(info.magnolia.cms.filters.MgnlMainFilter.name)
            }
        }

    }

    def getWebXmlFilterOrder() {
        def FilterManager =
            getClass().getClassLoader().loadClass('grails.plugin.webxml.FilterManager')
        [magnoliaFilterChain: FilterManager.URL_MAPPING_POSITION + 100]
    }


    def doWithSpring = {

        if (ServletContextHolder.getServletContext()) {
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

        for (controller in application.controllerClasses) {
            for (Class<?> aClass : controller.clazz.classes) {
                def name = aClass.getName()
                application.addArtefact(aClass)
                "${name}"(aClass) { bean ->
                    bean.autowire = "byName"
                }
            }
        }

    }

    def doWithDynamicMethods = { ctx ->
        def grailsApplication = ctx.getBean('grailsApplication')
        addMagnoliaPropertiesToTemplates(grailsApplication)
    }

    private def addMagnoliaPropertiesToTemplates(grailsApplication) {

        grailsApplication.controllerClasses.each {controllerClass ->
            addTemplateProperties(controllerClass)
        }
    }

    private def addTemplateProperties(controllerClass) {
        if (controllerClass.getClazz().isAnnotationPresent(Template.class)) {
            controllerClass.metaClass.getContent = {
                if (MgnlContext.isWebContext()) {
                    if (MgnlContext.aggregationState.currentContent)
                        return new ContentMap((javax.jcr.Node) MgnlContext.aggregationState.currentContent.JCRNode)
                }
            }
            controllerClass.metaClass.getUser = {
                if (MgnlContext.isWebContext())
                    return MgnlContext.getUser();
            }
            controllerClass.metaClass.getAggregationState = {
                if (MgnlContext.isWebContext())
                    return MgnlContext.getAggregationState();
            }
        } else if (controllerClass.getClazz().isAnnotationPresent(Area.class)) {
            controllerClass.metaClass.getContent = {
                if (MgnlContext.isWebContext()) {
                    if (MgnlContext.aggregationState.currentContent)
                        return new ContentMap((javax.jcr.Node) MgnlContext.aggregationState.currentContent.JCRNode)
                }
            }
            controllerClass.metaClass.getUser = {
                if (MgnlContext.isWebContext())
                    return MgnlContext.getUser();
            }
            controllerClass.metaClass.getAggregationState = {
                if (MgnlContext.isWebContext())
                    return MgnlContext.getAggregationState();
            }
            controllerClass.metaClass.getComponents = {
                if (MgnlContext.isWebContext())
                    RenderContext.get().contextObjects.get("components")
            }
        }
    }

    def onChange = { event ->

        if (application.isArtefactOfType(ControllerArtefactHandler.TYPE, event.source)) {
            def context = event.ctx
            if (!context) {
                if (log.isDebugEnabled()) {
                    log.debug("Application context not found. Can't reload")
                }
                return
            }

            for (Class<?> aClass : event.source.classes) {
                def name = aClass.getName()
                def cC = application.addArtefact(ControllerArtefactHandler.TYPE, aClass)
                def beanDefinitions = beans {
                    "${name}"(aClass) { bean ->
                        bean.autowire = true
                    }
                }
                beanDefinitions.registerBeans(event.ctx)
                cC.initialize()
            }

            GrailsTemplateExporter.reload()

            addMagnoliaPropertiesToTemplates(application)
        }

    }

}
