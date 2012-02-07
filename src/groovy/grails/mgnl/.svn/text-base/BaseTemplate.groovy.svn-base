package grails.mgnl

abstract class BaseTemplate {

    def afterInterceptor = {model ->
        println "For Template $model"
        model.contentMap = templateContentMap.toHashMap()
        println "Model ::: $model"
    }
}
