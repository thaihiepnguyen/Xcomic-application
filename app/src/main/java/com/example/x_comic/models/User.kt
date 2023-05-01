package com.example.x_comic.models

class User : java.io.Serializable
{
    var id: String = ""
    var full_name: String = "no data yet"
    var age: Long = 0
    var avatar: String = ""
    var bio: String = "no data yet"
    var dob: String = "no data yet"
    var email: String = ""
    var hide: Boolean = true
    var penname: String = "no data yet"
    var phone: String = "no data yet"
    var gender: String = "male"
    var aboutme: String = ""
    var follow: ArrayList<String> = ArrayList()
    var have_followed: ArrayList<String> = ArrayList()
    var heart_list: ArrayList<String> = ArrayList()
    var collection: ArrayList<String> = ArrayList()
    var role: Long = 1
    constructor(
        id: String = "",
        full_name: String = "no data yet",
        age: Long = 0,
        avatar: String = "",
        bio: String = "no data yet",
        dob: String = "no data yet",
        email: String = "",
        hide: Boolean = true,
        penname: String = "no data yet",
        phone: String = "no data yet",
        gender: String = "male",
        aboutme: String = "",
        follow: ArrayList<String> = ArrayList(),
        have_followed: ArrayList<String> = ArrayList(),
        heart_list: ArrayList<String> = ArrayList(),
        collection: ArrayList<String> = ArrayList(),
        role: Long = 1, // Role = 1: là đọc giả; 2: là người đọc giả có thể đăng truyện; 3: là admin
    ) {
        this.id = id
        this.full_name = full_name
        this.age = age
        this.avatar = avatar
        this.bio = bio
        this.dob = dob
        this.email = email
        this.hide = hide
        this.penname = penname
        this.phone = phone
        this.gender = gender
        this.role = role
        this.follow = follow
        // is_followed nó bug :)))
        this.have_followed = have_followed
        this.heart_list = heart_list
        this.aboutme = aboutme
        this.collection = collection
    }

    fun addToReadingList(book: Product) {
        this.collection.add(book.id)
    }

    fun removeFromReadingList(book: Product) {
        var index: Int? = null

        for (i in 0 until this.collection.size) {
            if (this.collection[i] == book.id) {
                index = i
            }
        }

        if (index != null)
            this.collection.removeAt(index)
    }

    fun isExits(id: String): Boolean {
        return this.collection.contains(id)
    }

    fun love(book: Product) {
        this.heart_list.add(book.id)
    }

    fun unLove(book: Product) {
        var index: Int? = null

        for (i in 0 until this.heart_list.size) {
            if (this.heart_list[i] == book.id) {
                index = i
            }
        }

        if (index != null)
            this.heart_list.removeAt(index)
    }

    fun follow(other: User) {
        this.follow.add(other.id)
    }

    fun haveFollowed(other: User) {
        this.have_followed.add(other.id)
    }

    fun unHaveFollowed(other: User) {
        var index: Int? = null

        for (i in 0 until this.have_followed.size) {
            if (this.have_followed[i] == other.id) {
                index = i
            }
        }

        if (index != null)
            this.have_followed.removeAt(index)
    }


    fun unfollow(other: User) {
        var index: Int? = null

        for (i in 0 until this.follow.size) {
            if (this.follow[i] == other.id) {
                index = i
            }
        }

        if (index != null)
            this.follow.removeAt(index)
    }

    fun following(other: User): Boolean {
        return this.follow.contains(other.id)
    }

    fun isfollowed(id: String) : Boolean {
        return this.have_followed.contains(id)
    }

    fun isFollowing(id: String) : Boolean {
        return this.follow.contains(id)
    }



    constructor() {

    }
}