package com.example.x_comic.views.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.adapters.AuthorListAdapter
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.databinding.ActivityAllBinding
import com.example.x_comic.databinding.ActivityAuthorListBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.profile.AuthorProfileActivity

class AuthorListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorListBinding
    private val authorList: MutableList<User> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        var userViewModel: UserViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val avatarAdapter = AuthorListAdapter(authorList)
        avatarAdapter.onItemClick = {
                book -> nextAuthorProfileActivity(book)
        }

        binding.listView!!.adapter = avatarAdapter;
        binding.listView!!.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        userViewModel.getAllUserIsFollowed(FirebaseAuthManager.auth.uid!!) {
                usersID -> run {
            authorList.clear()
            var cnt: Int = 0
            for (userid in usersID.children) {
                userViewModel.getUserById(userid.value as String) {
                        user -> run {
                    if (user.isfollowed(FirebaseAuthManager.auth.uid!!)) {
                        cnt++
                        authorList.add(user)
                    }
                }
                    avatarAdapter.notifyDataSetChanged()
                }
                avatarAdapter.notifyDataSetChanged()
            }
        }
        }
    }
    fun nextAuthorProfileActivity(author: User) {
        val intent = Intent(this, AuthorProfileActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("authorKey", author)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}