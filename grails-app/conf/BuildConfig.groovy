grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.dependency.resolution = {

    inherits("global") {

    }
    log "warn"
    repositories {
        inherits true
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"

        mavenRepo "http://nexus.magnolia-cms.com/content/groups/public/"
    }
    dependencies {
        String magnoliaVersion = "4.5.1"

        runtime('info.magnolia:magnolia-core:' + magnoliaVersion) {
            exclude 'slf4j-log4j12'
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-gui:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-jaas:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-module-admininterface:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-module-exchange-simple:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-module-fckeditor:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-module-mail:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }

        runtime('info.magnolia:magnolia-templating-jsp:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }

        runtime('info.magnolia:magnolia-rendering:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-templating:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-templating-editor:' + magnoliaVersion) {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('org.apache.jackrabbit:jackrabbit-core:2.4.0') {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('javax.jcr:jcr:2.0') {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('net.sourceforge.openutils:openutils-log4j:2.0.5') {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
        runtime('info.magnolia:magnolia-module-blossom:2.0') {
            exclude group: 'commons-logging', name: 'commons-logging'
        }
    }

    plugins {
        build ":release:1.0.1"
    }

}
