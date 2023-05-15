package com.example.x_comic.views.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.databinding.ActivityMyCollectionBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.Reading
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.detail.DetailActivity

class ReadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCollectionBinding
    private val bookList: MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backImg.setOnClickListener {
            finish()
        }

        var productViewModel: ProductViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        val bookListAdapter = BookListAdapter(bookList)
        bookListAdapter.onItemClick = {
                book -> nextBookDetailActivity(book)
        }

        binding.listView!!.adapter = bookListAdapter;
        binding.listView!!.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        productViewModel.getAllReadingBook(FirebaseAuthManager.auth.uid!!) {
                list -> run {
            bookList.clear()
            val readings = ArrayList<Reading>()
            var cnt: Int = 0
            for (item in list.children) {
                var reading = item.getValue(Reading::class.java)
                if (reading != null) {
                    readings.add(reading)
                }
                productViewModel.getBookById(reading?.id_book as String) {
                        book -> run {
                    if (!book.hide) {
                        cnt++
                        bookList.add(book)
                    }
                    }
                    bookListAdapter.notifyDataSetChanged()
                }
                bookListAdapter.notifyDataSetChanged()
            }
        }
        }
    }

    fun nextBookDetailActivity(book: Product) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("book_data", book.id)

        startActivity(intent)
    }
}