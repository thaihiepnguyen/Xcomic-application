package com.example.x_comic.views.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityEditProfileBinding
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.login.LoginActivity

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContentView(binding.root)

        binding.logout.setOnClickListener {
            // đăng xuất khỏi firebase
            FirebaseAuthManager.auth.signOut();
            nextLoginActivity();
        }

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.passwordLayout.setOnClickListener {
            nextResetPasswordActivity()
        }

        binding.fullnameLayout.setOnClickListener {
            nextChangeUsernameActivity()
        }

        binding.genderLayout.setOnClickListener {
            nextChangeGenderActivity()
        }

        binding.ageLayout.setOnClickListener {
            nextChangeAgeActivity()
        }

        binding.pennameLayout.setOnClickListener {
            nextChangePennameActivity()
        }

        binding.phoneLayout.setOnClickListener {
            nextChangePhoneNumberActivity()
        }
        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.callApi(uid)
                .observe(this, Observer {
                        user ->
                    run {
                        binding.user = user
                    }
                })
        }
    }

    private fun nextChangePhoneNumberActivity() {
        val intent = Intent(this, ChangePhoneNumberActivity::class.java)
        startActivity(intent)
    }

    private fun nextLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun nextResetPasswordActivity() {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }
    private fun nextChangeUsernameActivity() {
        val intent = Intent(this, ChangeUsernameActivity::class.java)
        startActivity(intent)
    }
    private fun nextChangeGenderActivity() {
        val intent = Intent(this, ChangeGenderActivity::class.java)
        startActivity(intent)
    }

    private fun nextChangeAgeActivity() {
        val intent = Intent(this, ChangeAgeActivity::class.java)
        startActivity(intent)
    }

    private fun nextChangePennameActivity() {
        val intent = Intent(this, ChangePennameActivity::class.java)
        startActivity(intent)
    }
}