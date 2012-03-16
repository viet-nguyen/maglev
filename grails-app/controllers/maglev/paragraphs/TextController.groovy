package maglev.paragraphs

import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder

/**
 * Simple paragraph for adding text to a page.
 */
//@Paragraph("Text")
//@ParagraphDescription("Simple text block")
public class TextController{

    def index = {
        def x = 10;
        x += 20
        //log.info("test log")
        render(view: "text")
    }

    @TabFactory("Content")
    public void addDialog(TabBuilder tab) {
        tab.addFckEditor("body", "Text", "");
    }
}
