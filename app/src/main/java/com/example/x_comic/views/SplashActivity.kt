package com.example.x_comic.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beust.klaxon.Klaxon
import com.example.x_comic.R
import com.example.x_comic.databinding.ActivityLoginBinding
import com.example.x_comic.databinding.LayoutDialogSendpassBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.*
import com.example.x_comic.views.admin.HomeActivity
import com.example.x_comic.views.login.LoginActivity
import com.example.x_comic.views.main.MainActivity
import com.example.x_comic.views.profile.BlockUserActivity
import com.example.x_comic.views.signup.SignupActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        var sharedPreferences: SharedPreferences = getSharedPreferences("MySharedPreferences02032002", MODE_PRIVATE)
        _sharedPreferences = sharedPreferences


        // TODO: Lúc này người dùng đã đăng nhập rồi
        Handler().postDelayed({
            if (FirebaseAuthManager.auth.currentUser != null) {
                // Show Man hinh Block nhung ma chua hop ly lam
                var id_user = FirebaseAuthManager.auth.uid
                id_user?.let {
                    userViewModel.getUserById(id_user) { user ->
                        kotlin.run {
                            if (user.hide)
                                nextBlockUserActivity()
                            else {
                                if (sharedPreferences.contains("currentRole")) {
                                    val currentRole = sharedPreferences.getLong("currentRole", 1)
                                    // parse có lỗi gì thì mặc định vô main
                                    if (currentRole.compareTo(3) == 0) {
                                        nextHomeActivity()
                                    } else {
                                        nextMainActivity()
                                    }
                                } else {
                                    // lỡ đâu code lỗi gì thì vô main; tránh crash
                                    // hoặc đăng nhập bằng gg thì vô main; chắc chắn không phải là admin
                                    nextMainActivity()
                                }
                            }
                        }
                    }
                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        },3000)
    }


        private lateinit var userViewModel: UserViewModel
        private var RC_SIGN_IN = 123321
        private lateinit var mClient: GoogleSignInClient
        private val _users: MutableList<User> = mutableListOf()
        private lateinit var _sharedPreferences: SharedPreferences





        private fun nextMainActivity() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        private fun nextHomeActivity() {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        private fun nextSignupActivity() {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        private fun nextBlockUserActivity() {
            val intent = Intent(this, BlockUserActivity::class.java)
            startActivity(intent)
        }

}
