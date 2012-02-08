import groovy.xml.StreamingMarkupBuilder

includeTargets << grailsScript("_GrailsWar")

eventCompileStart = { kind ->
    File configFolder = new File("${basedir}/web-app/WEB-INF/config")
    println "In Event Compile start"
    if (!configFolder.exists()) {
        println "Copying config to web-inf"
        ant.copydir(src: "${basedir}/web-app/config", dest: "${basedir}/web-app/WEB-INF/config")
    }

}

eventWebXmlEnd = { String tmpfile ->

    def xml = new XmlSlurper().parse(webXmlFile)
    // remove GrailsDispatcherServlet, seems there is only support for one to exists in each servletContext
    def servlet = xml.'servlet'
    def grailsDispatcher = servlet.findAll {node -> node.'servlet-name'.text() == 'grails'}
    grailsDispatcher.replaceNode {}

    def gsp = servlet.findAll {node -> node.'servlet-name'.text() == 'gsp'}
    gsp.replaceNode {}

    def servletMappings = xml.'servlet-mapping'
    def grailsServletMapping = servletMappings.findAll {it.'servlet-name'.text() == 'grails'}
    grailsServletMapping.replaceNode {}

    def gspMapping = servletMappings.findAll {it.'servlet-name'.text() == 'gsp'}
    gspMapping.replaceNode {}

    def listeners = xml.'listener'
    def grailsListener = listeners.findAll {it.'listener-class'.text() == 'org.codehaus.groovy.grails.web.context.GrailsContextLoaderListener'}
    grailsListener.replaceNode {}

    //Now for the filters

    def filterList = xml.'filter'
    def filterMappings = xml.'filter-mapping'

    //remove for now
    filterEntry = filterList.findAll { it.'filter-name'.text() == 'urlMapping' }
    filterEntry.replaceNode {}

    def filterMapping = filterMappings.findAll {it.'filter-name'.text() == 'urlMapping'}
    filterMapping.replaceNode {}

    def filterEntry = filterList.findAll { it.'filter-name'.text() == 'grailsWebRequest' }
    filterEntry.replaceNode {
        'filter' {
            'filter-name'('grailsWebRequest')
            'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
            'init-param' {
                'param-name'('targetBeanName')
                'param-value'('grailsWebRequest')
            }
        }
    }

    filterEntry = filterList.findAll { it.'filter-name'.text() == 'hiddenHttpMethod' }
    filterEntry.replaceNode {
        'filter' {
            'filter-name'('hiddenHttpMethod')
            'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
            'init-param' {
                'param-name'('targetBeanName')
                'param-value'('hiddenHttpMethod')
            }
        }
    }

    filterEntry = filterList.findAll { it.'filter-name'.text() == 'reloadFilter' }
    filterEntry.replaceNode {
        'filter' {
            'filter-name'('reloadFilter')
            'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
            'init-param' {
                'param-name'('targetBeanName')
                'param-value'('reloadFilter')
            }
        }
    }

    filterList = xml.'filter'
    filterEntry = filterList.findAll { it.'filter-name'.text() == 'charEncodingFilter' }
    filterEntry.each { tag ->
        tag.'filter-class' = 'com.altaworks.spring.SmartDelegatingFilterProxy'
    }

    //We will remove sitemesh for now. Maybe we can get it back again
    filterEntry = filterList.findAll { it.'filter-name'.text() == 'sitemesh' }
    filterEntry.replaceNode {
        'filter' {
            'filter-name'('sitemesh')
            'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
            'init-param' {
                'param-name'('targetBeanName')
                'param-value'('sitemesh')
            }
        }
    }

    filterMappings[filterMappings.size() - 1] + {
        'filter-mapping' {
            'filter-name'('magnoliaFilterChain')
            'url-pattern'('/*')
            'dispatcher'('REQUEST')
            'dispatcher'('FORWARD')
            'dispatcher'('INCLUDE')
            'dispatcher'('ERROR')

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




