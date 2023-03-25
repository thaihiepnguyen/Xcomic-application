package com.example.x_comic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ListAdapterSlideshow (

    private var bookList: MutableList<BookSneek>,
) : RecyclerView.Adapter<ListAdapterSlideshow.ViewHolder>()
{
    var onItemClick: ((BookSneek) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var title = listItemView.findViewById(R.id.bookname) as TextView;
        var author = listItemView.findViewById(R.id.author) as TextView;
        var cover = listItemView.findViewById(R.id.cover) as ImageView;


        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(bookList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ListAdapterSlideshow.ViewHolder {
        val context = parent.context;
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.book_cover_slideshow, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return bookList.size;
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = bookList.get(position);
        val title = holder.title;
        val author = holder.author;
        val cover = holder.cover;

        title.setText(bookList[position].title);
        author.setText(bookList[position].author);
        cover.setImageResource(bookList[position].cover);

    }

}



