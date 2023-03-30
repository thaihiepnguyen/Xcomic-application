package com.example.x_comic.models

data class User (
    var id: String,
    var full_name: String,
    var age: Long,
    var avatar: String,
    var bio: String,
    var dob: String,
    var email: String,
    var follow: Long,
    var is_hide: Boolean,
    var penname: String,
    var phone: String,
    var gender: String,
    var role: Long, // Role = 1: là đọc giả; 2: là người đọc giả có thể đăng truyện; 3: là admin
        )

{
}