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
import com.example.x_comic.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
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

    fun uploadAvt(userID: String,bitmap: Bitmap, imgAvt: ImageView){
        val storage = FirebaseStorage.getInstance()
        val fileName = "${userID}.png"
        val storageRef = storage.reference.child("users/$fileName")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                Glide.with(imgAvt.context)
                    .load(downloadUrl)
                    .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(150)))
                    .into(imgAvt)
            }


        }.addOnFailureListener { exception ->
            // Tải lên ảnh thất bại
            exception.printStackTrace()
        }
    }

    fun getAvt(userID: String, imgAvt: ImageView) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("users/$userID.png")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val downloadUrl = uri.toString()
            Glide.with(imgAvt.context)
                .load(downloadUrl)
                .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(150)))
                .into(imgAvt)
        }.addOnFailureListener {

        }
    }

    fun getAvtForImageButton(userID: String, imgAvt: ImageButton) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("users/$userID.png")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val downloadUrl = uri.toString()
            Glide.with(imgAvt.context)
                .load(downloadUrl)
                .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(150)))
                .into(imgAvt)
        }
    }
    // add
    fun addUser(user: User) {
        val database = Firebase.database
        database.reference
            .child("users")
            .child(user.id)
            .setValue(user)
    }

    fun changeUsername(username: String) {
        var currentUser = FirebaseAuthManager.getUser()
        val database = Firebase.database
        if (currentUser != null) {
            database.reference
                .child("users")
                .child(currentUser.uid)
                .child("full_name")
                .setValue(username)
        }
    }

    fun changePenname(penname: String) {
        var currentUser = FirebaseAuthManager.getUser()
        val database = Firebase.database
        if (currentUser != null) {
            database.reference
                .child("users")
                .child(currentUser.uid)
                .child("penname")
                .setValue(penname)
        }
    }

    fun changePhone(phone: String) {
        var currentUser = FirebaseAuthManager.getUser()
        val database = Firebase.database
        if (currentUser != null) {
            database.reference
                .child("users")
                .child(currentUser.uid)
                .child("phone")
                .setValue(phone)
        }
    }

    fun changeAge(age: Long) {
        var currentUser = FirebaseAuthManager.getUser()
        val database = Firebase.database
        if (currentUser != null) {
            database.reference
                .child("users")
                .child(currentUser.uid)
                .child("age")
                .setValue(age)
        }
    }

    fun changeGender(gender: String) {
        var currentUser = FirebaseAuthManager.getUser()
        val database = Firebase.database
        if (currentUser != null) {
            database.reference
                .child("users")
                .child(currentUser.uid)
                .child("gender")
                .setValue(gender)
        }
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