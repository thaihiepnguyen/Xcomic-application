package com.example.x_comic.views.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.AvatarListAdapter
import com.example.x_comic.adapters.ListAdapterSlideshow
import com.example.x_comic.databinding.ActivityMainProfileBinding
import com.example.x_comic.databinding.ActivityProfileBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.IOException

class MainProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    val bookList: MutableList<Product> = mutableListOf()
    val followList: MutableList<User> = mutableListOf()
    private var REQUEST_CODE_PICK_IMAGE = 1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)


        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.settingImg.setOnClickListener {
            nextSettingActivity()
        }


        binding.FavoriteListView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.profileListView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val favoriteAdapter = ListAdapterSlideshow(this, bookList)
        val avatarAdapter = AvatarListAdapter(this, followList)
        binding.FavoriteListView.adapter = favoriteAdapter
        binding.profileListView.adapter = avatarAdapter

//        userViewModel.getAllAuthor()
//            .observe(this, Observer { authors ->
//                run {
//
//                }
//            })

        productViewModel.getLatestBook {
            products -> run {
                bookList.clear()
                for (book in products.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null) {
                        bookList.add(product)
                    }
                }
                favoriteAdapter.notifyDataSetChanged()
            }
        }

        binding.avtImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.getAllUserIsFollowed(uid) {
                usersID -> run {
                    followList.clear()
                    for (userid in usersID.children) {
                        userViewModel.getUserById(userid.value as String) {
                            user -> run {
                                followList.add(user)
                                Log.d("TESTING 1", user.full_name)
                                avatarAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }


            userViewModel.callApi(uid)
                .observe(this, Observer {
                    // user nó được thay đổi realtime ở đb
                        user ->
                    run {
                        binding.emailTV.text = user.email
                        binding.usernameTV.text = user.full_name
                        binding.aboutme.text = user.aboutme

                        if (user.avatar != "") {
                            Glide.with(binding.avtImg.context)
                                .load(user.avatar)
                                .apply(RequestOptions().override(100, 100))
                                .circleCrop()
                                .into(binding.avtImg)

                            Glide.with(binding.background.context)
                                .load(user.avatar)
                                .apply(RequestOptions.bitmapTransform(BlurTransformation(30, 3)))
                                .into(binding.background)
                        } else {
                            // nếu mà chưa có avt thì mặc định render vầy :v
                            Glide.with(binding.avtImg.context)
                                .load(R.drawable.avatar)
                                .apply(RequestOptions().override(100, 100))
                                .circleCrop()
                                .into(binding.avtImg)
                        }

                        binding.followNumber.text = user.have_followed.size.toString()
                    }
                })
        }
        if (uid != null) {
            userViewModel.getAvt(uid, binding.avtImg, binding.background)
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
                // upload avt lên storage
                var uploadTask = userViewModel.uploadAvt(uid, bitmap)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()

                        // change avt ở realtime db
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

    private fun nextSettingActivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }
}