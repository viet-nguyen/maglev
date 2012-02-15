package maglev

import grails.converters.JSON

class JsonTestController {

    def index() {
        Map data = [data: "here is the data!"]
        render data as JSON
    }
}
