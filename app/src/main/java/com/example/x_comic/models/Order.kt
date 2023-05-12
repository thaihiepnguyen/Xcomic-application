package com.example.x_comic.models

class Order:java.io.Serializable{
    var id: String = ""
    var uid: String = ""
    var bid: String = ""
    var cid: String = ""
    var time: String = ""
    var cost: Double = 0.0

    constructor() {}

    constructor(id: String, uid: String, bid: String, cid: String, cost: Double, time: String) {
        this.id = id
        this.uid = uid
        this.bid = bid
        this.cid = cid
        this.cost = cost
        this.time = time
    }
}