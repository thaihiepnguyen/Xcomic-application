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
import com.example.x_comic.models.Chapter

class ChapterAdapter(
    private val context: Activity,
    private val chapters: List<Chapter>
) : ArrayAdapter<Chapter>(context, R.layout.list_chapter_item_layout, chapters) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.list_chapter_item_layout, null, true)
        val chapterTV = rowView.findViewById(R.id.tvChapterName) as TextView
        chapterTV.text = chapters[position].name
        val dateTV = rowView.findViewById(R.id.tvDate) as TextView
        dateTV.text = chapters[position].date_update
        return rowView
    }
}
