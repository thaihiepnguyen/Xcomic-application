package com.example.x_comic.models

class Notification:java.io.Serializable{
    var title: String = ""
    var message: String = ""
    var total: String = ""

    constructor() {}

    constructor(title: String, message: String, total: String) {
        this.title = title
        this.message = message
        this.total = total

    }
}