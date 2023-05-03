package com.example.x_comic.models

class Filter: java.io.Serializable {
    var listCate = ArrayList<Category>()
    var chapterRange:String = ""
    var bookStatus: String = ""
    constructor( listCate: ArrayList<Category>,chapterRange:String, bookStatus: String){
        this.listCate = listCate
        this.chapterRange = chapterRange
        this.bookStatus = bookStatus
    }
    constructor(){

    }
    companion object{
        fun applyFilter(filter: Filter, input: ArrayList<Product>): ArrayList<Product> {
            return input.filter { product ->
                // Check if the product has all the categories in the filter
                filter.listCate.all { filterCategory ->
                    product.categories.any { productCategory ->
                        productCategory.name == filterCategory.name
                    }
                } &&
                        when (filter.chapterRange) {
                            "50+" -> product.chapters.size > 50
                            "20-50" -> product.chapters.size in 20..50
                            "10-20" -> product.chapters.size in 10..20
                            "1-10" -> product.chapters.size in 1..10
                            else -> true
                        }
                        && when (filter.bookStatus){
                            "Ongoing" -> product.status == false
                            "Completed" -> product.status == true
                            else ->true
                        }
            } as ArrayList<Product>
        }


    }
}