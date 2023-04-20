package com.example.x_comic.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.Book
import com.example.x_comic.models.Product
import com.example.x_comic.views.detail.DetailActivity
import com.google.firebase.storage.FirebaseStorage
import jp.wasabeef.glide.transformations.BlurTransformation

class CollectionBookList (

    private var bookList: MutableList<Book>,
) : RecyclerView.Adapter<CollectionBookList.ViewHolder>()
{
    var context: Context? = null;
    var onItemClick: ((Book) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var title = listItemView.findViewById(R.id.book_list_title) as TextView;
        var author = listItemView.findViewById(R.id.book_list_author) as TextView;
        var cover = listItemView.findViewById(R.id.cover_book) as ImageView;
        var view = listItemView.findViewById(R.id.view) as TextView;
        var favorite = listItemView.findViewById(R.id.favorite) as TextView;
        var chapter = listItemView.findViewById(R.id.chapter) as TextView;
        var category1 = listItemView.findViewById(R.id.category_tag) as TextView;
        var category2 = listItemView.findViewById(R.id.category_tag2) as TextView;
        var category3 = listItemView.findViewById(R.id.category_tag3) as TextView;
        var rest = listItemView.findViewById(R.id.rest) as TextView;

        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(bookList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context;
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.book_list_2, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return bookList.size;
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList.get(position);

        val title = holder.title;
        val author = holder.author;
        val cover = holder.cover;

        val view = holder.view;
        val favorite = holder.favorite;
        val chapter = holder.chapter;



        var category_holder = arrayListOf(holder.category1,holder.category2,holder.category3)
        var rest = holder.rest;

        cover.setImageResource(book.book.cover);
        title.setText(book.book.title);
        author.setText(book.book.author);

        view.setText(book.view.toString());
        favorite.setText(book.favorite.toString());
        chapter.setText(book.chapter.toString());

        val category = book.genres.take(3);
        for (i in category) {
            when (i){
                "Romance" -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!,
                        R.color.love
                    ));
                }
                "Fiction" -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!,
                        R.color.yellow_green
                    ));
                }
                "Short Story" -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!,
                        R.color.light_blue
                    ));
                }
                "Mystery" -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.violet));
                }
                "Thriller" -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!,
                        R.color.golden
                    ));
                }
                "Horror" -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!,
                        R.color.purple_500
                    ));
                }
                "Humor" -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!,
                        R.color.pink
                    ));
                }
                else -> {
                    category_holder[category.indexOf(i)].setText(i)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!,
                        R.color.lightgrey
                    ));
                }


            }
        }

        rest.setText(if ((book.genres.size -3)>0) "+ ${book.genres.size -3} more" else "");


    }

}



