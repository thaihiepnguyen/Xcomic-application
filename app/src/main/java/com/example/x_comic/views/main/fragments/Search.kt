package com.example.x_comic.views.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.x_comic.R
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.models.Product
import com.example.x_comic.views.detail.DetailActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Search : Fragment() {


    var bookList: MutableList<Product> = mutableListOf()
    var bookDetailList: MutableList<Product> = mutableListOf()


    var customBookListView: RecyclerView? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)

        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        var keyword = arguments?.getString("keyword")
        var json = arguments?.getString("filter")
        var myFilter = com.example.x_comic.models.Filter()
        if (json!=null){
            myFilter = Klaxon().parse<com.example.x_comic.models.Filter>(json)!!
        }


        customBookListView = view.findViewById(R.id.searchBookList);

        val bookListAdapter = BookListAdapter(bookDetailList)

        bookListAdapter.onItemClick = {
            book -> nextBookDetailActivity(book)
        }
        customBookListView!!.adapter = bookListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this.context);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)


        val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("book")

        database.orderByChild("title")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    bookDetailList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val book = snapshot.getValue(Product::class.java)
                        if (book != null) {
                            if (keyword == null){
                                bookDetailList.add(book)
                            }else if (book.title.contains(keyword!!, ignoreCase = true)){
                                bookDetailList.add(book)
                            }
                        }
                    }
                    val tmp = com.example.x_comic.models.Filter.applyFilter(myFilter!!,
                        bookDetailList as ArrayList<Product>
                    )
                    bookDetailList.clear()
                    bookDetailList.addAll(tmp)
                    bookListAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // handle error
                }
            })





        val btnFilter: ImageButton = view.findViewById(R.id.btnFilter);

        btnFilter.setOnClickListener{
            val fragment = Filter();

            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            bundle.putString("filter", Klaxon().toJsonString(myFilter))
            fragment.arguments = bundle
           val oldFragment: FrameLayout = view.findViewById(R.id.searchLayout);
           oldFragment.removeAllViews();


            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.detach(Search());
            transaction.remove(Search());
            transaction.replace(R.id.searchLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        view.findViewById<EditText>(R.id.searchEditText).setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                hideKeyboard(view)

                var searchEditText = view.findViewById<EditText>(R.id.searchEditText)
                keyword = searchEditText.text.toString()
                myFilter = com.example.x_comic.models.Filter()
                val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("book")

                database.orderByChild("title")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            bookDetailList.clear()
                            for (snapshot in dataSnapshot.children) {
                                val book = snapshot.getValue(Product::class.java)
                                if (book != null) {
                                    if (keyword == null){
                                        bookDetailList.add(book)
                                    }else if (book.title.contains(keyword!!, ignoreCase = true)){
                                        bookDetailList.add(book)
                                    }
                                }
                            }
                            val tmp = com.example.x_comic.models.Filter.applyFilter(myFilter!!,
                                bookDetailList as ArrayList<Product>
                            )
                            bookDetailList.clear()
                            bookDetailList.addAll(tmp)
                            bookListAdapter.notifyDataSetChanged()
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // handle error
                        }
                    })
                return@OnKeyListener true
            }
            false
        })

        return view
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun nextBookDetailActivity(book: Product) {
        val intent = Intent(context, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("bookKey", book)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}