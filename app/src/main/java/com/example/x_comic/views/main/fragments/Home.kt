package com.example.x_comic.views.main.fragments



import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.AvatarListAdapter
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.adapters.ListAdapterSlideshow
import com.example.x_comic.models.Avatar
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.login.LoginActivity
import com.example.x_comic.views.profile.MainProfileActivity
import com.google.android.material.tabs.TabLayout
import kotlin.math.log


class Home : Fragment() {
    val bookListSlideShow: MutableList<Product> = mutableListOf()
    val avatarList: MutableList<Avatar> = mutableListOf()
    val authorList: MutableList<User> = mutableListOf()
    var bookPointer: MutableList<Product> = mutableListOf()
    var bookBackup: MutableList<Product> = mutableListOf()
    private val bookPopularList: MutableList<Product> = mutableListOf()
    private val bookLatestList: MutableList<Product> = mutableListOf()
    private val bookCompletedList: MutableList<Product> = mutableListOf()

    var tabsBook = mutableListOf(
        bookPopularList,bookLatestList,bookCompletedList)
    var customSlideView: RecyclerView? = null;
    var customAvatarView: RecyclerView? = null;
    var customBookListView: RecyclerView? = null;
    var tabLayout: TabLayout? = null;
    var scrollView: NestedScrollView? = null;
    var avatar: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        var userViewModel: UserViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        var productViewModel: ProductViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)


        customSlideView = view.findViewById(R.id.listView);
        customAvatarView = view.findViewById(R.id.avatarListView);
        customBookListView = view.findViewById(R.id.popularListBook);
        scrollView = view.findViewById(R.id.nestedScrollView);
        avatar = view.findViewById(R.id.avatar);

        tabLayout = view.findViewById(R.id.tabs_book);

        tabLayout!!.removeAllTabs()
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Popular"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Latest"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Completed"))

        // TODO : khai báo adapter
        val adapterSlideShow = ListAdapterSlideshow(requireActivity(), bookListSlideShow);
        customSlideView!!.adapter = adapterSlideShow;
        customSlideView!!.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        val bookListAdapter = BookListAdapter(requireActivity(), bookPointer)
        customBookListView!!.adapter = bookListAdapter
        customBookListView!!.layoutManager = LinearLayoutManager(this.context);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)
        customAvatarView!!.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false);

        val avatarAdapter = AvatarListAdapter(requireActivity(), authorList)
        customAvatarView!!.adapter = avatarAdapter
//        if (bookList.isNotEmpty()){
//            val authorList = bookList.map { Avatar(it.author, R.drawable.avatar_1) };
//            avatarList.addAll(authorList)
//        }

        userViewModel.getAllAuthor()
            .observe(this, Observer { authors ->
                run {
                    authorList.clear()
                    authorList.addAll(authors)
                    avatarAdapter.notifyDataSetChanged()
                }
            })

        productViewModel.getAllBook { books ->
            run {
                bookListSlideShow.clear()
                for (book in books.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null) {
                        bookListSlideShow.add(product)
                    }
                }
                adapterSlideShow.notifyDataSetChanged()
            }
        }

        // TODO: code được comment ở đây tương đương với dòng 144
        // tùy TH mình sài nhe
//        productViewModel.getPopularBook().observe(this, Observer {
//            products -> run {
//                bookPopularList.clear()
//                bookPopularList.addAll(products)
//                if (tabLayout!!.selectedTabPosition == 0) {
//                    bookPointer.clear()
//                    bookPointer.addAll(bookPopularList)
//                    tabsBook[tabLayout!!.selectedTabPosition].clear()
//                    tabsBook[tabLayout!!.selectedTabPosition].addAll(bookPopularList)
//                }
//                bookListAdapter.notifyDataSetChanged()
//            }
//        })
        productViewModel.getPopularBook { books ->
            run {
                bookPopularList.clear()

                for (book in books.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null) {
                        bookPopularList.add(product)

                    }
                }
                
                if (tabLayout!!.selectedTabPosition == 0) {
                    bookPointer.clear()
                    bookPointer.addAll(bookPopularList)
                    bookBackup.clear()
                    bookBackup.addAll(bookPopularList)
                    tabsBook[tabLayout!!.selectedTabPosition].clear()
                    tabsBook[tabLayout!!.selectedTabPosition].addAll(bookPopularList)
                }
                bookListAdapter.notifyDataSetChanged()
            }
        }

        productViewModel.getCompletedBook { books ->
            run {
                bookCompletedList.clear()
                for (book in books.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null) {
                        bookCompletedList.add(product)
                    }
                }

                if (tabLayout!!.selectedTabPosition == 2) {
                    bookPointer.clear()
                    bookPointer.addAll(bookCompletedList)
                    tabsBook[tabLayout!!.selectedTabPosition].clear()
                    tabsBook[tabLayout!!.selectedTabPosition].addAll(bookCompletedList)
                }
                bookListAdapter.notifyDataSetChanged()
            }
        }

        productViewModel.getLatestBook { books ->
            run {
                bookLatestList.clear()
                for (book in books.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null) {
                        bookLatestList.add(product)
                    }
                }

                if (tabLayout!!.selectedTabPosition == 1) {
                    bookPointer.clear()
                    bookPointer.addAll(bookLatestList)
                    tabsBook[tabLayout!!.selectedTabPosition].clear()
                    tabsBook[tabLayout!!.selectedTabPosition].addAll(bookLatestList)
                }
                bookListAdapter.notifyDataSetChanged()
            }
        }
//
//        //    initMediator();
//        if (bookPopularList.isNotEmpty() && bookLatestList.isNotEmpty() && bookCompletedList.isNotEmpty()
//        ) {
//
//        }

        tabLayout!!.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                scrollView!!.smoothScrollTo(0,tabLayout!!.top);
                when (tab?.position) {
                    tab?.position ->  {
                        bookPointer.clear()
                        bookPointer.addAll(tabsBook[tab!!.position])

                        // Tạm bợ ^^
                        if (tabsBook[0].isEmpty()) {
                            tabsBook[0].addAll(bookBackup)
                        }
                        bookListAdapter!!.notifyDataSetChanged();
                        bookListAdapter!!.notifyItemRangeChanged(0,bookPointer.size);
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        avatar!!.setOnClickListener {
            nextProfileActivity()
        }
        var currentUser = FirebaseAuthManager.getUser()
        userViewModel.callApi(currentUser!!.uid).observe(this, Observer { user ->
            run {
                if (user.avatar != "") {
                    Glide.with(this)
                        .load(user.avatar)
                        .apply(RequestOptions().override(100, 100))
                        .circleCrop()
                        .into(avatar!!)
                }
            }
        })

        return view
    }

    private fun initMediator() {
        TabbedListMediator(
            customBookListView!!,
            tabLayout!!,
            tabsBook.indices.toList(),
            true
        ).attach()
    }

    // TODO: sẽ truyền với hiệu ứng từ trái sang phải
    private fun nextProfileActivity() {
        val intent = Intent(context, MainProfileActivity::class.java)
        startActivity(intent)
    }
}