package com.example.x_comic.views.purchase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import jp.wasabeef.glide.transformations.BlurTransformation

class PurchaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val backCover = findViewById<ImageView>(R.id.background)
        backCover.setImageResource(R.drawable.bookcover);
        Glide.with(this)
            .load(R.drawable.bookcover)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
            .into(backCover)
    }
}