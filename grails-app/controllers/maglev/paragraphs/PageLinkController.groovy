package maglev.paragraphs

import com.altaworks.magnolia.MagnoliaUtils
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder
import maglev.BaseParagraph

//@Paragraph("PageLink")
//@ParagraphDescription("PageLink Desc")
public class PageLinkController extends BaseParagraph {

    def index = {
        Map model = ['url': MagnoliaUtils.getUrlFromUuidNodeData("link")]
        render(view: 'index', model: model)
    }

    @TabFactory("Content")
    void addDialog(TabBuilder tab) {
        tab.addUuidLink("link", "Link to page", "Link to page")
    }

}
