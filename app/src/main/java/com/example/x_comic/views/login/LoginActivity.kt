package com.example.x_comic.views.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityLoginBinding
import com.example.x_comic.viewmodels.LoginViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.main.MainActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val progressDialog = ProgressDialog(this)

        binding.loginBtn.setOnClickListener {
            val email = binding.usernameET.text.toString().trim()
            val password = binding.passwordET.text.toString().trim()
            val database = Firebase.database
            progressDialog.show()
            loginViewModel.login(email, password).observe(this, Observer { success ->
                if (success) {
                    val userAuth = loginViewModel.getUser()

                    if (userAuth != null) {
                        val uid = userAuth.uid

                        userViewModel.setUser(uid)

                        userViewModel.data.observe(this, Observer { User ->
                            Log.d("user", User.toString())
                        })

                    }
                    nextMainActivity()
                    progressDialog.cancel()
                } else {
                    Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_LONG).show()
                    progressDialog.cancel()
                }
            })
        }
    }
//
//    private fun isEmailValid(email: String): Boolean {
//        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
//        return emailRegex.matches(email)
//    }
//    private fun verify(email: String, password: String): Boolean {
//        return isEmailValid(email) && (password.length > 6)
//    }

    private fun nextMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}