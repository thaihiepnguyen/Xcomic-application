package com.example.x_comic.views.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityChangePennameBinding
import com.example.x_comic.databinding.ActivityChangeUsernameBinding
import com.example.x_comic.viewmodels.UserViewModel

class ChangePennameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePennameBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePennameBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.savePenname.setOnClickListener {
            val newPenname = binding.pennameET.text.toString().trim()
            userViewModel.changePenname(newPenname)
            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your penname was changed.")
                .setPositiveButton("OK") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}