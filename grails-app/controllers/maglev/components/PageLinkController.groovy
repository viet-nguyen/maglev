package maglev.components

import com.altaworks.magnolia.MagnoliaUtils
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder
import info.magnolia.module.blossom.annotation.Template
import info.magnolia.module.blossom.annotation.TemplateDescription

@Template(id = "grailsModule:components/pageLink", title = "Page link")
@TemplateDescription("Simple link to a page")
public class PageLinkController{

    def index = {
        Map model = ['url': MagnoliaUtils.getUrlFromUuidNodeData("link")]
        render(view: 'index', model: model)
    }

    @TabFactory("Content")
    void addDialog(TabBuilder tab) {
        tab.addUuidLink("link", "Link to page", "Link to page")
    }

}
