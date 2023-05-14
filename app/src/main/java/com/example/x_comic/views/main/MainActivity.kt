package com.example.x_comic.views.main

import android.content.ContentValues.TAG
import android.content.Intent
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
import com.example.x_comic.views.login.LoginActivity
import com.example.x_comic.views.main.fragments.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    override fun onStart() {
        super.onStart()
        val val1 = intent.getStringExtra("key1")
        val extraData = intent.extras
        Log.d(TAG, val1.toString())
        Log.d(TAG, extraData.toString())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "msg_subscribed"
                if (!task.isSuccessful) {
                    msg = "msg_subscribe_failed"
                }
                Log.d(TAG, msg)
            }

        val config = CheckoutConfig(
            application = application,
            clientId = "AetqiuaX2ZoHcRciOFWmqHOGSUcasvGCdv0wqrqFsLYxezwE_f2mvjaWrR0nwmep-nSPp_b_CITJitd7",
            environment = Environment.SANDBOX,
            returnUrl = "com.example.xcomic://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)
        if (FirebaseAuthManager.auth.currentUser == null) {
            nextLoginActivity()
            return
        }
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
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
    private fun nextLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}