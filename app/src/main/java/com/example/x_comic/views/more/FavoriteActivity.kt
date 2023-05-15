package com.example.x_comic.views.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.adapters.BookListAdapter
import com.example.x_comic.databinding.ActivityCompletedBinding
import com.example.x_comic.databinding.ActivityFavoriteBinding
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val bookList: MutableList<Product> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
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

        productViewModel.getAllBookIsLoved(FirebaseAuthManager.auth.uid!!) {
                booksID -> run {
            bookList.clear()
            var cnt: Int = 0
            for (bookid in booksID.children) {
                productViewModel.getBookById(bookid.value as String) {
                        book -> run {
                    if (book.islove(FirebaseAuthManager.auth.uid!!) && !book.hide) {
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