package com.example.x_comic.views.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.adapters.BookManagementListAdapter
import com.example.x_comic.databinding.ActivityBookManagementBinding
import com.example.x_comic.databinding.ActivityChangePennameBinding
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.ProductViewModel

class BookManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookManagementBinding
    private lateinit var productViewModel: ProductViewModel
    val listBook: MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookManagementBinding.inflate(layoutInflater)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        setContentView(binding.root)

        binding.backImg.setOnClickListener {
            finish()
        }

        binding.listBook.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val bookListAdapter = BookManagementListAdapter(listBook)

        binding.listBook.adapter = bookListAdapter

        productViewModel.getAllBook {books -> kotlin.run {
            listBook.clear()
            for (book in books.children) {
                val product = book.getValue(Product::class.java)
                if (product != null) {
                    listBook.add(product)
                }
            }
            bookListAdapter.notifyDataSetChanged()
        }
        }
    }
}