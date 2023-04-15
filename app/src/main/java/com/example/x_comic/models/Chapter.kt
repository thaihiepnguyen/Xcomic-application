package com.example.x_comic.models

import java.util.*

class Chapter {
    var id: String? = ""
    var name: String = ""
    var _lock: Boolean = false
    var date_update: String = "2023"
    var content: String = "This is the content of chapter"

    constructor(name: String) {
        this.name = name
        this._lock = false
        this.date_update = "2023"
        this.content = "This is the content of chapter"
    }
}