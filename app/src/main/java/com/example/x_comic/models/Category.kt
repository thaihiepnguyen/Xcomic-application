package com.example.x_comic.models

class Category {
    var id: String = ""
    var name: String = ""

    constructor() {}
    constructor(id: String, name: String)
    {
        this.id = id
        this.name = name
    }

}