package com.example.x_comic.views.post

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookAuthor
import com.example.x_comic.models.Chapter
import com.example.x_comic.views.main.fragments.Writing

class MultiSelectSpinnerAdapter(context: Context, items: Array<String>) :
    ArrayAdapter<String>(context, R.layout.spinner_item_layout, R.id.item_textView, items), SpinnerAdapter {

    private val selectedItems = BooleanArray(items.size)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item_layout, parent, false)

        val textView: TextView = view.findViewById(R.id.item_textView)
        textView.text = getItem(position)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
        checkBox.isChecked = selectedItems[position]
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            selectedItems[position] = isChecked
        }

        return view
    }

    fun getSelectedItems(): BooleanArray = selectedItems
}
class PostNewActivity : AppCompatActivity() {

    private lateinit var adapter: MultiSelectSpinnerAdapter
    private lateinit var spinner: Spinner

    var chapterList: MutableList<Chapter> = mutableListOf(
        Chapter("Chapter 1", "updated 1/1/2000"),
        Chapter("Chapter 2", "updated 1/1/2000"),
        Chapter("Chapter 3", "updated 1/1/2000"),
        Chapter("Chapter 4", "updated 1/1/2000"),
        Chapter("Chapter 5", "updated 1/1/2000"),
        Chapter("Chapter 6", "updated 1/1/2000"),
        )
    var customChapterListView: RecyclerView? = null;

    class ChaptersAdapter (private val chapters: MutableList<Chapter>) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {
        var onItemClick: ((chapter: Chapter, position: Int) -> Unit)? = null
        var onButtonClick: ((Chapter) -> Unit)? = null
        inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
            val name = listItemView.findViewById(R.id.tvChapterName) as TextView
            val dateUpdate = listItemView.findViewById(R.id.tvDate) as TextView

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
            holder.dateUpdate.text = chapters[position].data_update
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_new)

        spinner = findViewById(R.id.spCategory)

        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
        adapter = MultiSelectSpinnerAdapter(this, items)
        spinner.adapter = adapter

        val nextButton = findViewById<Button>(R.id.btnNext)
        nextButton.setOnClickListener {
            val selectedItems = adapter.getSelectedItems()
            val selection = StringBuilder()
            for (i in items.indices) {
                println(i)
                if (selectedItems[i]) {
                    selection.append(items[i]).append(", ")
                }
            }
            Toast.makeText(this, "Selected Items: ${selection}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NewChapterActivity::class.java)
            startActivity(intent)

        }

        customChapterListView = findViewById(R.id.listViewChapter) as RecyclerView;
        val chapterListAdapter = ChaptersAdapter(chapterList);
        customChapterListView!!.adapter = chapterListAdapter;
        customChapterListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customChapterListView?.addItemDecoration(itemDecoration)

        chapterListAdapter!!.onItemClick = {chapter, position ->
            // DO SOMETHING
        }

    }
}