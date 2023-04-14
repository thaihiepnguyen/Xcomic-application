package com.example.x_comic.models

import java.util.Date

class Episode {
    companion object {
        var id :Long = 0
    }
    var id: Long = 0
    var is_lock: Boolean = false
    var content: String = ""
    var date_update: String = ""
    var name: String = ""

    constructor(
        is_lock: Boolean = false,
        content: String = "",
        date_update: String = "",
        name: String = ""
        ) {
        this.id = Episode.id
        this.is_lock = is_lock
        this.content = content
        this.date_update = date_update
        this.name = name

        Episode.id++
    }

    constructor() {}
}