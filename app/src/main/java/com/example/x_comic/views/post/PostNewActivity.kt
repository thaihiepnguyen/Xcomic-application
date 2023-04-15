package com.example.x_comic.views.post

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.CategoryAdapter
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookAuthor
import com.example.x_comic.models.Chapter
import com.example.x_comic.views.main.fragments.Writing
import com.google.android.flexbox.FlexboxLayoutManager

class PostNewActivity : AppCompatActivity() {

    val category_list: MutableList<String> = mutableListOf("Romance","Poetry","Science Fiction","Teen Fiction","Short Story","Mystery","Adventure","Thriller","Horror","Humor","LGBT+","Non Fiction","Fanfiction","Historical Fiction","Contemporary Lit","Diverse Lit","Fantasy","Paranormal","New Adult")
    var categoryView : RecyclerView? = null;

    var chapterList: MutableList<Chapter> = mutableListOf(
        Chapter("Chapter 1"),
        Chapter("Chapter 2"),
        Chapter("Chapter 3"),
        Chapter("Chapter 4"),
        Chapter("Chapter 5"),
        Chapter("Chapter 6"),
        )
    var customChapterListView: RecyclerView? = null;

    class ChaptersAdapter (private val chapters: MutableList<Chapter>) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {
        var onItemClick: ((chapter: Chapter, position: Int) -> Unit)? = null
        var onButtonClick: ((Chapter) -> Unit)? = null
        inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
            val name = listItemView.findViewById(R.id.tvChapterName) as TextView
            val dateUpdate = listItemView.findViewById(R.id.tvDate) as TextView

            init {
                listItemView.setOnClickListener { onItemClick?.invoke(chapters[adapterPosition], adapterPosition) }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            // Inflate the custom layout
            val chapterView = inflater.inflate(R.layout.chapter_status, parent, false)
            // Return a new holder instance
            return ViewHolder(chapterView)
        }

        override fun getItemCount(): Int {
            return chapters.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Get the data model based on position
            val c: Chapter = chapters.get(position)
            // Set item views based on your views and data model

            holder.name.text = chapters[position].name

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_new)

        val layoutManager = FlexboxLayoutManager(this);
        categoryView = findViewById(R.id.category_list);
        val adapter = CategoryAdapter(category_list);
        categoryView!!.adapter = adapter;
        categoryView!!.layoutManager = layoutManager;


        val nextButton = findViewById<Button>(R.id.btnNext)
        nextButton.setOnClickListener {

            val intent = Intent(this, NewChapterActivity::class.java)
            startActivity(intent)

        }

        customChapterListView = findViewById(R.id.listViewChapter) as RecyclerView;
        val chapterListAdapter = ChaptersAdapter(chapterList);
        customChapterListView!!.adapter = chapterListAdapter;
        customChapterListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customChapterListView?.addItemDecoration(itemDecoration)

        chapterListAdapter!!.onItemClick = {chapter, position ->
            // DO SOMETHING
        }

    }
}