package com.example.x_comic.models

class User
{
    var id: String = ""
    var full_name: String = "no data yet"
    var age: Long = 0
    var avatar: String = ""
    var bio: String = "no data yet"
    var dob: String = "no data yet"
    var email: String = ""
    var follow: Long = 0
    var hide: Boolean = true
    var penname: String = "no data yet"
    var phone: String = "no data yet"
    var gender: String = "male"
    var aboutme: String = ""
    var role: Long = 1
    constructor(
        id: String = "",
        full_name: String = "no data yet",
        age: Long = 0,
        avatar: String = "",
        bio: String = "no data yet",
        dob: String = "no data yet",
        email: String = "",
        follow: Long = 0,
        hide: Boolean = true,
        penname: String = "no data yet",
        phone: String = "no data yet",
        gender: String = "male",
        aboutme: String = "",
        role: Long = 1, // Role = 1: là đọc giả; 2: là người đọc giả có thể đăng truyện; 3: là admin
    ) {
        this.id = id
        this.full_name = full_name
        this.age = age
        this.avatar = avatar
        this.bio = bio
        this.dob = dob
        this.email = email
        this.follow = follow
        this.hide = hide
        this.penname = penname
        this.phone = phone
        this.gender = gender
        this.role = role
        this.aboutme = aboutme
    }
    constructor() {

    }
}