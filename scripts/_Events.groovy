import groovy.xml.StreamingMarkupBuilder

includeTargets << grailsScript("_GrailsWar")

eventCompileStart = { kind->
	File configFolder = new File("${basedir}/web-app/WEB-INF/config")
	println "In Event Compile start"
	if(!configFolder.exists()){
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

  def servletMappings = xml.'servlet-mapping'
  def cnt2 = servletMappings.size()
  def grailsServletMapping = servletMappings.findAll {it.'servlet-name'.text() == 'grails'}
  grailsServletMapping.replaceNode {}

  def listeners = xml.'listener'
  def grailsListener = listeners.findAll {it.'listener-class'.text() == 'org.codehaus.groovy.grails.web.context.GrailsContextLoaderListener'}
  grailsListener.replaceNode {}

  def filterMappings = xml.'filter-mapping'
  def filterMapping = filterMappings.findAll {it.'filter-name'.text() == 'urlMapping'}
  filterMapping.replaceNode {}


  def filterList = xml.'filter'
  def filterEntry = filterList.findAll { it.'filter-name'.text() == 'grailsWebRequest' }
  filterEntry.replaceNode {}

  filterMappings = xml.'filter-mapping'
  filterMapping = filterMappings.findAll {it.'filter-name'.text() == 'grailsWebRequest'}
  filterMapping.replaceNode {}



    filterList = xml.'filter'
    filterEntry = filterList.findAll { it.'filter-name'.text() == 'hiddenHttpMethod' }
    filterEntry.replaceNode {}

    filterMappings = xml.'filter-mapping'
    filterMapping = filterMappings.findAll {it.'filter-name'.text() == 'hiddenHttpMethod'}
    filterMapping.replaceNode {}



    filterList = xml.'filter'
    filterEntry = filterList.findAll { it.'filter-name'.text() == 'reloadFilter' }
    filterEntry.replaceNode {}

    filterMappings = xml.'filter-mapping'
    filterMapping = filterMappings.findAll {it.'filter-name'.text() == 'reloadFilter'}
    filterMapping.replaceNode {}





  filterList = xml.'filter'
  filterEntry = filterList.findAll { it.'filter-name'.text() == 'urlMapping' }
  filterEntry.replaceNode {}

  filterList = xml.'filter'
  filterEntry = filterList.findAll { it.'filter-name'.text() == 'charEncodingFilter' }
  filterEntry.each { tag ->
      tag.'filter-class' = 'com.altaworks.spring.SmartDelegatingFilterProxy'
  }

  filterList = xml.'filter'
  filterEntry = filterList.findAll { it.'filter-name'.text() == 'sitemesh' }
  filterEntry.replaceNode {}

  filterMappings = xml.'filter-mapping'
  filterMapping = filterMappings.findAll {it.'filter-name'.text() == 'sitemesh'}
  filterMapping.replaceNode {}




    filterMappings[filterMappings.size() - 1] + {
      'filter' {
        'filter-name'('delegatingFilterProxy2')
        'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
        'init-param' {
            'param-name'('targetBeanName')
            'param-value'('hiddenHttpMethodFilterBean')
        }
      }
    }

    filterMappings[filterMappings.size() - 1] + {
      'filter-mapping' {
        'filter-name'('delegatingFilterProxy2')
        'url-pattern'('/*')
        'dispatcher'('REQUEST')
        'dispatcher'('FORWARD')

      }
    }



    filterMappings[filterMappings.size() - 1] + {
      'filter' {
        'filter-name'('delegatingFilterProxy3')
        'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
        'init-param' {
            'param-name'('targetBeanName')
            'param-value'('reloadFilterFilterBean')
        }
      }
    }

    filterMappings[filterMappings.size() - 1] + {
      'filter-mapping' {
        'filter-name'('delegatingFilterProxy3')
        'url-pattern'('/*')
        'dispatcher'('REQUEST')
        'dispatcher'('FORWARD')

      }
    }






    filterMappings[filterMappings.size() - 1] + {
      'filter' {
        'filter-name'('delegatingFilterProxy')
        'filter-class'('com.altaworks.spring.SmartDelegatingFilterProxy')
        'init-param' {
            'param-name'('targetBeanName')
            'param-value'('grailsWebRequestFilterBean')
        }
      }
    }

    filterMappings[filterMappings.size() - 1] + {
      'filter-mapping' {
        'filter-name'('delegatingFilterProxy')
        'url-pattern'('/*')
        'dispatcher'('REQUEST')
        'dispatcher'('FORWARD')

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
        mkp.declareNamespace("": "http://java.sun.com/xml/ns/j2ee")
        mkp.yield(xml)
    }
}



eventGenerateWebXmlEnd = {
    /* needed because Magnolia Core Module checks web.xml for correctness */
    ant.copy(file:webXmlFile, todir:"${basedir}/web-app/WEB-INF", overwrite:true)

    /* copy deps to lib in dev too since magnolia reads easier there */
    defaultWarDependencies.curry(ant)
    def libDir = "${basedir}/web-app/WEB-INF/lib"
    ant.mkdir(dir:libDir)
    ant.copy(todir:libDir, defaultWarDependencies)

}




