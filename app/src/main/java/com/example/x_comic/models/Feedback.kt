package com.example.x_comic.models

class Feedback :java.io.Serializable{
    var id: String = ""
    var uid: String = ""
    var bid: String = ""
    var rating: Double = 0.0
    var feedback: String = ""

    constructor() {}

    constructor(id: String, uid: String, bid: String, rating: Double, feedback: String) {
        this.id = id
        this.uid = uid
        this.bid = bid
        this.rating = rating
        this.feedback = feedback
    }
}