package com.example.x_comic.views.detail

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.CategoryAdapter
import com.example.x_comic.adapters.ChapterAdapter
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.main.fragments.*
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import jp.wasabeef.glide.transformations.BlurTransformation



class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = intent
        val stringData = intent.getStringExtra("book_data")
        val bookData = Product.fromString(stringData!!)

        var title = findViewById(R.id.book_title) as TextView;
        var author = findViewById(R.id.book_author) as TextView;
        var cover = findViewById(R.id.book_cover) as ImageView;
        var view = findViewById(R.id.viewTextView) as TextView;
        var favorite = findViewById(R.id.favoriteTextView) as TextView;
        var chapter = findViewById(R.id.numOfChapterTextView) as TextView;

        var categoryView = findViewById(R.id.category_list) as RecyclerView
        var rest = findViewById(R.id.rest) as TextView;
        var status = findViewById(R.id.statusTV) as TextView;
        var age = findViewById(R.id.ageTV) as TextView;
        var ratingTextView = findViewById(R.id.ratingTV) as TextView;
        var chooseChapterBtn = findViewById(R.id.chooseChapterBtn) as Button;

        title.text = bookData.title
        author.text = bookData.author
        val backCover = findViewById<ImageView>(R.id.background)
        // Get a reference to the Firebase Storage instance
        val storage = FirebaseStorage.getInstance()
        val imageName = bookData.cover // Replace with your image name
        val imageRef = storage.reference.child("book_cover/$imageName")
        imageRef.getBytes(Long.MAX_VALUE)
            .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                // Set the Bitmap to the ImageView
                cover.setImageBitmap(bitmap)


                Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                    .into(backCover)
            }.addOnFailureListener {
                // Handle any errors
            }
        view.text = bookData.view.toString()
        chapter.text = bookData.chapters.size.toString()
        if (bookData.status){
            status.text = "Status: Done"
        }else{
            status.text = "Status: Ongoing"
        }
        age.text = "16+"
        ratingTextView.text = bookData.rating.toString()

        val category_list: MutableList<String> = mutableListOf()
        for (chapter in bookData.categories) {
            category_list.add(chapter.name)
        }
        val adapter = CategoryAdapter(category_list);
        categoryView!!.adapter = adapter;
        val layoutManager = FlexboxLayoutManager(this);
        layoutManager!!.flexWrap = FlexWrap.WRAP;
        layoutManager!!.flexDirection = FlexDirection.ROW;
        layoutManager!!.alignItems = AlignItems.FLEX_START;
        categoryView!!.layoutManager = layoutManager;


        chooseChapterBtn.setText("☰ " +  bookData.chapters.size.toString()  +" Chapters")

        //button back to previous activity
        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }


        //open dialog choose chapter to read
        val chooseBtn = findViewById<Button>(R.id.chooseChapterBtn)
        chooseBtn.setOnClickListener {
            val dl = Dialog(this)
            dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dl.setContentView(R.layout.list_chapter_dialog)
            dl.show()

            var chapterListView = dl.findViewById<ListView>(R.id.chapterListView)

            val adapter = ChapterAdapter(this, bookData.chapters)
            chapterListView.adapter = adapter
            chapterListView.setOnItemClickListener { adapterView, view, i, l ->

            }
        }
        //open dialog feedback and rating
        val ratingBtn = findViewById<Button>(R.id.ratingBtn)
        ratingBtn.setOnClickListener {
            val dl = Dialog(this)
            dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dl.setContentView(R.layout.dialog_fb_rating)
            dl.show()

            val ratingBar = dl.findViewById<RatingBar>(R.id.ratingBar)
            val ratingTextView = dl.findViewById<TextView>(R.id.ratingTextView)
            val submitBtn = dl.findViewById<Button>(R.id.submitBtn)

            ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
                ratingTextView.text = String.format("(%s)", fl)
            }

            submitBtn.setOnClickListener {
                dl.dismiss()
            }

        }
    }
}