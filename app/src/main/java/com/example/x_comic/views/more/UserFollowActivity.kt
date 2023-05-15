package com.example.x_comic.views.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.AuthorListAdapter
import com.example.x_comic.databinding.ActivityAuthorListBinding
import com.example.x_comic.databinding.ActivityUserFollowBinding
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.profile.AuthorProfileActivity

class UserFollowActivity : AppCompatActivity() {
    private var _currentAuthor: User? = null
    private lateinit var binding: ActivityUserFollowBinding
    private val userList: MutableList<User> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        var userViewModel: UserViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val avatarAdapter = AuthorListAdapter(userList)
        avatarAdapter.onItemClick = {
                book -> nextAuthorProfileActivity(book)
        }

        binding.listView!!.adapter = avatarAdapter;
        binding.listView!!.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var bundle = Bundle()
        bundle = intent.extras!!

        var author: User = bundle.get("authorKey") as User;

        _currentAuthor = author

        userViewModel.getAllUserFollow(_currentAuthor!!.id) {
                usersID -> run {
            userList.clear()
            for (userid in usersID.children) {
                userViewModel.getUserById(userid.value as String) {
                        user -> run {
                    if (user.isFollowing(_currentAuthor!!.id)) {
                        userList.add(0, user)
                    }
                }
                    avatarAdapter.notifyItemInserted(0)
                }
            }
            avatarAdapter.notifyDataSetChanged()
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