package com.example.x_comic.views.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityChangeAgeBinding
import com.example.x_comic.databinding.ActivityChangeGenderBinding
import com.example.x_comic.viewmodels.UserViewModel

class ChangeAgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeAgeBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding = ActivityChangeAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.saveAge.setOnClickListener {
            val newAge = binding.ageET.text.toString().trim()
            userViewModel.changeAge(newAge.toLong())
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your age was changed.")
                .setPositiveButton("OK") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}