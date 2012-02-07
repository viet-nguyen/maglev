import grails.mgnl.Person

class BootStrap {

    def init = { servletContext ->
		println "Executing BootStrap now-----------------------"
		(1..4).each {
			new Person(name:'someName '+it, age: (10 * it)).save(failOnError:true)
		}
    }
    def destroy = {
    }
}
