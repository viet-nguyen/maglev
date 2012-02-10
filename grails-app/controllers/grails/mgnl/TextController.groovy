package grails.mgnl

import info.magnolia.module.blossom.annotation.Paragraph
import info.magnolia.module.blossom.annotation.ParagraphDescription
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder

/**
 * Simple paragraph for adding text to a page.
 */
@Paragraph("Text")
@ParagraphDescription("Simple text block")
public class TextController extends BaseParagraph {

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
