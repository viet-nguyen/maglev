package maglev.templates

import info.magnolia.module.blossom.dialog.TabBuilder
import info.magnolia.module.blossom.annotation.*
import maglev.paragraphs.TextController

@Template(id = "grailsModule:pages/demoTemplate", title = "Demo template")
class DemoTemplateController{

    static transactional = true

    def personService

    def index = {
        render view: "demoTemplate"
    }

    @TabFactory("Content")
    public void propertiesDialog(TabBuilder builder) {
        builder.addEdit("title", "Title", "");
    }


    @Area("mainArea")
    @Inherits
    @AvailableComponentClasses([SomeContentController.class,TextController.class])
    static class MainAreaController {

        def index = {
            render(view: "/demoTemplate/mainArea")
        }


        @TabFactory("header")
        void header(TabBuilder builder){
            builder.addEdit("header","Header","")
        }
    }

}
