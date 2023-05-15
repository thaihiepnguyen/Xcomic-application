package com.example.x_comic.views.post

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate


class NewChapterActivity : AppCompatActivity() {
    var etContent : EditText? = null
    var tvCountWords : TextView? = null
    var chapter = Chapter()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chapter)

        chapter.id_chapter =  Firebase.database.getReference().push().key

        var intent = intent
        val _IdBook = intent.getStringExtra(Chapter.MESSAGE5) as String
        val position = intent.getIntExtra(Chapter.MESSAGE1, -1) as Int
        val _chapter = intent.getSerializableExtra(Chapter.MESSAGE3) as? Chapter
        chapter.id_book =  _IdBook
        _chapter?.let {
            findViewById<TextView>(R.id.titleChapter).setText("Edit Chapter")
            findViewById<EditText>(R.id.etTitleChapter).setText(_chapter.name)
            findViewById<EditText>(R.id.etContent).setText(_chapter.content)
            findViewById<Switch>(R.id.sLockChapter).isChecked = _chapter._lock
            tvCountWords?.text = etContent.toString().split("\\s+".toRegex()).size.toString() + " words"
            findViewById<ImageButton>(R.id.btn_menu).visibility = View.VISIBLE

            chapter = _chapter
        }

        etContent = findViewById(R.id.etContent)
        tvCountWords = findViewById(R.id.tvCountWords)
        var wordCount = 0

        etContent?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the word count every time the text changes
                val text = s.toString()
                val words = text.split("\\s+".toRegex())
                wordCount = words.size
                tvCountWords?.text = wordCount.toString() + " words"
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })

        var btnDone = findViewById<Button>(R.id.btnNext)
        btnDone.setOnClickListener {
            chapter.name = findViewById<EditText>(R.id.etTitleChapter).text.toString()
            chapter._lock = findViewById<Switch>(R.id.sLockChapter).isChecked
            chapter.content = findViewById<EditText>(R.id.etContent).text.toString()

            val currentDate = LocalDate.now()
            chapter.date_update = currentDate.toString()

            // Tra Chapter moi ve
            val replyIntent = Intent()
            replyIntent.putExtra(Chapter.MESSAGE2, chapter)
            replyIntent.putExtra(Chapter.MESSAGE4, position)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }

        val backButton = findViewById<ImageButton>(R.id.imgbtnBack)
        backButton.setOnClickListener {
            finish()
        }

        val btnMenu = findViewById<ImageButton>(R.id.btn_menu)
        val popupMenu = PopupMenu(this, btnMenu)
        popupMenu.menuInflater.inflate(R.menu.chapter_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.btnDeleteChapter -> {
                    // Perform action for Item 1
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Are you sure you want to delete?")
                    builder.setPositiveButton("Yes") { dialog, which ->
                        // Handle user's positive response
                        // Tra Chapter moi ve
                        val replyIntent = Intent()
                        replyIntent.putExtra(Chapter.MESSAGE2, chapter)
                        replyIntent.putExtra(Chapter.MESSAGE4, -1)
                        setResult(Activity.RESULT_OK, replyIntent)
                        finish()
                    }
                    builder.setNegativeButton("No") { dialog, which ->
                        // Handle user's negative response
                    }
                    val dialog = builder.create()
                    dialog.show()
                    true
                }
                else -> false
            }
        }

        btnMenu.setOnClickListener {
            popupMenu.show()
        }
    }
}