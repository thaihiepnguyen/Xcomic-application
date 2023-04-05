package com.example.x_comic.views.detail

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.views.main.fragments.*
import jp.wasabeef.glide.transformations.BlurTransformation



class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        //button back to previous activity
        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val backCover = findViewById<ImageView>(R.id.background)
        backCover.setImageResource(R.drawable.bookcover);
        Glide.with(this)
            .load(R.drawable.bookcover)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
            .into(backCover)
        //open dialog choose chapter to read
        val chooseBtn = findViewById<Button>(R.id.chooseChapterBtn)
        chooseBtn.setOnClickListener {
            val dl = Dialog(this)
            dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dl.setContentView(R.layout.list_chapter_dialog)
            dl.show()

            var chapterListView = dl.findViewById<ListView>(R.id.chapterListView)

            var chapters = listOf<String>("Chapter 1","Chapter 2","Chapter 3","Chapter 4");

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, chapters)
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