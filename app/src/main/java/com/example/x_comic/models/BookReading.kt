package com.example.x_comic.models

class BookReading  {
    var book: Product? = null
    var current: Int = 1
    var chapter: Int =1

    constructor() {}

    constructor( book: Product,  current: Int,  chapter: Int) {
        this.book = book;
        this.current = current;
        this.chapter = chapter;
    }
}