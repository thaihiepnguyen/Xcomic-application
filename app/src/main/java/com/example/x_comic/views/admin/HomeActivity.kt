package com.example.x_comic.views.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.x_comic.R

class HomeActivity : AppCompatActivity() {
    var btnBooksManager : Button? = null
    var btnAccountsManager : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnBooksManager = findViewById(R.id.btnBooksManagement)
        btnAccountsManager = findViewById(R.id.btnAccountsManagement)

        btnBooksManager?.setOnClickListener {
            // TODO: Màn hình quản lý sách
        }

        btnAccountsManager?.setOnClickListener {
            // TODO: Màn hình quản lý tài khoản
        }
    }
}