package com.example.x_comic.models

class Product : Cloneable {
    var id: String = ""
    var title: String = ""
    var cover: String = ""
    var status: Boolean = false
    var tiny_des: String = ""
    var author: String = ""
    var view: Long = 0
    var rating: Long = 0
    var hide: Boolean = false
    var categories = ArrayList<String>()
    var chapters = ArrayList<Episode>()
    constructor(
        title: String = "",
        cover: String = "",
        status: Boolean = false,
        tiny_des: String = "",
        author: String = "",
        view: Long = 0,
        rating: Long = 0,
        hide: Boolean = false,
        categories: ArrayList<String>,
        chapters: ArrayList<Episode>
    ) {
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

//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "id" to id,
//            "author" to author,
//            "title" to title,
//            "cover" to cover,
//            "status" to status,
//            "tiny_des" to tiny_des,
//            "view" to view,
//            "rating" to rating,
//            "hide" to hide,
//            "categories" to categories,
//            "chapters" to chapters
//        )
//    }
}