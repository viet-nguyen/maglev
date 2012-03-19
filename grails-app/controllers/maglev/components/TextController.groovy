package maglev.components

import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder
import info.magnolia.module.blossom.annotation.Template
import info.magnolia.module.blossom.annotation.TemplateDescription
import info.magnolia.module.blossom.annotation.Inherits

@Template(id = "grailsModule:components/text", title = "Text")
@TemplateDescription("Simple textblock with header")
public class TextController{

    def index = {
        render(view: "text")
    }

    @TabFactory("Content")
    public void addDialog(TabBuilder tab) {
        tab.addEdit("title","Title","")
        tab.addFckEditor("body", "Text", "");
        tab.addCheckbox("inheritable","Inherit","")
    }
}
