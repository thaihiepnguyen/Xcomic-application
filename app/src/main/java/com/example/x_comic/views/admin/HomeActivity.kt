package com.example.x_comic.views.admin

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.login.LoginActivity

class HomeActivity : AppCompatActivity() {
    var btnBooksManager : Button? = null
    var btnAccountsManager : Button? = null
    var btnLogout : Button? = null
    var ivAvatar : ImageView? = null
    var tvNameAdmin : TextView? = null
    var tvEmail : TextView? = null
    var numUsers : TextView? = null
    var numBooks : TextView? = null

    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        btnBooksManager = findViewById(R.id.btnBooksManagement)
        btnAccountsManager = findViewById(R.id.btnAccountsManagement)
        btnLogout = findViewById(R.id.logout)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvNameAdmin = findViewById(R.id.tvNameAdmin)
        tvEmail = findViewById(R.id.tvGmail)
        numUsers = findViewById(R.id.tvUsers)
        numBooks = findViewById(R.id.tvBooks)

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.callApi(uid)
                .observe(this, Observer {
                    // user nó được thay đổi realtime ở đb
                        user -> run {
                    Glide.with(ivAvatar!!.context)
                        .load(user.avatar)
                        .apply(RequestOptions().override(250, 250))
                        .circleCrop()
                        .into(ivAvatar!!)

                    tvNameAdmin?.text = user.full_name
                    tvEmail?.text = user.email
                }})
        }

        productViewModel.getAllBook { books ->
            run {
                var count = 0
                for (book in books.children) {
                    count++
                }
                numBooks?.text = count.toString() + " Books"
            }
        }

        userViewModel.getAllUser { users ->
            run {
                var count = 0
                for (book in users.children) {
                    count++
                }
                numUsers?.text = count.toString() + " Users"
            }
        }

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