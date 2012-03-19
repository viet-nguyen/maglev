package maglev.components

import grails.mgnl.Person
import grails.mgnl.Pet
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.annotation.Template
import info.magnolia.module.blossom.dialog.TabBuilder
import info.magnolia.module.blossom.annotation.TemplateDescription

@Template(id = "grailsModule:components/petForm", title = "Pet form")
@TemplateDescription("Some kind of pet form that does nothing...")
class PetFormParagraphController {

    def index = {
        if (params.keySet().containsAll(['name', 'age'])) {
            def p = new Person(name: params.name, age: params.int('age'))
            p.save()
        }
        render(view: "petForm", model: [pets: Pet.findAll()])
    }

    @TabFactory("Pet")
    void dummyContent(TabBuilder builder) {
        builder.addHidden("dummy", "dummy")
    }
}
