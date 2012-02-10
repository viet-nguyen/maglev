import grails.mgnl.Person

class BootStrap {

    def init = { servletContext ->
		(1..4).each {
			new Person(name:'someName '+it, age: (10 * it)).save(failOnError:true)
		}
    }
    def destroy = {
    }
}
