package com.example.x_comic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {
    private val firebaseAuthManager = FirebaseAuthManager()

    fun login(email: String, password: String): LiveData<Boolean> {
        return firebaseAuthManager.login(email, password)
    }
}