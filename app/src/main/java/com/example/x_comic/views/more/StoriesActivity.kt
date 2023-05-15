package com.example.x_comic.views.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.databinding.ActivityFavoriteBinding
import com.example.x_comic.databinding.ActivityStoriesBinding
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.detail.DetailActivity

class StoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoriesBinding
    private val bookList: MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        var bundle = Bundle()
        bundle = intent.extras!!

        var author: User = bundle.get("authorKey") as User;

        var productViewModel: ProductViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        val bookListAdapter = BookListAdapter(bookList)
        bookListAdapter.onItemClick = {
                book -> nextBookDetailActivity(book)
        }

        binding.listView!!.adapter = bookListAdapter;
        binding.listView!!.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        productViewModel.getAllCollectionBook(author.id) {
                list -> run {
            bookList.clear()
            var cnt: Int = 0
            for (snapshot in list.children) {
                val productid = snapshot.getValue(String::class.java)
                if (productid != null) {
                    productViewModel.getBookById(productid) { book -> run {
                        cnt++
                        if (!book.hide)
                            bookList.add(book)
                    }
                        bookListAdapter.notifyDataSetChanged()
                    }
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