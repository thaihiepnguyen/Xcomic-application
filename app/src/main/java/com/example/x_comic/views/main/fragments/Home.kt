package com.example.x_comic.views.main.fragments


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmadhamwi.tabsync.TabbedListMediator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.AvatarListAdapter
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.adapters.ListAdapterSlideshow
import com.example.x_comic.models.Avatar
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookSneek
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.profile.ProfileActivity
import com.google.android.material.tabs.TabLayout
import kotlin.random.Random


class Home : Fragment() {


    val bookList: MutableList<Product> = mutableListOf()

    val avatarList: MutableList<Avatar> = mutableListOf()

    var bookDetailList: MutableList<Product> = mutableListOf()

    private val bookLatestList: MutableList<Product> = mutableListOf()

    private val bookCompletedList: MutableList<Product> = mutableListOf()

    var tabsBook = mutableListOf(
        bookDetailList,bookLatestList,bookCompletedList);
    var customSlideView: RecyclerView? = null;
    var customAvatarView: RecyclerView? = null;
    var customBookListView: RecyclerView? = null;
    var tabLayout: TabLayout? = null;
    var scrollView: NestedScrollView? = null;
    var avatar: ImageButton? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        var productViewModel: ProductViewModel
        var userViewModel: UserViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
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

        productViewModel.getAllBook()
            .observe(this, Observer { products ->
                run {
                    bookList.clear()

                    println(products)
                    bookList.addAll(products)
                    val adapter = ListAdapterSlideshow(requireActivity(), bookList);
                    customSlideView!!.adapter = adapter;
                    customSlideView!!.layoutManager =
                        LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false);

                }
            })

        var bookListAdapter:BookListAdapter? = null;
        productViewModel.getPopularBook().observe(this,Observer{
                popularProducts->run{
            bookDetailList.clear()
            bookDetailList.addAll(popularProducts)
            bookListAdapter = BookListAdapter(requireActivity(),bookDetailList);
            customBookListView!!.adapter = bookListAdapter;
        }
        })


        productViewModel.getCompletedBook().observe(this,Observer{
                completedProducts->run{
            bookCompletedList.clear()
            bookCompletedList.addAll(completedProducts)
        }
        })
        productViewModel.getLatestBook().observe(this,Observer{
                latestProducts->run{
            bookLatestList.clear()
            bookLatestList.addAll(latestProducts)
        }
        })


        println("abc"+ bookList);


                    tabsBook = mutableListOf(
                        bookDetailList,bookLatestList,bookCompletedList);


                    val avatarAdapter = AvatarListAdapter(avatarList);



                    println(bookDetailList.iterator());
                    println(bookLatestList.iterator());
                    println(bookCompletedList.iterator());
                    println(tabsBook[1].iterator());

                    customAvatarView!!.adapter = avatarAdapter;



                    customAvatarView!!.layoutManager =
                        LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false);

                    customBookListView!!.layoutManager = LinearLayoutManager(this.context);
                    val itemDecoration: RecyclerView.ItemDecoration =
                        DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
                    customBookListView?.addItemDecoration(itemDecoration)

                    //    initMediator();

                    tabLayout!!.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener
                    {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            scrollView!!.smoothScrollTo(0,tabLayout!!.top);
                            when (tab?.position) {
                                //NEED SOLUTION HERE
                                tab?.position ->  {
                                    bookDetailList.clear();
                                    bookListAdapter!!.notifyDataSetChanged();
                                    // println(1);
                                    bookDetailList.addAll(tabsBook[tab!!.position]);
                                    println(bookDetailList);
                                    bookListAdapter!!.notifyItemRangeChanged(0,bookDetailList.size);
                                    bookListAdapter!!.notifyItemRangeChanged(0,bookDetailList.count());
                                }

                            }

                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {

                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {

                        }
                    })


                    // TODO: thêm lắng nghe sự kiện click vào avatar nhé!
                    avatar!!.setOnClickListener {
                        nextProfileActivity()
                    }




        var currentUser = FirebaseAuthManager.getUser()
        userViewModel.callApi(currentUser!!.uid).observe(this, Observer { user ->
            run {
                if (user.avatar !== "") {
                    Glide.with(this)
                        .load(user.avatar)
                        .apply(RequestOptions().override(100, 100).transform(CenterCrop()).transform(RoundedCorners(150)))
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
        val intent = Intent(context, ProfileActivity::class.java)
        startActivity(intent)
    }
}