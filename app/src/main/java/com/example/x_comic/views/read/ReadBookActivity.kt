package com.example.x_comic.views.read

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.ChapterAdapter
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Product
import com.example.x_comic.views.purchase.PurchaseActivity
import com.google.firebase.storage.FirebaseStorage
import jp.wasabeef.glide.transformations.BlurTransformation

open class OnSwipeTouchListener(context: Context?) : View.OnTouchListener {
    private val gestureDetector: GestureDetector
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 1
        private val SWIPE_VELOCITY_THRESHOLD = 1

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent,
            velocityX: Float, velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffX = e2.x - e1.x
                val diffY = e2.y - e1.y
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) onSwipeRight() else onSwipeLeft()
                        result = true
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}


class ReadBookActivity : AppCompatActivity() {
    var sv_wrapper : ScrollView? = null
    var progressBarReadBook : ProgressBar? = null
    var textViewContentBook : TextView? = null
    var btnNext : ImageButton? = null
    var btnBack : ImageButton? = null
    var book : Product? = null
    var curChapter = Chapter()

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

    private fun getPreviousChapter () : Int {
        var index = book!!.chapters.indexOf(curChapter);
        for (i in book!!.chapters.reversed()) {
            if (!i._lock && book!!.chapters.indexOf(i) < book!!.chapters.indexOf(curChapter))
                return book!!.chapters.indexOf(i);
        }

        return index;
    }

    private fun getNextChapter () : Int {
        var index = book!!.chapters.indexOf(curChapter);
        for (i in book!!.chapters) {
            if (!i._lock && book!!.chapters.indexOf(i) > book!!.chapters.indexOf(curChapter))
                return book!!.chapters.indexOf(i);
        }

        return index;
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
        book = intent.getSerializableExtra("book") as? Product
        val id_chapter = intent.getStringExtra("id_chapter") as String

        book?.let {
            var titleTv = findViewById<TextView>(R.id.tvTitleChapter)

            for (i in book!!.chapters)
                if (i.id_chapter!!.equals(id_chapter))
                    curChapter = i
            titleTv.text = curChapter.name
            textViewContentBook?.text = curChapter.content
            findViewById<TextView>(R.id.viewTextView).text = book?.view.toString() + " view";
            findViewById<TextView>(R.id.favoriteTextView).text = book?.favorite.toString() + "K favor";
            findViewById<TextView>(R.id.numOfChapterTextView).text = book?.chapters?.size.toString() + " Chapters"

            var cover = findViewById(R.id.ivCoverChapter) as ImageView;
            val storage = FirebaseStorage.getInstance()
            val imageName = book!!.cover // Replace with your image name
            val imageRef = storage.reference.child("book_cover/$imageName")
            imageRef.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    // Set the Bitmap to the ImageView
                    cover.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    // Handle any errors
                }
        }


        findViewById<TextView>(R.id.numOfChapterTextView).setOnClickListener {
            val dl = Dialog(this)
            dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dl.setContentView(R.layout.list_chapter_dialog)
            dl.show()
            var bgcover = dl.findViewById<ImageView>(R.id.background)
            var minicover = dl.findViewById<ImageView>(R.id.book)
            var minititle = dl.findViewById<TextView>(R.id.book_title)
            val storage = FirebaseStorage.getInstance()
            val imageName = book!!.cover // Replace with your image name
            val imageRef = storage.reference.child("book_cover/$imageName")
            minititle.text = book?.title
            imageRef.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    // Set the Bitmap to the ImageView
                    minicover.setImageBitmap(bitmap)


                    Glide.with(this)
                        .load(bitmap)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                        .into(bgcover)
                }.addOnFailureListener {
                    // Handle any errors
                }



            var chapterListView = dl.findViewById<ListView>(R.id.chapterListView)
            val adapter = ChapterAdapter(this, book!!.chapters)
            chapterListView.adapter = adapter
            chapterListView.setOnItemClickListener { adapterView, view, i, l ->
                //TODO: Code doc sach khi click vo chapter o day ne
                if(!book!!.chapters[i]._lock){
                    val intent = Intent(this, ReadBookActivity::class.java)
                    intent.putExtra("book",book)
                    intent.putExtra("id_chapter",book!!.chapters[i].id_chapter)
                    ActivityCompat.startActivityForResult(this, intent, 302, null)
                    finish()
                }else{
                    val intent = Intent(this, PurchaseActivity::class.java)
                    intent.putExtra("bookdata", Klaxon().toJsonString(book))
                    intent.putExtra("chapter", Klaxon().toJsonString(book!!.chapters[i]))
                    ActivityCompat.startActivityForResult(this, intent, 700, null)
                    finish()
                }

            }
        }

        textViewContentBook?.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                // Xử lý sự kiện lướt từ phải qua trái ở đây
                val intent = Intent(this@ReadBookActivity, ReadBookActivity::class.java)
                intent.putExtra("book",book)
                intent.putExtra("id_chapter",book!!.chapters[getPreviousChapter()].id_chapter)
                ActivityCompat.startActivityForResult(this@ReadBookActivity, intent, 302, null)
                finish()
            }

            override fun onSwipeLeft() {
                // Xử lý sự kiện lướt từ phải qua trái ở đây
                val intent = Intent(this@ReadBookActivity, ReadBookActivity::class.java)
                intent.putExtra("book",book)
                intent.putExtra("id_chapter",book!!.chapters[getNextChapter()].id_chapter)
                ActivityCompat.startActivityForResult(this@ReadBookActivity, intent, 302, null)
                finish()
            }
        })

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