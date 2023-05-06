package com.example.x_comic.models;

class Reading :java.io.Serializable{
    var id: String = ""
    var id_user: String = ""
    var id_book: String = ""
    var id_chapter: String = ""
    var posChap : Int = 0
    var numChap : Int = 0

    constructor() {}

    constructor(id: String, id_user: String, id_book: String, id_chapter: String, posChap : Int, numChap: Int) {
        this.id = id
        this.id_user = id_user
        this.id_book = id_book
        this.id_chapter = id_chapter
        this.posChap = posChap
        this.numChap = numChap
    }
}