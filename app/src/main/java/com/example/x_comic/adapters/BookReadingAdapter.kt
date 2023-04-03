package com.example.x_comic.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookReading

class BookReadingAdapter (
    private var bookReadingList: MutableList<BookReading>,
    private val isOnline: Boolean = false
) : RecyclerView.Adapter<BookReadingAdapter.ViewHolder>()
{
    var onItemClick: ((BookReading) -> Unit)? = null
    var context: Context? = null;
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var cover = listItemView.findViewById(R.id.cover) as ImageView;
        var title = listItemView.findViewById(R.id.bookname) as TextView;
        var progressBar = listItemView.findViewById(R.id.progress_bar) as ProgressBar;

        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(bookReadingList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        val inflater = LayoutInflater.from(context)


        var columnView = inflater.inflate(R.layout.book_reading, parent, false);
        if (isOnline){
            columnView = inflater.inflate(R.layout.book_reading_online, parent, false);
        }

        return ViewHolder(columnView);
    }

    override fun getItemCount(): Int {
        return bookReadingList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookReadingList.get(position);

        val cover = holder.cover;
        val title = holder.title;
        var progressbar = holder.progressBar;



        cover.setImageResource(book.book.book.cover);
        title.setText(book.book.book.title);

        var total = book.book.chapter;
        var current = book.current;

        progressbar.progress = current*100/total;





    }

}



