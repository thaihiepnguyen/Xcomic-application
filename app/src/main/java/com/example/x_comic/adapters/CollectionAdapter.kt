package com.example.x_comic.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.BookReading
import com.example.x_comic.models.CollectionBook
import jp.wasabeef.glide.transformations.BlurTransformation

class CollectionAdapter (
    private var CollectionList: MutableList<CollectionBook>,
) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>()
{
    var onItemClick: ((CollectionBook) -> Unit)? = null
    var context: Context? = null;
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var cover = listItemView.findViewById(R.id.cover) as ImageView;
        var title = listItemView.findViewById(R.id.collection_name) as TextView;
        var number = listItemView.findViewById(R.id.books_number) as TextView

        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(CollectionList[adapterPosition])
            }
        }

    }


    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        val inflater = LayoutInflater.from(context)


       var columnView =  inflater.inflate(R.layout.collection_book, parent, false);
        return ViewHolder(columnView);
    }

    override fun getItemCount(): Int {
        return CollectionList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collection = CollectionList.get(position);

        val cover = holder.cover;
        val title = holder.title;
        var number = holder.number;


        cover.setImageResource(collection.bookList[0].book.cover);



//        Glide.with(context!!)
//            .load(collection.bookList[0].book.cover)
//            .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
//            .into(cover)
        title.setText(collection.name);
        number.setText(collection.bookList.size.toString());







    }

}