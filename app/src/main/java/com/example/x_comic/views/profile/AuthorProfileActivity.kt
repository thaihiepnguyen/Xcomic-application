package com.example.x_comic.views.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.ListAdapterSlideshow
import com.example.x_comic.databinding.ActivityAuthorProfileBinding
import com.example.x_comic.databinding.ActivityMainProfileBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class AuthorProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    val bookList: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        binding.backBtn.setOnClickListener {
            finish()
        }

        var author = intent.getStringExtra("authorKey")?.let {
            Klaxon().parse<User>(it)
        }

        binding.emailTV.text = author!!.email
        binding.usernameTV.text = author!!.full_name
        binding.aboutme.text = author!!.aboutme

        if (author.avatar != "") {
            Glide.with(binding.avtImg.context)
                .load(author.avatar)
                .apply(RequestOptions().override(100, 100))
                .circleCrop()
                .into(binding.avtImg)

            Glide.with(binding.background.context)
                .load(author.avatar)
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

        binding.listView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        productViewModel.getLatestBook().observe(this, Observer {
                products -> run {
            bookList.clear()
            bookList.addAll(products)
            val adapter = ListAdapterSlideshow(this, bookList);

            binding.listView.adapter = adapter
        }
        })
    }
}