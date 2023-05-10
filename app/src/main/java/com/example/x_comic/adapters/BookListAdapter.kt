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
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.Book
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.detail.DetailActivity
import com.google.firebase.storage.FirebaseStorage
import jp.wasabeef.glide.transformations.BlurTransformation
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.ProductViewModel

class BookListAdapter (
    private var bookList: MutableList<Product>
) : RecyclerView.Adapter<BookListAdapter.ViewHolder>()
{
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
        var category1 = listItemView.findViewById(R.id.category_tag) as TextView;
        var category2 = listItemView.findViewById(R.id.category_tag2) as TextView;
        var category3 = listItemView.findViewById(R.id.category_tag3) as TextView;
        var rest = listItemView.findViewById(R.id.rest) as TextView;
        var love = listItemView.findViewById(R.id.favorite_book_list) as ImageButton;
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
        var columnView =  inflater.inflate(R.layout.book_list, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return bookList.size;
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var userViewModel : UserViewModel = UserViewModel()
        var bookViewModel : ProductViewModel = ProductViewModel()
        val book = bookList.get(position);

        val title = holder.title;

        val author = holder.author;
        val cover = holder.cover;

        val view = holder.view;
        val favorite = holder.favorite;
        val chapter = holder.chapter;

        var _currentUser: User? = null


        var category_holder = arrayListOf(holder.category1,holder.category2,holder.category3)

        var love = holder.love;
        var rest = holder.rest;
        var favourite = false
        title.setText(book.title);
        userViewModel.getUserById(book.author) {
            user -> author.setText(user.penname);
        }

//        val storage = FirebaseStorage.getInstance()
        val imageName = book.cover // Replace with your image name
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
        Glide.with(cover.context)
            .load(imageName)
            .apply(RequestOptions().override(500, 600))
            .into(cover)

        view.setText(book.view.toString());
        favorite.setText(book.have_loved.size.toString());
        chapter.setText(book.chapters.size.toString());
        val category = book.categories.take(3);
            for (i in category) {
            when (i.name){
                "Romance" -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!,
                        R.color.love
                    ));
                }
                "Fiction" -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                   category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!,
                       R.color.yellow_green
                   ));
                }
                "Short Story" -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                   category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!,
                       R.color.light_blue
                   ));
                }
                "Mystery" -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                    category_holder[category.indexOf(i)].backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!, R.color.violet));
                }
                "Thriller" -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                  category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!,
                      R.color.golden
                  ));
                }
                "Horror" -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!,
                        R.color.purple_500
                    ));
                }
                "Humor" -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!,
                        R.color.pink
                    ));
                }
                else -> {
                    category_holder[category.indexOf(i)].setText(i.name)
                    category_holder[category.indexOf(i)].backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(_currentContext!!,
                        R.color.lightgrey
                    ));
                }


            }
            }

        rest.setText(if ((book.categories.size -3)>0) "+ ${book.categories.size -3} more" else "");

        FirebaseAuthManager.auth.uid?.let { userViewModel.getUserById(it) { user ->
            run {
                _currentUser = user
                favourite = _currentUser?.let { book.islove(it.id) } == true
                if (favourite) {
                    love.setImageResource(R.drawable.love_clickable)
                } else {
                    love.setImageResource(R.drawable.love)
                }
            }
        }
        }
        love.setOnClickListener{
            favourite = !favourite;
            if (favourite) {
                _currentUser!!.love(book)
                book.love(_currentUser!!)
                love.setImageResource(R.drawable.love_clickable)
            }else {
                _currentUser!!.unLove(book)
                book.notlove(_currentUser!!)
                love.setImageResource(R.drawable.love)
            }
            bookViewModel.saveCurrentIsLove(book)
            userViewModel.saveHeartList(_currentUser!!)
        }
    }

}



