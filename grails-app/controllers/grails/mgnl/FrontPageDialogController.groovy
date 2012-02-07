package grails.mgnl

import info.magnolia.module.blossom.annotation.TabOrder
import info.magnolia.module.blossom.annotation.DialogFactory
import info.magnolia.module.blossom.annotation.TabFactory
import info.magnolia.module.blossom.dialog.TabBuilder

@DialogFactory("front-page-dialog")
@TabOrder(["Content", "Margins"])
class FrontPageDialogController {

	@TabFactory("Margins")
	public void margins(TabBuilder tab) {
		tab.addStatic("Margins around the side of the front page");
		tab.addEdit("leftMargin", "Left Margin", "Left margin in pixels");
		tab.addEdit("rightMargin", "Right Margin", "Right margin in pixels");
	}

	@TabFactory("Content")
	public void content(TabBuilder tab) {
		tab.addEdit("title", "Title", "The title of this page from new dialog");
	}


	def index = { }
}
