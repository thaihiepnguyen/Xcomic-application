package com.example.x_comic.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.Product
import com.google.firebase.storage.FirebaseStorage

class BookManagementListAdapter(
    private var bookList: MutableList<Product>
) : RecyclerView.Adapter<BookManagementListAdapter.ViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null
    private lateinit var _currentContext : Context
    //var context: Context? = null;
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var title = listItemView.findViewById(R.id.book_list_title) as TextView;
        var author = listItemView.findViewById(R.id.book_list_author) as TextView;
        var cover = listItemView.findViewById(R.id.cover_book) as ImageView;
        var view = listItemView.findViewById(R.id.view) as TextView;
        var favorite = listItemView.findViewById(R.id.favorite) as TextView;
        var chapter = listItemView.findViewById(R.id.chapter) as TextView;
        var blockBtn = listItemView.findViewById(R.id.blockBtn) as Button
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(bookList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context;
        _currentContext = context
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.book_management_item, parent, false)
        return ViewHolder(columnView)

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

        var blockBtn = holder.blockBtn

        title.setText(book.title);
        author.setText(book.author);
        val storage = FirebaseStorage.getInstance()
        val imageName = book.cover // Replace with your image name
        val imageRef = storage.reference.child("book/$imageName")
        imageRef.getBytes(Long.MAX_VALUE)
            .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                // Set the Bitmap to the ImageView
                cover.setImageBitmap(bitmap)

            }.addOnFailureListener {
                // Handle any errors
            }

        view.setText(book.view.toString());
        favorite.setText(book.favorite.toString());
        chapter.setText(book.chapters.size.toString());
    }

    override fun getItemCount(): Int {
        return bookList.size;
    }
}