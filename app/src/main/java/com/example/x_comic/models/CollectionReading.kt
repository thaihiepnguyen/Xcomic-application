package com.example.x_comic.models

class CollectionReading: java.io.Serializable {

    var name: String= ""
    var bookList: MutableList<String> = mutableListOf()

    constructor() {}

    constructor( name: String, bookList: MutableList<String>) {
       this.name = name;
        this.bookList = bookList;
    }
}