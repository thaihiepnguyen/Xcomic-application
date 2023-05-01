package com.example.x_comic.views.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.AvatarListAdapter
import com.example.x_comic.adapters.IAvatarListAdapter
import com.example.x_comic.adapters.ListAdapterSlideshow
import com.example.x_comic.databinding.ActivityAuthorProfileBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import jp.wasabeef.glide.transformations.BlurTransformation


class AuthorProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    private var _currentUser: User? = null
    private var _currentAuthor: User? = null
    private var _isFollowing: Boolean? = null
    val bookList: MutableList<Product> = mutableListOf()
    val followList: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        binding.listView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.profileListView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val favoriteAdapter = ListAdapterSlideshow(this, bookList)
        val avatarAdapter = AvatarListAdapter(followList, object : IAvatarListAdapter {
            override fun onClickItemAuthor(author: User) {
                nextAuthorProfileActivity(author)
            }
        })
        binding.listView.adapter = favoriteAdapter
        binding.profileListView.adapter = avatarAdapter

        binding.backBtn.setOnClickListener {
            finish()
        }
//        var author = intent.getStringExtra("authorKey")?.let {
//            Klaxon().parse<User>(it)
//        }

        var bundle = Bundle()

        bundle = intent.extras!!

        var author: User = bundle.get("authorKey") as User;

        _currentAuthor = author

        userViewModel.getAllUserFollow(_currentAuthor!!.id) {
                usersID -> run {
            followList.clear()
            var cnt: Int = 0
            for (userid in usersID.children) {
                userViewModel.getUserById(userid.value as String) {
                        user -> run {
                    if (user.isFollowing(_currentAuthor!!.id)) {
                        cnt++
                        followList.add(user)
                    }
                }
                    avatarAdapter.notifyDataSetChanged()
                    binding.followProfileTV.text = "${cnt} Profiles"
                }
                avatarAdapter.notifyDataSetChanged()
            }
        }
        }

        // uid ở đây là người click vào xem profile của author
        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.callApi(uid)
                .observe(this, Observer {
                    // user nó được thay đổi realtime ở đb
                    user -> run {

                    _currentUser = user
                    _isFollowing = _currentAuthor?.let { user.following(it) }
                    if (_isFollowing == true) {
                        setFollowingUI(binding.followBT)
                    }
                    else {
                        setFollowUI(binding.followBT)
                    }
                    binding.followNumber.text = _currentAuthor?.have_followed?.size.toString()
                }
            })
        }
        binding.followBT.setOnClickListener {
            if (_isFollowing == false) {
                setFollowingUI(binding.followBT)
                if (_currentAuthor != null) {
                    // TODO: nghĩa là thằng current user sẽ follow author
                    // và thằng cur author được user này follow
                    // khổ quá :<<<
                    _currentUser?.follow(_currentAuthor!!)
                    _currentAuthor?.haveFollowed(_currentUser!!)
                }
                _isFollowing = true
            } else {
                setFollowUI(binding.followBT)
                if (_currentAuthor != null) {
                    _currentUser?.unfollow(_currentAuthor!!)
                    _currentAuthor?.unHaveFollowed(_currentUser!!)
                }
                _isFollowing = false
            }
            _currentUser?.let { it1 -> userViewModel.saveCurrentFollow(it1) }
            _currentAuthor?.let { it1 -> userViewModel.saveCurrentHaveFollowed(it1) }
        }

        binding.emailTV.text = author!!.email
        binding.usernameTV.text = author!!.full_name
        binding.aboutme.text = author!!.aboutme

        if (author.avatar != "") {
            Glide.with(binding.avtImg.context)
                .load(author.avatar)
                .apply(RequestOptions().override(100, 100))
                .circleCrop()
                .into(binding.avtImg)

            Glide.with(binding.background.context)
                .load(author.avatar)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(30, 3)))
                .into(binding.background)
        } else {
            // nếu mà chưa có avt thì mặc định render vầy :v
            Glide.with(binding.avtImg.context)
                .load(R.drawable.avatar)
                .apply(RequestOptions().override(100, 100))
                .circleCrop()
                .into(binding.avtImg)
        }

        binding.listView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        productViewModel.getLatestBook().observe(this, Observer {
                products -> run {
            bookList.clear()
            bookList.addAll(products)
            val adapter = ListAdapterSlideshow(this, bookList);

            binding.listView.adapter = adapter
        }
        })
    }

    fun setFollowingUI(follow: Button) {
        follow.setText("Following")
        val drawable = resources.getDrawable(R.drawable.baseline_group_24)
        follow.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    fun setFollowUI(follow: Button) {
        follow.setText("Follow")
        val drawable = resources.getDrawable(R.drawable.baseline_person_add_alt_1_24)
        follow.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
    fun nextAuthorProfileActivity(author: User) {
        val intent = Intent(this, AuthorProfileActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("authorKey", author)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}