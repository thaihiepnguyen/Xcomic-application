package com.example.x_comic.views.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityChangeUsernameBinding
import com.example.x_comic.databinding.ActivityResetPasswordBinding
import com.example.x_comic.viewmodels.UserViewModel

class ChangeUsernameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeUsernameBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeUsernameBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.saveUsername.setOnClickListener {
            val newUsername = binding.usernameET.text.toString().trim()
            userViewModel.changeUsername(newUsername)
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your username was changed.")
                .setPositiveButton("OK") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}