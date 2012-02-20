package maglev.paragraphs

import info.magnolia.module.blossom.annotation.Paragraph
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder
import grails.mgnl.Person
import grails.mgnl.Pet

@Paragraph("Pet form")
class PetFormParagraphController {

    def personService

    def index = {
        if (request.method == "POST") {
            if (params.keySet().containsAll(['name', 'age'])) {
                new Person(name: params.name, age: params.int('age')).save()
            }
        }
        Pet p = new Pet()
        p.name = 'Random Name ' + Math.random()
        p.save()

        personService.createTobbe()

        render(view: "petForm", model: [pet: p, pets: Pet.findAll()])
    }

    @TabFactory("Pet")
    void dummyContent(TabBuilder builder) {
        builder.addHidden("dummy", "dummy")
    }
}
