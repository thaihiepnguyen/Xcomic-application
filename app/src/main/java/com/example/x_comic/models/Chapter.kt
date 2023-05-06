package com.example.x_comic.models

import java.util.*

class Chapter : java.io.Serializable {
    // văn xóa id ở đây bị bug á. phải đồng bộ tên với trên fbase
    var id: String = ""
    var id_book: String = ""
    var id_chapter: String? = ""
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
        return "$id_book<Chap/>$id_chapter<Chap/>$name<Chap/>$_lock<Chap/>$date_update<Chap/>$content"
    }

    companion object {
        const val MESSAGE1 = "message1"
        const val MESSAGE2 = "message2"
        const val MESSAGE3 = "message3"
        const val MESSAGE4 = "message4"
        const val MESSAGE5 = "message5"
        fun fromString(string: String): Chapter {
            val parts = string.split("<Chap/>")
            val chapter = Chapter(parts[2])
            chapter.id_book = parts[0]
            chapter.id_chapter = parts[1]
            chapter._lock = parts[3].toBoolean()
            chapter.date_update = parts[4]
            chapter.content = parts[5]
            return chapter
        }
    }
}
