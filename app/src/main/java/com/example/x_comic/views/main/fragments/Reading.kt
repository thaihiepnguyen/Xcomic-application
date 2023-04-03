package com.example.x_comic.views.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.BookReadingAdapter
import com.example.x_comic.adapters.CategoryAdapter
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookReading
import com.example.x_comic.models.BookSneek
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager


class Reading : Fragment() {


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
    var bookDetailList: MutableList<Book> = mutableListOf(

        Book(
            bookList[0],
            253.2,
            125.5,
            20,
            arrayListOf("Romance", "Thriller", "Short Story", "Humor")
        ),
        Book(bookList[1], 154.4, 100.3, 50, arrayListOf("Fiction", "Horror", "Mystery", "Humor")),
        Book(bookList[2], 179.6, 122.2, 25, arrayListOf("School Life", "Humor", "Short Story")),
        Book(
            bookList[3],
            211.3,
            112.6,
            34,
            arrayListOf("Romance", "Mystery", "Short Story", "Humor")
        ),
        Book(
            bookList[4],
            236.2,
            109.7,
            36,
            arrayListOf("Fiction", "Thriller", "Mystery", "Horror", "Humor", "Romance")
        )
    )
    var listReadingOffline: MutableList<BookReading> = mutableListOf(
        BookReading(
            bookDetailList[1],
            current = 12
        ),
        BookReading(
            bookDetailList[0],
            current = 5
        ),
        BookReading(
            bookDetailList[4],
            current = 10
        ),
        BookReading(
            bookDetailList[3],
            current = 11
        ),
        BookReading(
            bookDetailList[2],
            current = 8
        )

    )

    var listReading: MutableList<BookReading> = mutableListOf();
    fun getListRead(){
    repeat(50)
    {
        val bookReading = BookReading(
            bookDetailList[it % 5],
            current = listReadingOffline[it % 5].current
        )
        listReading.add(bookReading)
    }
    }
    var customOfflineBookList : RecyclerView? = null;
    var customOnlineBookList: RecyclerView? = null;

    var btnOffline: TextView? = null;
    var btnOnline: TextView? = null;

    var layoutExpand: ExpandableRelativeLayout? = null;
    var layoutExpand2: ExpandableRelativeLayout? = null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_reading, container, false)

        customOfflineBookList = view.findViewById(R.id.offline_books);
        customOnlineBookList = view.findViewById(R.id.online_books);

        getListRead();



        val adapter = BookReadingAdapter(listReadingOffline);

        val OnlineAdapter = BookReadingAdapter(listReading, true);


        customOfflineBookList!!.adapter = adapter;

        customOnlineBookList!!.adapter = OnlineAdapter;


        customOfflineBookList!!.layoutManager = GridLayoutManager(this.context,3);
        customOnlineBookList!!.layoutManager = GridLayoutManager(this.context,3);



        btnOffline = view.findViewById(R.id.offline);
        btnOnline = view.findViewById(R.id.online);

        layoutExpand = view.findViewById(R.id.expandableLayoutOffline);


        layoutExpand2 = view.findViewById(R.id.expandableLayoutOnline);


        btnOffline!!.setOnClickListener{
            layoutExpand!!.toggle();
            if (layoutExpand!!.isExpanded){
                btnOffline!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevronup,0)
            }
            else {
                btnOffline!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevrondown,0)

            }
        }

        btnOnline!!.setOnClickListener{
            layoutExpand2!!.toggle();
            if (layoutExpand2!!.isExpanded){
                btnOnline!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevronup,0)
            }
            else {
                btnOnline!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevrondown,0)

            }
        }
        return view
    }


}