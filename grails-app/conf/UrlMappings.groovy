class UrlMappings {

    static excludes = [
    "/test*"
    ]

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/testvy"(view: "/error")
        "404"(view: '/error')
    }

}
