package maglev.pages

import info.magnolia.module.blossom.annotation.Area
import info.magnolia.module.blossom.annotation.AvailableComponentClasses
import info.magnolia.module.blossom.annotation.Template
import maglev.components.TextController
import maglev.components.SomeContentController

@Template(id = "grailsModule:pages/productTemplate", title = "Productpage template")
class ProductTemplateController {

    def index() {
        def productName = params.article ?: ""
        render view: "productTemplate", model: ["name": productName]
    }


    @Area("mainArea")
    @AvailableComponentClasses([SomeContentController.class, TextController.class])
    static class MainAreaController {
        def index() {
            render view: "/productTemplate/mainArea"
        }
    }

}
