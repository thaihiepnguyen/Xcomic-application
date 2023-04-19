package com.example.x_comic.models

class Category {
    var id: String = ""
    var name: String = ""

    constructor() {}

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    override fun toString(): String {
        return "$id;$name"
    }

    companion object {
        fun fromString(string: String): Category {
            val parts = string.split(";")
            return Category(parts[0], parts[1])
        }
    }
}
