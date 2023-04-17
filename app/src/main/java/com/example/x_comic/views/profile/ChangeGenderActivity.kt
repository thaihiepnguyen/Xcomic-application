package com.example.x_comic.views.profile

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityChangeGenderBinding
import com.example.x_comic.viewmodels.UserViewModel


class ChangeGenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeGenderBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding = ActivityChangeGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item, arrayOf("Male", "Female", "Other")
        )

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.backImg.setOnClickListener {
            finish()
        }
        binding.saveGender.setOnClickListener {
            var selectedGender = binding.spinner.selectedItem

            userViewModel.changeGender(selectedGender as String)

            AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your gender was changed.")
                .setPositiveButton("OK") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}