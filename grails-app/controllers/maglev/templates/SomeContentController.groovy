package maglev.templates

import info.magnolia.module.blossom.annotation.Template

@Template(id = "grailsModule:components/someContent", title = "Some content")
class SomeContentController {

    def index() {
        render view: "/component/someContent"
    }
}
