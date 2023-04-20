package com.example.x_comic.views.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookSneek
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import jp.wasabeef.glide.transformations.BlurTransformation


class CollectionActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val cover = findViewById(R.id.cover) as ImageView;
        val btnOption = findViewById(R.id.btnOption) as ImageButton
        val btnShare = findViewById(R.id.btnShare) as ImageButton

        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)

        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout?.totalScrollRange ?: 0
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = "Collection 1" // Set an empty title when fully collapsed
                    btnOption.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                        R.color.black))
                    btnShare.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                            R.color.black))
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = "" // Set your desired title when fully expanded
                    btnOption.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                            R.color.white))
                    btnShare.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                            R.color.white))
                    isShow = false
                }
            }
        })

        Glide.with(this)
            .load(R.drawable.bookcover)
           .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
            .into(cover)
        setSupportActionBar(toolbar)
        customBookListView = findViewById(R.id.collectionBookList);
        val bookListAdapter = BookListAdapter(bookDetailList);
        customBookListView!!.adapter = bookListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)


        btnOption.setOnClickListener { v ->
            showPopup(v)
        }

    }
    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.option_collection_menu, popup.menu)
        popup.show()
    }

}