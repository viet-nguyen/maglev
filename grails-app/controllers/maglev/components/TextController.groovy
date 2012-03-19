package maglev.components

import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder
import info.magnolia.module.blossom.annotation.Template

/**
 * Simple paragraph for adding text to a page.
 */
//@Paragraph("Text")
//@ParagraphDescription("Simple text block")
@Template(id = "grailsModule:components/text", title = "Text")
public class TextController{

    def index = {
        def x = 10;
        x += 20
        render(view: "text")
    }

    @TabFactory("Content")
    public void addDialog(TabBuilder tab) {
        tab.addEdit("title","Title","")
        tab.addFckEditor("body", "Text", "");
    }
}
