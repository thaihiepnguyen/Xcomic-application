package com.example.x_comic.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.x_comic.databinding.ActivityProfileBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.net.UnknownServiceException

class UserViewModel : ViewModel() {
    private val database = Firebase.database
    val db = database.getReference("users")

    private val _user = MutableLiveData<User>()
    // TODO: biến này để truyền sang Activity khác
    val userLiveData: LiveData<User>
        get() = _user

    // Hàm này sẽ chạy ở thread khác
    fun callApi(uid: String) : MutableLiveData<User> {
        if (_user.value == null) {
            val ref = db.child(uid)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var user: User = dataSnapshot.getValue(User::class.java)!!
                    _user.value = user
                    _user.postValue(user)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // TODO: Xử lý lỗi, bỏ thread đi
                    db.removeEventListener(this)
                }
            })
        }

        return _user
    }

    // add
    fun addUser(user: User) {
        val database = Firebase.database
        database.reference
            .child("users")
            .child(user.id)
            .setValue(user)
    }

    // ton
    fun isExist(uid: String, callback: (Boolean) -> Unit) {
        val database = Firebase.database
        val ref = database.reference.child("users").child(uid)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }
}