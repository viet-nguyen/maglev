package maglev

import info.magnolia.module.blossom.annotation.Template

@Template("Productpage template")
class ProductTemplateController {

    def index() {
        def productName = params.article ?: ""
        render view: "productTemplate", model: ["name": productName]
    }
}
