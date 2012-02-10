package grails.mgnl

/**
 * Created by IntelliJ IDEA.
 * User: ankur
 * Date: 19/7/11
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class BaseParagraph {

    def afterInterceptor = {model ->
        //println "For paragraph $model"
        model.contentMap = contentMap.toHashMap()
        //println "Model ::: $model"
    }
}
