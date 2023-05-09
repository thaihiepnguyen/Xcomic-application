package com.example.x_comic.views.profile

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.x_comic.R
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.views.login.LoginActivity

class BlockUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_user)

        val sharedPreferences: SharedPreferences = getSharedPreferences("MySharedPreferences02032002", MODE_PRIVATE)
        findViewById<Button>(R.id.btnlogout).setOnClickListener {
            // đăng xuất khỏi firebase
            FirebaseAuthManager.auth.signOut();
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.remove("currentRole")
            editor.apply()
            nextLoginActivity();
        }
    }

    private fun nextLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}