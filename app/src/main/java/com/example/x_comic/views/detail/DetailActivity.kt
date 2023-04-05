package com.example.x_comic.views.detail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
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