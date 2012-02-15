package maglev

import info.magnolia.module.blossom.annotation.Template

@Template("Wine template")
class ProductTemplateController {

    def index() {
        def wineName = params.article ?: ""
        render view: "productTemplate", model: ["name": wineName]
    }
}
