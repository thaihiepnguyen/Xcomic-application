package com.example.x_comic.views.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.R
import com.example.x_comic.databinding.ActivityMainBinding
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.main.fragments.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.explore -> replaceFragment(Explore())
                R.id.library -> replaceFragment(Library())
                R.id.writing -> replaceFragment(Writing())
                R.id.notification -> replaceFragment(Notification())
                else -> {

                }
            }
            true
        }

        // TODO: Đăng ký với Observer
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)


        val uid = FirebaseAuthManager.auth.uid

        if (uid != null) {
            userViewModel.callApi(uid).observe(this, Observer {
                    user ->
//                TODO: Binding biến user ở đây vào UI
            })
        }

        productViewModel.productsLiveData.observe(this, Observer {
            products -> Log.d("PRODUCT 1", products[0].title)
//                TODO: Binding biến products ở đây vào UI
        })
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}