package maglev.pages


import info.magnolia.module.blossom.dialog.TabBuilder
import maglev.components.PageLinkController
import maglev.components.PersonsController
import maglev.components.SomeContentController
import maglev.components.TextController
import info.magnolia.module.blossom.annotation.*

@Template(id = "grailsModule:pages/demoTemplate", title = "Demo template")
class DemoTemplateController {

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
    @AvailableComponentClasses([SomeContentController.class,
    TextController.class, PersonsController.class,
    PageLinkController.class])
    static class MainAreaController {

        def index = {
            render(view: "/demoTemplate/mainArea")
        }

        @TabFactory("Info")
        public void propertiesDialog(TabBuilder builder) {
        }

    }

    @Area("rightColumn")
    @Inherits(components = ComponentInheritanceMode.ALL)
    @AvailableComponentClasses([SomeContentController.class,
    TextController.class])
    static class RightColumnController {

        def index = {
            render(view: "/demoTemplate/rightColumn")
        }

        @TabFactory("Info")
        public void propertiesDialog(TabBuilder builder) {
            builder.addEdit("test","Test","")
        }
    }


}
