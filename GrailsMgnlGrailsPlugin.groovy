import info.magnolia.cms.core.AggregationState
import info.magnolia.cms.core.Content
import info.magnolia.context.MgnlContext
import info.magnolia.module.blossom.annotation.Paragraph
import info.magnolia.module.blossom.annotation.Template
import org.codehaus.groovy.grails.commons.DefaultGrailsControllerClass
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.commons.GrailsApplication
import com.altaworks.magnolia.ContentMap
import info.magnolia.module.blossom.BlossomConfiguration
import info.magnolia.module.blossom.BlossomModule
import org.springframework.web.context.WebApplicationContext
import com.altaworks.magnolia.GrailsBlossomDispatcherServlet
import com.altaworks.magnolia.GrailsModule

class GrailsMgnlGrailsPlugin {
	// the plugin version
	def version = "0.1"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "2.0.0 > *"
	// the other plugins this plugin depends on
	def dependsOn = [core: "2.0.0 > *"]

	// since we need to remove grails dispatcher, which is added by controllers-plugin
	def loadAfter = ['controllers']

	// resources that are excluded from plugin packaging
	def pluginExcludes = [
			"grails-app/views/**" ,
			"grails-app/controllers/**" ,
			"web-app/css/**" ,
			"web-app/repositories/**" ,
			"web-app/js/**"
	]

	def author = "Kimmo Björnsson"
	def authorEmail = "kimmo@kokaihop.se"
	def title = "Grails Magnolia plugin"
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

	def doWithSpring = { applicationContext ->
		grailsWebRequestFilterBean(org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequestFilter) { bean ->
			bean.'lazyInit' = true
		}
		reloadFilterFilterBean(org.codehaus.groovy.grails.web.servlet.filter.GrailsReloadServletFilter) { bean ->
			bean.'lazyInit' = true
		}
		hiddenHttpMethodFilterBean(org.codehaus.groovy.grails.web.filters.HiddenHttpMethodFilter) { bean ->
			bean.'lazyInit' = true
		}
	}

	def doWithDynamicMethods = { ctx ->
		def grailsApplication = ctx.getBean('grailsApplication')
		addMagnoliaPropertiesToTemplatesAndParagraphs(grailsApplication)
	}

	private def addMagnoliaPropertiesToTemplatesAndParagraphs(grailsApplication) {
		grailsApplication.controllerClasses.each {controllerClass ->
			if (controllerClass.getClazz().isAnnotationPresent(Template.class)) {
				controllerClass.metaClass.getTemplateContent = {
					AggregationState aggregationState = MgnlContext.getAggregationState();
					return aggregationState.getMainContent();
				}
                controllerClass.metaClass.getTemplateContentMap = {
                    AggregationState aggregationState = MgnlContext.getAggregationState();
                    return new ContentMap(aggregationState.getMainContent().getJCRNode());
                }
			}
          if (controllerClass.getClazz().isAnnotationPresent(Paragraph.class)) {
              controllerClass.metaClass.getContent = {
                  AggregationState aggregationState = MgnlContext.getAggregationState();
                  Content currentContent = aggregationState.getCurrentContent();
                  return currentContent;
              }
              controllerClass.metaClass.getContentMap = {
                AggregationState aggregationState = MgnlContext.getAggregationState();
                Content currentContent = aggregationState.getCurrentContent();
                return new ContentMap(currentContent.getJCRNode());
              }
          }
          if ((controllerClass.getClazz().isAnnotationPresent(Template.class)) || (controllerClass.getClazz().isAnnotationPresent(Paragraph.class))) {
              controllerClass.metaClass.getUser = {
                return MgnlContext.getUser();
              }
              controllerClass.metaClass.getAggregationState = {
                return MgnlContext.getAggregationState();
              }
              controllerClass.metaClass.getWebContext = {
                return MgnlContext.getWebContext();
              }
              controllerClass.metaClass.getContext = {
                return MgnlContext.getInstance();
              }
          }

		}
	}

	def onChange = { event ->
        BlossomModule.getParagraphRegistry().getParagraphs().clear()
        BlossomModule.getParagraphRegistry().afterPropertiesSet()
        GrailsModule.grailsBlossomDispatcherServlet.registerControllers()

        def grailsApplication = event.ctx.getBean('grailsApplication')
        addMagnoliaPropertiesToTemplatesAndParagraphs(grailsApplication)
	}

}
