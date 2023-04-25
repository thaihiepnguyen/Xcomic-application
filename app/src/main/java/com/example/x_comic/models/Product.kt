package com.example.x_comic.models

import android.util.Log

class Product : java.io.Serializable {
    var id: String = ""
    var title: String = ""
    var cover: String = ""
    var status: Boolean = false
    var tiny_des: String = ""
    var author: String = ""
    var view: Long = 0
    var favorite: Long = 0
    var age: Int = 0
    var rating: Double = 0.0
    var hide: Boolean = false
    var categories = ArrayList<Category>()
    var chapters = ArrayList<Chapter>()
    constructor(
        id: String = "",
        title: String = "",
        cover: String = "",
        status: Boolean = false,
        tiny_des: String = "",
        author: String = "",
        view: Long = 0,
        favorite: Long = 0,
        age: Int = 0,
        rating: Double = 0.0,
        hide: Boolean = false,
        categories: ArrayList<Category>,
        chapters: ArrayList<Chapter>
    ) {
        this.id = id
        this.title = title
        this.cover = cover
        this.status = status
        this.tiny_des = tiny_des
        this.author = author
        this.view = view
        this.favorite = favorite
        this.age = age
        this.hide = hide
        this.rating = rating
        this.categories = categories
        this.chapters = chapters
    }
    constructor() {}
        companion object {
            const val MESSAGE1 = "message1"
            const val MESSAGE2 = "message2"

        }
    }


