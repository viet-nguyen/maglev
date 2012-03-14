package maglev.templates

import info.magnolia.module.blossom.dialog.TabBuilder
import maglev.BaseTemplate
import info.magnolia.module.blossom.annotation.*

@Template(id = "grailsModule:pages/demoTemplate", title = "Demo template")
class DemoTemplateController extends BaseTemplate {

    static transactional = true

    def personService

    def index = {
        render view: "demoTemplate"
    }

    @TabFactory("Content")
    public void propertiesDialog(TabBuilder builder) {
        builder.addEdit("title", "Title", "");
    }

    @Area("main")
    @Inherits
    @AvailableComponentClasses([SomeContentController.class])
    class MainAreaController {

        def index = {
            render(view: "area/mainArea")
        }
    }


}
