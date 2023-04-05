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

class Search : Fragment() {


    val bookList: MutableList<BookSneek> = mutableListOf(
        BookSneek("How to Burn The Bad Boy", "alsophanie", R.drawable.bookcover, 4.9),
        BookSneek("Temporarily", "bbiboo123", R.drawable.book_cover_1, 4.5),
        BookSneek("Rome Is Us", "ann_beyond", R.drawable.book_cover_2, 4.0),
        BookSneek("Fool Man", "landyshere", R.drawable.book_cover_3, 4.7),
        BookSneek("The Mind Of A Leader", "vivianneee", R.drawable.book_cover_4, 4.3),
        BookSneek("The Light Beyond The Garden Wall", "pixiequinn", R.drawable.book_cover_5, 4.5),
        BookSneek("The Secret At The Joneses", "rhjulxie", R.drawable.book_cover_6, 3.9),
        BookSneek("The Victim's Picture", "gizashey", R.drawable.book_cover_7, 4.2)

    )
    private val bookDetailList: MutableList<Book> = mutableListOf(

        Book(
            bookList[1],
            253.2,
            125.5,
            20,
            arrayListOf("Romance", "Thriller", "Short Story", "Humor")
        ),
        Book(bookList[2], 154.4, 100.3, 50, arrayListOf("Fiction", "Horror", "Mystery", "Humor")),
        Book(bookList[3], 179.6, 122.2, 13, arrayListOf("School Life", "Humor", "Short Story")),
        Book(
            bookList[4],
            211.3,
            112.6,
            7,
            arrayListOf("Romance", "Mystery", "Short Story", "Humor")
        ),
        Book(
            bookList[5],
            236.2,
            109.7,
            36,
            arrayListOf("Fiction", "Thriller", "Mystery", "Horror", "Humor", "Romance")
        ),

        Book(
            bookList[3],
            236.2,
            109.7,
            36,
            arrayListOf("Fiction", "Thriller", "Horror", "Humor", "Romance")
        ),
        Book(
            bookList[6],
            236.2,
            109.7,
            36,
            arrayListOf("Fiction", "Horror", "Humor", "Romance")
        ),
        Book(
            bookList[7],
            236.2,
            109.7,
            36,
            arrayListOf("Fiction", "Thriller", "Horror", "Humor")
        )
    )


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
        val bookListAdapter = BookListAdapter(bookDetailList);
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