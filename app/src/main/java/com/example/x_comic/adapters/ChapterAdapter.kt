package com.example.x_comic.adapters

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.x_comic.R

class ChapterAdapter(
    private val context: Activity,
    private val chapters: List<String>
) : ArrayAdapter<String>(context, R.layout.chapter_status, chapters) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.chapter_status, null, true)
        val chapterTV = rowView.findViewById(R.id.tvChapterName) as TextView
        chapterTV.text = chapters[position]
        return rowView
    }
}
