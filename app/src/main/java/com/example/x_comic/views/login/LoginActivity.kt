package com.example.x_comic.views.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.databinding.ActivityLoginBinding
import com.example.x_comic.viewmodels.LoginViewModel
import com.example.x_comic.views.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginBtn.setOnClickListener {
            val email = binding.usernameET.text.toString()
            val password = binding.passwordET.text.toString()

            loginViewModel.login(email, password).observe(this, Observer { success ->
                if (success) {
                    nextMainActivity()
                } else {
                    Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun nextMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}