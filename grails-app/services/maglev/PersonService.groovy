package maglev

import grails.mgnl.Person

class PersonService {

    static transactional = true


	void createPerson() {
		new Person(name:'He Man', age: (35)).save(failOnError:true)
	}


    void createTobbe() {
        new Person(name:'Tobbe', age: (12)).save(failOnError:true)
    }

}

