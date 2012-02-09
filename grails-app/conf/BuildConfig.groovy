grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
//        excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        inherits true
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"

//        faced unresolved dependencies with older nexus repo
//        mavenRepo "http://nexus.magnolia-cms.com"
        mavenRepo "http://nexus.magnolia-cms.com/content/groups/public/"
        mavenRepo "http://archiva.kokaihop.se/archiva/repository/internal"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        String magnoliaVersion = "4.4.5"
        runtime('info.magnolia:magnolia-core:' + magnoliaVersion) {
            excludes([ name: 'commons-logging' ], 'slf4j-log4j12')
        }
        runtime('info.magnolia:magnolia-module-templating:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-module-admininterface:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-taglib-utility:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-taglib-cms:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-module-exchange-simple:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-gui:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-jaas:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-module-fckeditor:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-module-mail:' + magnoliaVersion){
            excludes([ name: 'commons-logging' ])
        }
        runtime('org.apache.jackrabbit:jackrabbit-core:1.6.4'){
            excludes([ name: 'commons-logging' ])
        }
        runtime('javax.jcr:jcr:2.0'){
            excludes([ name: 'commons-logging' ])
        }
        runtime('net.sourceforge.openutils:openutils-log4j:2.0.5'){
            excludes([ name: 'commons-logging' ])
        }
        runtime('info.magnolia:magnolia-module-blossom:1.2.2'){
            excludes([ name: 'commons-logging' ])
        }
    }

}
