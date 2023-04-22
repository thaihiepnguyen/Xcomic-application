package com.example.x_comic.views.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.databinding.ActivityAboutMeBinding
import com.example.x_comic.databinding.ActivityChangeAgeBinding
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel

class AboutMeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutMeBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding = ActivityAboutMeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.save.setOnClickListener {
            userViewModel.changeAboutMe(binding.aboutmeET.text.toString())
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your about me was changed.")
                .setPositiveButton("OK") { _, _ ->
                    finish()
                }
                .show()
        }

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.callApi(uid)
                .observe(this, Observer {
                        user ->
                    run {
                        binding.aboutmeET.setText(user.aboutme)
                    }
                })
        }
    }
}