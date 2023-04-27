package com.example.x_comic.views.read

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import com.example.x_comic.R

class ReadBookActivity : AppCompatActivity() {
    var sv_wrapper : ScrollView? = null
    var progressBarReadBook : ProgressBar? = null
    var textViewContentBook : TextView? = null
    var btnNext : ImageButton? = null
    var btnBack : ImageButton? = null

    private fun fadeInImageButton(imageButton: ImageButton?) {
        imageButton?.alpha = 0f
        imageButton?.visibility = View.VISIBLE

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                imageButton!!.alpha += 0.1f
                if (imageButton!!.alpha < 1f) {
                    handler.postDelayed(this, 300)
                }
            }
        }, 300)
    }

    private fun fadeOutImageButton(imageButton: ImageButton?) {
        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 1000
        imageButton?.startAnimation(animation)

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                imageButton?.visibility = View.INVISIBLE
            }
        }, 1000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_book)

        sv_wrapper = findViewById(R.id.sv_wrapper)
        progressBarReadBook = findViewById(R.id.progressBarReadBook)
        textViewContentBook = findViewById(R.id.tvContentChapterBook)
        btnNext = findViewById(R.id.btnNextChapter)
        btnBack = findViewById(R.id.btnBackChapter)
        val intent = intent
        val titleStr = intent.getStringExtra("title")
        var titleTv = findViewById<TextView>(R.id.tvTitleChapter)
        titleTv.text = titleStr

        val contentStr = intent.getStringExtra("content")

        textViewContentBook?.text = contentStr
        sv_wrapper?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            var totalScrollLength = sv_wrapper?.getChildAt(0)!!.height - sv_wrapper!!.height

            progressBarReadBook?.apply {
                max = totalScrollLength
                progress = scrollY
            }

        }

        textViewContentBook?.setOnClickListener {
            fadeInImageButton(btnNext)
            fadeInImageButton(btnBack)

            Handler().postDelayed({
                fadeOutImageButton(btnNext)
                fadeOutImageButton(btnBack)
            }, 4000)
        }

        btnNext?.setOnClickListener {
            // TODO: Chương tiếp theo
            recreate()
        }

        btnBack?.setOnClickListener {
            // TODO: Chương trước đó
            recreate()
        }
    }
}