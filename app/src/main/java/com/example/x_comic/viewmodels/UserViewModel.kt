package com.example.x_comic.viewmodels

import android.graphics.Bitmap
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.models.Product
import com.example.x_comic.models.Reading
import com.example.x_comic.models.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.ByteArrayOutputStream
import java.net.UnknownServiceException

class UserViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app")
    val db = database.getReference("users")

    private val _user = MutableLiveData<User>()
    private val _authors = MutableLiveData<ArrayList<User>>()
    // TODO: biến này để truyền sang Activity khác
    val userLiveData: LiveData<User>
        get() = _user
    val authorLiveData: LiveData<ArrayList<User>>
        get() = _authors

    // Hàm này sẽ chạy ở thread khác
    fun callApi(uid: String) : MutableLiveData<User> {
        if (_user.value == null) {
            val ref = db.child(uid)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        var user: User = dataSnapshot.getValue(User::class.java)!!
                        _user.value = user
                        _user.postValue(user)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // TODO: Xử lý lỗi, bỏ thread đi
                    db.removeEventListener(this)
                }
            })
        }

        return _user
    }


    inline fun getAllUser(crossinline callback: (DataSnapshot)->Unit){
        // tạo thread mới.
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
                db.removeEventListener(this)
            }
        })
    }

    fun uploadAvt(userID: String,bitmap: Bitmap) : UploadTask {
        val storage = FirebaseStorage.getInstance()
        val fileName = "${userID}.png"
        val storageRef = storage.reference.child("users/$fileName")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)

        return uploadTask
    }

    fun getAvt(userID: String, imgAvt: ImageView, bg: ImageView) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("users/$userID.png")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val downloadUrl = uri.toString()

            changeAvt(downloadUrl)
            Glide.with(imgAvt.context)
                .load(downloadUrl)
                .apply(RequestOptions().override(100, 100))
                .circleCrop()
                .into(imgAvt)
            Glide.with(bg.context)
                .load(downloadUrl)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                .into(bg)
        }.addOnFailureListener {

        }
    }

    fun saveCurrentFollow(user: User) {
        db
            .child(user.id)
            .child("follow")
            .setValue(user.follow)
    }

    fun saveCurrentHaveFollowed(user: User) {
        db
            .child(user.id)
            .child("have_followed")
            .setValue(user.have_followed)
    }


    fun addUser(user: User) {
        db
            .child(user.id)
            .setValue(user)
    }

    inline fun getAllUserIsFollowed(uid: String, crossinline callback: (DataSnapshot)->Unit) {
        db.child(uid).child("follow").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(task.result)
            } else {

            }
        }
    }

    inline fun getAllUserFollow(uid: String, crossinline callback: (DataSnapshot)->Unit) {
        db.child(uid).child("have_followed").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    inline fun getUserById(uid: String, crossinline callback: (User)->Unit) {
        val ref = db.child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user: User = dataSnapshot.getValue(User::class.java)!!
                callback(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // TODO: Xử lý lỗi, bỏ thread đi
                db.removeEventListener(this)
            }
        })
    }

    fun getAllAuthor() : MutableLiveData<ArrayList<User>> {
        if (_authors.value == null) {
            // tạo thread mới.
            db.orderByChild("role").equalTo(2.0).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val authors = ArrayList<User>()

                    for (snapshot in dataSnapshot.children) {
                        val author = snapshot.getValue(User::class.java)
                        if (author != null) {
                            authors.add(author)
                        }
                    }
                    _authors.value = authors
                    _authors.postValue(authors)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                    db.removeEventListener(this)
                }
            })
        }
        return _authors
    }

    
    fun changeUsername(username: String) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("full_name")
                .setValue(username)
        }
    }

    fun changeAboutMe(aboutme: String) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("aboutme")
                .setValue(aboutme)
        }
    }

    fun changePenname(penname: String) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("penname")
                .setValue(penname)
        }
    }

    fun changePhone(phone: String) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("phone")
                .setValue(phone)
        }
    }

    fun changeAge(age: Long) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("age")
                .setValue(age)
        }
    }

    fun changeGender(gender: String) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("gender")
                .setValue(gender)
        }
    }

    fun changeAvt(avatar: String) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("avatar")
                .setValue(avatar)
        }
    }

    // ton
    fun isExist(uid: String, callback: (Boolean) -> Unit) {
        val ref = db.child(uid)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }

    fun saveHeartList(user: User) {
        db.child(user.id).child("heart_list")
            .setValue(user.heart_list)
    }

    fun saveCollection(user: User) {
        db.child(user.id).child("collection")
            .setValue(user.collection)
    }

    fun updateReadingUserList(readingList : ArrayList<Reading>) {
        var currentUser = FirebaseAuthManager.getUser()
        if (currentUser != null) {
            db
                .child(currentUser.uid)
                .child("reading")
                .setValue(readingList)
        }
    }
}