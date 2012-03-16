package maglev.templates

import info.magnolia.module.blossom.annotation.Template
import info.magnolia.module.blossom.dialog.TabBuilder
import info.magnolia.module.blossom.annotation.TabFactory

@Template(id = "grailsModule:components/someContent", title = "Some content")
class SomeContentController {

    def index() {
        render view: "someContent"
    }

    @TabFactory("Content")
    void content(TabBuilder builder){
        builder.addFckEditor("text","Text","")
    }

}
