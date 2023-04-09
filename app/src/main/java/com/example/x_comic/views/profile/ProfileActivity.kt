package com.example.x_comic.views.profile

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.x_comic.databinding.ActivityProfileBinding
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.main.MainActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            nextMainActivity()
        }

        UserViewModel.updateUI(binding)
    }

    private fun nextMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}