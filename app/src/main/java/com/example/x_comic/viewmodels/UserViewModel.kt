package com.example.x_comic.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.x_comic.databinding.ActivityProfileBinding
import com.example.x_comic.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserViewModel {
    companion object {
        var user = User()
        var ref: DatabaseReference? = null
        fun updateUI(binding: ActivityProfileBinding) {
            ref?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    user = dataSnapshot.getValue(User::class.java)!!
                    binding.user = user
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                }
            })
        }
    }

    fun setUser(uid: String) : UserViewModel {
        val database = Firebase.database // mẫu singleton design p
        val ref = database.reference.child("users").child(uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
            }
            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
            }
        })
        UserViewModel.ref = ref
        return this
    }

    fun addUser(user: User) {
        val database = Firebase.database
        database.reference
            .child("users")
            .child(user.id)
            .setValue(user)
    }

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