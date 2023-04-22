package com.example.x_comic.views.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.databinding.ActivityEditProfileBinding
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.login.LoginActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.IOException

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private var REQUEST_CODE_PICK_IMAGE = 1112
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

        binding.avtLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.callApi(uid)
                .observe(this, Observer {
                        user ->
                    run {
                        binding.user = user
                        Glide.with(binding.avtImg.context)
                            .load(user.avatar)
                            .apply(RequestOptions().override(100, 100))
                            .circleCrop()
                            .into(binding.avtImg)
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            // Lưu ảnh vào profile
            saveImageToProfile(imageUri)
        }
    }

    private fun saveImageToProfile(imageUri: Uri?) {
        try {
            val uid = FirebaseAuthManager.auth.uid
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            if (uid != null) {
                var uploadTask = userViewModel.uploadAvt(uid, bitmap)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        userViewModel.changeAvt(downloadUrl)
                    }
                }.addOnFailureListener { exception ->
                    // Tải lên ảnh thất bại
                    exception.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
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