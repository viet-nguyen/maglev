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
        render view: "demoTemplate"
    }

    @DialogFactory("main-properties")
    public void propertiesDialog(DialogBuilder builder) {
        TabBuilder settings = builder.addTab("Content");
        settings.addEdit("title", "Title", "");
    }

}
