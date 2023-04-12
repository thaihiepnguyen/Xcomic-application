package com.example.x_comic.models

class Product {
    var id: Long = 0
    var title: String = ""
    var cover: Long = 0
    var status: Boolean = false
    var tiny_des: String = ""
    var author: String = ""
    var view: Long = 0
    var rating: Double = 0.0
    var hide: Boolean = false
    var age: Long = 0
    var categories = ArrayList<Category>()
    var chapters = ArrayList<Episode>()
    constructor(
        id: Long = 0,
        title: String = "",
        cover: Long = 0,
        status: Boolean = false,
        tiny_des: String = "",
        author: String = "",
        view: Long = 0,
        rating: Double = 0.0,
        hide: Boolean = false,
        age: Long = 0,
        categories: ArrayList<Category>,
        chapters: ArrayList<Episode>
    ) {
        this.id = id
        this.title = title
        this.age = age
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