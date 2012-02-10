package grails.mgnl

import info.magnolia.module.blossom.annotation.DialogFactory
import info.magnolia.module.blossom.annotation.Template
import info.magnolia.module.blossom.dialog.DialogBuilder
import info.magnolia.module.blossom.dialog.TabBuilder

@Template("Demo template")
class DemoTemplateController extends BaseTemplate {

    static transactional = true

    def personService

    def index = {

        if (request.method == "POST") {
            //println params
            if (params.keySet().containsAll(['name', 'age'])) {
                new Person(name: params.name, age: params.int('age')).save()
            }
        }
        Pet p = new Pet()
        p.name = 'Random Name ' + Math.random()
        p.save()
        //println p.id

        personService.createTobbe()
     //  println templateContentMap.keySet()
//		println templateContentMap.values() // This throws exception as not implemented/supported

        render(view: "demoTemplate", model: [pet: p, pets: Pet.findAll()])
    }

    @DialogFactory("main-properties")
    public void propertiesDialog(DialogBuilder builder) {
        TabBuilder settings = builder.addTab("Content");
        settings.addEdit("title", "Title", "");
    }

}
