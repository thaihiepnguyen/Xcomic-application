package com.example.x_comic.views.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.example.x_comic.R

class NewChapterActivity : AppCompatActivity() {
    var etContent : EditText? = null
    var tvCountWords : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chapter)

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
    }
}