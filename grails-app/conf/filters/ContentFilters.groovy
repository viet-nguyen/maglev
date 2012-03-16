import info.magnolia.context.MgnlContext
import info.magnolia.jcr.util.ContentMap
import info.magnolia.module.blossom.render.RenderContext

/**
 * @author Åke Argéus
 */
class ContentFilters {

    def filters = {
        contentFilter(uri: '/**') {
            after = { model ->
                if (MgnlContext.isWebContext()) {
                    if (MgnlContext.aggregationState) {
                        model.put("state",MgnlContext.aggregationState)
                        if (MgnlContext.aggregationState.currentContent) {
                            model.put("content", new ContentMap((javax.jcr.Node) MgnlContext.aggregationState.currentContent.JCRNode))

                            if(RenderContext.get()){
                                model.putAll(RenderContext.get().contextObjects)
                            }
                        }
                    }
                }
            }
        }
    }
}
