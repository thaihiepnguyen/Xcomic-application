package com.example.x_comic.views.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityChangePennameBinding
import com.example.x_comic.databinding.ActivityChangePhoneNumberBinding
import com.example.x_comic.viewmodels.UserViewModel

class ChangePhoneNumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePhoneNumberBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePhoneNumberBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }


        binding.savePhone.setOnClickListener {
            val newPhone = binding.phoneET.text.toString().trim()
            userViewModel.changePhone(newPhone)
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your phone was changed.")
                .setPositiveButton("OK") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}