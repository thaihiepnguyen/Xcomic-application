package com.example.x_comic.adapters

import BookDialog
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
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
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
        println("hello "+bookReadingList.size);

       
        return bookReadingList.size;
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookReadingList.get(position);

        val cover = holder.cover;
        val title = holder.title;
        var progressbar = holder.progressBar;



       // cover.setImageResource(book.book.book.cover);


        val imageName = book.book.cover
        Glide.with(cover.context)
            .load(imageName)
            .apply(RequestOptions().override(500, 600))
            .into(cover)
        title.setText(book.book.title);

        var total = book.chapter;
        var current = book.current;

        progressbar.progress = current*100/total;

        holder.itemView.setOnLongClickListener {
            val dialog = BookDialog(book.book.title, isOnline);
            dialog.show((context as? FragmentActivity)!!.supportFragmentManager,"dbchau10");
            true // Return true to indicate the event has been consumed
        }





    }

}



