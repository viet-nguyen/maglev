package grails.mgnl

abstract class BaseTemplate {

    def afterInterceptor = {model ->
        //println "For Template $model"
        model.contentMap = templateContentMap
        //println "Model ::: $model"
    }
}
