package com.example.x_comic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.Chapter

class ChaptersAdapter (private val chapters: MutableList<Chapter>) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {
    var onItemClick: ((chapter: Chapter, position: Int) -> Unit)? = null
    var onButtonClick: ((Chapter) -> Unit)? = null
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val name = listItemView.findViewById(R.id.tvChapterName) as TextView
        val dateUpdate = listItemView.findViewById(R.id.tvDate) as TextView
        val ivStatusChapter = listItemView.findViewById(R.id.ivStatusChapter) as ImageView

        init {
            listItemView.setOnClickListener { onItemClick?.invoke(chapters[adapterPosition], adapterPosition) }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val chapterView = inflater.inflate(R.layout.chapter_status, parent, false)
        // Return a new holder instance
        return ViewHolder(chapterView)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val c: Chapter = chapters.get(position)
        // Set item views based on your views and data model

        holder.name.text = chapters[position].name
        holder.dateUpdate.text = "updated " + chapters[position].date_update
        if (chapters[position]._lock) {
            holder.ivStatusChapter.setImageResource(R.drawable.baseline_lock_24)
        } else {
            holder.ivStatusChapter.setImageResource(R.drawable.ic_unlock)
        }
    }
}
