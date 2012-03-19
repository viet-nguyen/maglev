class UrlMappings {

    static excludes = [
            "/test*"
    ]

    static mappings = {
        "/g/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/css/*"(controller: "css")
        "/images/*"(controller: "images")
        "/js/*"(controller: "js")

        "500"(view: '/error')
    }

}
