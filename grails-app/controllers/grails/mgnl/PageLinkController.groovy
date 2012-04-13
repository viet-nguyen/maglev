package grails.mgnl

import com.altaworks.magnolia.MagnoliaUtils
import info.magnolia.module.blossom.annotation.Paragraph
import info.magnolia.module.blossom.annotation.ParagraphDescription
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder

@Paragraph("PageLink")
@ParagraphDescription("PageLink Desc")
public class PageLinkController {

    def index = {
        Map model = ['url': MagnoliaUtils.getUrlFromUuidNodeData("link")]
        render(view: 'index', model: model)
    }

    @TabFactory("Content")
    void addDialog(TabBuilder tab) {
        tab.addUuidLink("link", "Link to page", "Link to page")
    }

}
