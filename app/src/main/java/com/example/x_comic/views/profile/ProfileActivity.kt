package com.example.x_comic.views.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.x_comic.databinding.ActivityProfileBinding
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userViewModel: UserViewModel
    val storage = FirebaseStorage.getInstance()
    var REQUEST_CODE_PICK_IMAGE = 1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.settingImg.setOnClickListener {
            nextSettingActivity()
        }

        binding.avtImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.callApi(uid)
                .observe(this, Observer {
                    // user nó được thay đổi realtime ở đb
                        user ->
                    run {
                        binding.user = user
                        if (user.role.toInt() == 1) {
                            binding.roleTV.text = "Member"
                        }
                    }

                })
        }

        if (uid != null) {
//            userViewModel.getAvt(uid, binding.avtImg)
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
//                userViewModel.uploadAvt(uid, bitmap, binding.avtImg, binding.)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun nextSettingActivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }
}