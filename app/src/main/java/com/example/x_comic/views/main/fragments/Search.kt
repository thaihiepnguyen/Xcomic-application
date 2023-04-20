package com.example.x_comic.views.main.fragments

import android.content.Context
import android.os.Bundle
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
import com.example.x_comic.R
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookSneek
import com.example.x_comic.models.Product

class Search : Fragment() {


    val bookList: MutableList<Product> = mutableListOf()
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
        customBookListView = view.findViewById(R.id.searchBookList);
        val bookListAdapter = BookListAdapter(requireActivity(),bookDetailList);
        customBookListView!!.adapter = bookListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this.context);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)

        val btnFilter: ImageButton = view.findViewById(R.id.btnFilter);

        btnFilter.setOnClickListener{
            val fragment = Filter();

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

        view.findViewById<EditText>(R.id.searchEditText).setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(view)
            }
        })

        return view
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
}