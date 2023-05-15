package com.example.x_comic.adapters

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.Product
import com.google.firebase.storage.FirebaseStorage

class BooksAuthAdapter (private val books: MutableList<Product>) : RecyclerView.Adapter<BooksAuthAdapter.ViewHolder>() {
    var onItemClick: ((book: Product, position: Int) -> Unit)? = null
    var onButtonClick: ((Product) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val title = listItemView.findViewById(R.id.book_list_title) as TextView
        val imageView: ImageView = listItemView.findViewById(R.id.cover_book) as ImageView
        val status = listItemView.findViewById(R.id.book_list_status) as TextView
        val chapter = listItemView.findViewById(R.id.book_list_chapter) as TextView

        init {
            listItemView.setOnClickListener { onItemClick?.invoke(books[adapterPosition], adapterPosition) }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val studentView = inflater.inflate(R.layout.book_status_writting, parent, false)
        // Return a new holder instance
        return ViewHolder(studentView)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val s: Product = books.get(position)
        // Set item views based on your views and data model

        holder.title.text = books[position].title
        if (!books[position].status) {
            holder.status.text = "In progress"

            holder.status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext()!!,
                R.color.golden))
        } else {
            holder.status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext()!!,
                R.color.done))
        }

        holder.chapter.text = "${countChapterIsPosted(books[position])} of ${books[position].chapters.size} Chapter was posted"
//        var cover = holder.imageView
//        val storage = FirebaseStorage.getInstance()
//        val imageName = books[position].cover // Replace with your image name
//        val imageRef = storage.reference.child("book/$imageName")
//        imageRef.getBytes(Long.MAX_VALUE)
//            .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
//                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                // Set the Bitmap to the ImageView
//                cover.setImageBitmap(bitmap)
//
//            }.addOnFailureListener {
//                // Handle any errors
//            }
        Glide.with(holder.imageView.context)
            .load(books[position].cover)
            .apply(RequestOptions().override(500, 600))
            .into(holder.imageView)
    }

    private fun countChapterIsPosted (p : Product) : Int {
        var count : Int = 0
        for (i in p.chapters)
            if (!i._lock)
                count++
        return count
    }
}
