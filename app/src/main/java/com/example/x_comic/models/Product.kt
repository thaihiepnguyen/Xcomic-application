package com.example.x_comic.models

class Product {
    var id: String = ""
    var title: String = ""
    var cover: String = ""
    var status: Boolean = false
    var tiny_des: String = ""
    var author: String = ""
    var view: Long = 0
    var rating: Double = 0.0
    var hide: Boolean = false
    var categories = ArrayList<Long>()
    var chapters = ArrayList<HashMap<String, Episode>>()
    constructor(
        id: String = "",
        title: String = "",
        cover: String = "",
        status: Boolean = false,
        tiny_des: String = "",
        author: String = "",
        view: Long = 0,
        rating: Double = 0.0,
        hide: Boolean = false,
        categories: ArrayList<Long>,
        chapters: ArrayList<HashMap<String, Episode>>
    ) {
        this.id = id
        this.title = title
        this.cover = cover
        this.status = status
        this.tiny_des = tiny_des
        this.author = author
        this.view = view
        this.hide = hide
        this.rating = rating
        this.categories = categories
        this.chapters = chapters
    }
    constructor() {

    }
}