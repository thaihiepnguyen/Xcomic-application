package com.example.x_comic.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.CollectionBookList
import com.example.x_comic.adapters.CollectionBookListAdd
import com.example.x_comic.models.Book
import com.example.x_comic.models.BookReading
import com.example.x_comic.models.Product
import com.example.x_comic.models.ProductCheck
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel


class AddCollectionBookActivity : AppCompatActivity() {

    private val bookList: MutableList<ProductCheck> = mutableListOf()
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel

    var customBookListView: RecyclerView? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_collection_book)


        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        customBookListView = findViewById(R.id.collectionBookList);
        val bookListAdapter = CollectionBookListAdd(bookList);
        customBookListView!!.adapter = bookListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {

            productViewModel.getAllReadingBook(uid) { booksID ->
                run {
                    bookList.clear();
                    var cnt: Int = 0


                    for (snapshot in booksID.children) {

                        var bookid = snapshot.getValue(com.example.x_comic.models.Reading::class.java)

                        productViewModel.getBookById(bookid!!.id_book) { bookInner ->
                            run {

                                if ( bookInner!=null && !bookInner.hide) {

                                    cnt++
                                    bookList.add(ProductCheck(bookInner,false))

                                    println(bookInner);

                                    bookListAdapter.notifyItemInserted(bookList.size);
                                    // OnlineAdapter.notifyDataSetChanged()
                                }
                                //     OnlineAdapter.notifyDataSetChanged()

                            }
                            //






                        }




                    }

                    bookListAdapter.notifyDataSetChanged();



                }


            }

        }




    }
}