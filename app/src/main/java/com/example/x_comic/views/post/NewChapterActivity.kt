package com.example.x_comic.views.post

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import java.time.LocalDate

class NewChapterActivity : AppCompatActivity() {
    var etContent : EditText? = null
    var tvCountWords : TextView? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chapter)

        var intent = intent
        val id_book = intent.getStringExtra(Chapter.MESSAGE1) as String

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
            var chapter = Chapter()
            chapter.name = findViewById<EditText>(R.id.etTitleChapter).text.toString()
            chapter._lock = findViewById<Switch>(R.id.sLockChapter).isChecked
            chapter.content = findViewById<EditText>(R.id.etContent).text.toString()
            chapter.id_book = id_book

            val currentDate = LocalDate.now()
            chapter.date_update = currentDate.toString()

            // Tra Chapter moi ve
            val replyIntent = Intent()
            replyIntent.putExtra(Chapter.MESSAGE2, chapter)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }
}