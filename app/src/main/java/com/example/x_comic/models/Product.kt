package com.example.x_comic.models

import android.util.Log

class Product {
    var id: String = ""
    var title: String = ""
    var cover: String = ""
    var status: Boolean = false
    var tiny_des: String = ""
    var author: String = ""
    var view: Long = 0
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
        this.age = age
        this.hide = hide
        this.rating = rating
        this.categories = categories
        this.chapters = chapters
    }
    constructor() {}

    override fun toString(): String {
        val categoriesString = categories.joinToString("|") { it.toString() }
        val chaptersString = chapters.joinToString("|") { it.toString() }
        return "$id<Product/>$title<Product/>$cover<Product/>$status<Product/>$tiny_des<Product/>$author<Product/>$view<Product/>$age<Product/>$rating<Product/>$hide<Product/>[$categoriesString]<Product/>[$chaptersString]"
    }



        companion object {
            fun fromString(string: String): Product {
                val parts = string.split("<Product/>")
                val id = parts[0]
                val title = parts[1]
                val cover = parts[2]
                val status = parts[3].toBoolean()
                val tiny_des = parts[4]
                val author = parts[5]
                val view = parts[6].toLong()
                val age = parts[7].toInt()
                val rating = parts[8].toDouble()
                val hide = parts[9].toBoolean()

                val categoriesString = parts[10].removeSurrounding("[", "]")

                val categories = categoriesString.split("|")
                val cate_list = ArrayList<Category>()
                for (cate in categories){
                    cate_list.add(Category.fromString(cate))
                }
                val chaptersString = parts[11].removeSurrounding("[", "]")
                val chapters = chaptersString.split("|")
                val chap_list = ArrayList<Chapter>()
                for (chap in chapters){
                    chap_list.add(Chapter.fromString(chap))
                }
                return Product(id, title, cover, status, tiny_des, author, view, age, rating, hide, cate_list, chap_list)
            }
        }
    }


