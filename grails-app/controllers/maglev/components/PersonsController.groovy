package maglev.components

import grails.mgnl.Person
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.annotation.Template
import info.magnolia.module.blossom.annotation.TemplateDescription
import info.magnolia.module.blossom.dialog.TabBuilder

@Template(id = "grailsModule:components/persons", title = "Persons")
@TemplateDescription("List of persons")
public class PersonsController {

    def personService

    def index = {
        if (request.method == "POST")
            if (params.keySet().containsAll(['name', 'age'])) {
                def p = new Person(name: params.name, age: params.int('age'))
                p.save()
            }

        Map model = [:]
        def persons = Person.list()
        model.put("persons", persons);

        render(view: 'persons', model: model)
    }

    @TabFactory("Content")
    public void addDialog(TabBuilder tab) {
        tab.addEdit("testString", "Test String", "Test String");
    }

}
