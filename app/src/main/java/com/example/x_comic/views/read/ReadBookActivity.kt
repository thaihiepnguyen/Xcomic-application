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

        textViewContentBook?.text = "Once upon a time, in a far-off land, there was a secret garden. It was hidden away from the rest of the world, and only a select few knew of its existence. The garden was said to be magical, and those who entered it would be blessed with good luck and happiness.\n" +
                "\n" +
                "One day, a young girl named Lily stumbled upon the secret garden. She was wandering through the woods, lost and alone, when she saw a small door nestled between two trees. Curious, she approached the door and found that it was unlocked. She pushed it open and stepped inside.\n" +
                "\n" +
                "As soon as she entered the garden, Lily felt a sense of peace wash over her. The garden was filled with vibrant flowers of every color, and the air was sweet with the scent of blooming roses. She walked through the winding paths, taking in the beauty around her.\n" +
                "\n" +
                "Suddenly, she heard a rustling sound and turned to see a small animal darting through the bushes. Lily followed it, and soon she found herself in a clearing. In the center of the clearing was a fountain, and beside the fountain sat an old man.\n" +
                "\n" +
                "The old man smiled at Lily and beckoned her over. \"Welcome to the secret garden,\" he said. \"I am the keeper of this place. What brings you here?\"\n" +
                "\n" +
                "Lily explained that she had been lost in the woods and had stumbled upon the garden by accident. The old man nodded understandingly. \"Many people find their way here when they need it most,\" he said. \"This garden is a place of healing and renewal.\"\n" +
                "\n" +
                "Over the next few days, Lily explored the garden and spent time with the old man. He taught her the secrets of the garden and showed her how to tend to the plants. Lily felt more at peace than she had in a long time, and she knew that the garden had worked its magic on her.\n" +
                "\n" +
                "Eventually, it was time for Lily to leave the secret garden and return to the real world. But she knew that she would never forget the garden or the old man who had shown her such kindness. And she knew that she would always carry a little piece of the magic with her, wherever she went."

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