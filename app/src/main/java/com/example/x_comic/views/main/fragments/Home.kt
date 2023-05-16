package com.example.x_comic.views.main.fragments



import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat

import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.*
import com.example.x_comic.models.Avatar
import com.example.x_comic.models.BookReading

import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.detail.DetailActivity
import com.example.x_comic.views.more.AllActivity
import com.example.x_comic.views.more.CompletedActivity
import com.example.x_comic.views.more.LastestActivity
import com.example.x_comic.views.more.PopularActivity
import com.example.x_comic.views.profile.AuthorProfileActivity
import com.example.x_comic.views.profile.MainProfileActivity
import com.example.x_comic.views.read.ReadBookActivity

import com.google.android.material.tabs.TabLayout



class Home : Fragment() {
    val bookListSlideShow: MutableList<Product> = mutableListOf()
    val avatarList: MutableList<Avatar> = mutableListOf()
    val authorList: MutableList<User> = mutableListOf()
    var bookPointer: MutableList<Product> = mutableListOf()
    var bookPopularBackup: MutableList<Product> = mutableListOf()
    var bookLastestBackup: MutableList<Product> = mutableListOf()
    var bookCompletedBackup: MutableList<Product> = mutableListOf()
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
    var cover: ImageView? = null
    var bookTitle: TextView? = null
    var bookAuthor: TextView? = null
    var viewmore: TextView? = null
    var viewmoreAll: TextView? = null
    var progressBar: ProgressBar? = null

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
        cover = view.findViewById(R.id.book)
        bookTitle = view.findViewById(R.id.book_title)
        bookAuthor = view.findViewById(R.id.book_author)
        progressBar = view.findViewById(R.id.progressBar)
        viewmore = view.findViewById(R.id.view_more_vertical)
        viewmoreAll = view.findViewById(R.id.view_more)

        var book_reading: com.example.x_comic.models.Reading? = null;
        var book_reading_info: BookReading? = null;


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
        val bookListAdapter = BookListAdapter(bookPointer)
        bookListAdapter.onItemClick = {
                book -> nextBookDetailActivity(book)
        }
        customBookListView!!.adapter = bookListAdapter
        customBookListView!!.layoutManager = LinearLayoutManager(this.context);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)
        customAvatarView!!.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false);

        val avatarAdapter = AvatarListAdapter(authorList)

        avatarAdapter.onItemClick = {
                author -> nextAuthorProfileActivity(author)
        }



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

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            productViewModel.getAllReadingBook(uid) { book ->
                run {

                    val firstSnapshot = if (book.hasChildren()) {
                        book.children.iterator().next()
                    } else {
                        null
                    }
                    val bookid =
                        firstSnapshot?.getValue(com.example.x_comic.models.Reading::class.java)

                    book_reading = bookid;
                    if (bookid != null) {
                        productViewModel.getBookById(bookid!!.id_book) { bookInner ->
                            run {

                                bookTitle!!.text = bookInner.title
                                userViewModel.getUserById(bookInner.author) { user ->
                                    run {
                                        bookAuthor!!.text = user.penname
                                    }
                                }

                                cover?.let {
                                    Glide.with(it.context)
                                        .load(bookInner.cover)
                                        .apply(RequestOptions().override(500, 600))
                                        .into(cover!!)
                                }

                                progressBar!!.progress = bookid.posChap * 100 / bookid.numChap;

                                book_reading_info = BookReading(bookInner,bookid.posChap,bookid.numChap)
                            }
                        }
                    }
                }
            }
        }

        val card_holder = view.findViewById<CardView>(R.id.card_holder);

        card_holder.setOnClickListener {
            if (book_reading_info != null){
            val intent = Intent(context, ReadBookActivity::class.java)

                intent.putExtra("book", book_reading_info!!.book!!.id)
                intent.putExtra("id_chapter", book_reading_info!!.book!!.chapters[book_reading_info!!.current - 1].id_chapter)
            ActivityCompat.startActivityForResult(requireActivity(), intent, 302, null)
        }
        }
        productViewModel.getAllBook { books ->
            run {
                bookListSlideShow.clear()
                for (book in books.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null && !product.hide) {
                        bookListSlideShow.add(product)
                    }
                }
                adapterSlideShow.notifyDataSetChanged()
            }
        }



        // TODO: code được comment ở đây tương đương với dòng 155
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
                    if (product != null && !product.hide) {
                        bookPopularList.add(product)

                    }
                }

                if (tabLayout!!.selectedTabPosition == 0) {
                    bookPointer.clear()
                    bookPointer.addAll(bookPopularList)
                    bookPopularBackup.clear()
                    bookPopularBackup.addAll(bookPopularList)
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
                    if (product != null && !product.hide) {
                        bookCompletedList.add(product)
                    }
                }

                if (tabLayout!!.selectedTabPosition == 2) {
                    bookPointer.clear()
                    bookPointer.addAll(bookCompletedList)
                    bookCompletedBackup.clear()
                    bookCompletedBackup.addAll(bookCompletedList)
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
                    if (product != null && !product.hide) {
                        bookLatestList.add(product)
                    }
                }

                if (tabLayout!!.selectedTabPosition == 1) {
                    bookPointer.clear()
                    bookPointer.addAll(bookLatestList)
                    bookLastestBackup.clear()
                    bookLastestBackup.addAll(bookLatestList)
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
                        // Tạm bợ ^^
                        if (tabsBook[0].isEmpty()) {
                            tabsBook[0].addAll(bookPopularBackup)
                        }
                        if (tabsBook[1].isEmpty()) {
                            tabsBook[1].addAll(bookLastestBackup)
                        }
                        if (tabsBook[2].isEmpty()) {
                            tabsBook[2].addAll(bookCompletedBackup)
                        }
                        bookPointer.addAll(tabsBook[tab!!.position])
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
        if (currentUser != null)
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

        viewmore!!.setOnClickListener {
            if (tabLayout!!.selectedTabPosition == 0) {
                nextPopularActivity()
            }
            if (tabLayout!!.selectedTabPosition == 1) {
                nextLastestActivity()
            }
            if (tabLayout!!.selectedTabPosition == 2) {
                nextCompletedActivity()
            }
        }

        viewmoreAll!!.setOnClickListener {
            nextAllActivity()
        }

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

    private fun nextPopularActivity() {
        val intent = Intent(context, PopularActivity::class.java)
        startActivity(intent)
    }

    private fun nextCompletedActivity() {
        val intent = Intent(context, CompletedActivity::class.java)
        startActivity(intent)
    }

    private fun nextAllActivity() {
        val intent = Intent(context, AllActivity::class.java)
        startActivity(intent)
    }

    private fun nextLastestActivity() {
        val intent = Intent(context, LastestActivity::class.java)
        startActivity(intent)
    }


    fun nextAuthorProfileActivity(author: User) {
        val intent = Intent(context, AuthorProfileActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("authorKey", author)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    fun nextBookDetailActivity(book: Product) {
        val intent = Intent(context, DetailActivity::class.java)
//        val bundle = Bundle()
//        bundle.putSerializable("productKey", book)
        intent.putExtra("book_data", book.id)

        startActivity(intent)
    }
}