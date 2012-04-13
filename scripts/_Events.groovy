import groovy.xml.StreamingMarkupBuilder

includeTargets << grailsScript("_GrailsWar")

eventCompileStart = { kind ->
    File configFolder = new File("${basedir}/web-app/WEB-INF/config")
    if (!configFolder.exists()) {
        ant.copydir(src: "${basedir}/web-app/config", dest: "${basedir}/web-app/WEB-INF/config")
    }
}

eventWebXmlEnd = { String tmpfile ->

    def xml = new XmlSlurper().parse(webXmlFile)

    def servlets = xml.'servlet'
    def grailsServlet = servlets.findAll {it.'servlet-name'.text() == 'grails'}
    grailsServlet.replaceNode {
        'servlet'{
            'servlet-name'('grails')
            'servlet-class'('info.magnolia.module.blossom.web.InstallationAwareServletProxy')
            'init-param'{
                'param-name'('servletClass')
                'param-value'('org.codehaus.groovy.grails.web.servlet.GrailsDispatcherServlet')
            }
        }
    }

    def gsp = servlets.findAll {node -> node.'servlet-name'.text() == 'gsp'}
    gsp.replaceNode {}

    def servletMappings = xml.'servlet-mapping'
    def grailsServletMapping = servletMappings.findAll {it.'servlet-name'.text() == 'grails'}
  //  grailsServletMapping.replaceNode {}

    def gspMapping = servletMappings.findAll {it.'servlet-name'.text() == 'gsp'}
    gspMapping.replaceNode {}

    def listeners = xml.'listener'
    def grailsListener = listeners.findAll {it.'listener-class'.text() == 'org.codehaus.groovy.grails.web.context.GrailsContextLoaderListener'}
    grailsListener.replaceNode {}

    def filterList = xml.'filter'
    def filterMappings = xml.'filter-mapping'

    def filterEntry = filterList.findAll { it.'filter-name'.text() == 'urlMapping' }
    filterEntry.replaceNode {
        'filter' {
            'filter-name'('urlMapping')
            'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
            'init-param' {
                'param-name'('targetBeanName')
                'param-value'('urlMapping')
            }
        }
    }

    String filterString = "";

    filterList = xml.'filter'
    filterList.findAll {it.'filter-name'.text() != 'magnoliaFilterChain' && it.'filter-name'.text() != 'urlMapping'}.each {
        def name = it.'filter-name'.text();
        def clazz = it.'filter-class'.text();
        if (it.'filter-class'.text() != 'com.altaworks.spring.SmartDelegatingFilterProxy') {
            if (it.'filter-class'.text() == 'org.springframework.web.filter.DelegatingFilterProxy') {
                it.'filter-class' = 'com.altaworks.spring.SmartDelegatingFilterProxy'
            } else {
                it.replaceNode {
                    'filter' {
                        'filter-name'(name)
                        'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
                        'init-param' {
                            'param-name'('targetBeanName')
                            'param-value'(name)
                        }
                    }
                }
                filterString+=name+","+clazz+";"
            }
        }
    }

    def contextParam = xml.'context-param'
    contextParam[contextParam.size() - 1] + {
        'context-param' {
            'param-name'('smartDelegatingFilters')
            'param-value'(filterString)
        }
    }

    filterMappings[filterMappings.size() - 1] + {
        'filter-mapping' {
            'filter-name'('magnoliaFilterChain')
            'url-pattern'('/*')
            'dispatcher'('REQUEST')
            'dispatcher'('FORWARD')
            'dispatcher'('INCLUDE')
           // 'dispatcher'('ERROR')

        }
    }

    webXmlFile.text = new StreamingMarkupBuilder().bind {
        mkp.declareNamespace("tag0": "http://java.sun.com/xml/ns/j2ee")
        mkp.yield(xml)
    }
}



eventGenerateWebXmlEnd = {
    /* needed because Magnolia Core Module checks web.xml for correctness */
    ant.copy(file: webXmlFile, todir: "${basedir}/web-app/WEB-INF", overwrite: true)

    /* copy deps to lib in dev too since magnolia reads easier there */
    defaultWarDependencies.curry(ant)
    def libDir = "${basedir}/web-app/WEB-INF/lib"
    ant.mkdir(dir: libDir)
    ant.copy(todir: libDir, defaultWarDependencies)

}




