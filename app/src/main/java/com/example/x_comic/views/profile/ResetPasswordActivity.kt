package com.example.x_comic.views.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.x_comic.R
import com.example.x_comic.databinding.ActivityEditProfileBinding
import com.example.x_comic.databinding.ActivityResetPasswordBinding
import com.example.x_comic.viewmodels.FirebaseAuthManager

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.savePassword.setOnClickListener {
            var newPassword = binding.passwordET.text.toString().trim()

            var currentUser = FirebaseAuthManager.getUser()

            currentUser?.updatePassword(newPassword)
                ?.addOnCompleteListener {
                    task ->
                        run {
                        if (task.isSuccessful) {
                            AlertDialog.Builder(this)
                                .setTitle("Success")
                                .setMessage("Your password was changed.")
                                .setPositiveButton("OK") { _, _ ->
                                    finish()
                                }
                                .show()
                        } else {
                            val errorMessage = task.exception?.message ?: "An error occurred while changing the password."
                            AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage(errorMessage)
                                .setPositiveButton("OK", null)
                                .show()
                        }
                    }
                }
        }
    }
}