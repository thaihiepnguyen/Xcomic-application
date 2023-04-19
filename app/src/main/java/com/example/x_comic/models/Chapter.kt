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

    constructor() {}

    override fun toString(): String {
        return "$id;$name;$_lock;$date_update;$content"
    }

    companion object {
        fun fromString(string: String): Chapter {
            val parts = string.split(";")
            val chapter = Chapter(parts[1])
            chapter.id = parts[0]
            chapter._lock = parts[2].toBoolean()
            chapter.date_update = parts[3]
            chapter.content = parts[4]
            return chapter
        }
    }
}
