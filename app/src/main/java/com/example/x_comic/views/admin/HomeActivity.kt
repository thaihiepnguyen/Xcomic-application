package com.example.x_comic.views.admin

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.x_comic.R
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.views.login.LoginActivity
import com.example.x_comic.views.post.NewChapterActivity

class HomeActivity : AppCompatActivity() {
    var btnBooksManager : Button? = null
    var btnAccountsManager : Button? = null
    var btnLogout : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnBooksManager = findViewById(R.id.btnBooksManagement)
        btnAccountsManager = findViewById(R.id.btnAccountsManagement)
        btnLogout = findViewById(R.id.logout)

        btnBooksManager?.setOnClickListener {
            // TODO: Màn hình quản lý sách
            val intent = Intent(this, BookManagementActivity::class.java)
            startActivity(intent)
        }

        btnAccountsManager?.setOnClickListener {
            // TODO: Màn hình quản lý tài khoản
            val intent = Intent(this, AccountManagementActivity::class.java)
            startActivity(intent)
        }
        val sharedPreferences: SharedPreferences = getSharedPreferences("MySharedPreferences02032002", MODE_PRIVATE)
        btnLogout?.setOnClickListener {
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