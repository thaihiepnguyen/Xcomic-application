package com.example.x_comic.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }

        return result
    }
}