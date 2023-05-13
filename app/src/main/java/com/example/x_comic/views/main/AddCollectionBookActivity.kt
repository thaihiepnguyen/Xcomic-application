package com.example.x_comic.views.main

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.CollectionBookList
import com.example.x_comic.adapters.CollectionBookListAdd
import com.example.x_comic.models.*
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import jp.wasabeef.glide.transformations.BlurTransformation



class AddCollectionBookActivity : AppCompatActivity() {

    private val bookList: MutableList<ProductCheck> = mutableListOf()
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel

    var customBookListView: RecyclerView? = null;
    var numSelected: TextView? = null;
    var submitBtn: ImageButton? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_collection_book)

        numSelected = findViewById(R.id.num_selected);
        submitBtn = findViewById(R.id.check);

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


        bookListAdapter.onItemClick = {
            book, being_selected, cover ->
            run {
                book.check = !book.check;
                println(book.check)

                val imageName = book.product.cover

                if (book.check) {
                    bookListAdapter.bookSelectedList.add(book.product.id);
                    Glide.with(cover.context)
                        .load(imageName)
                        .apply(RequestOptions().override(500, 600))
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))

                        .into(cover)

                    cover.setColorFilter(
                        ContextCompat.getColor(
                        this,
                        R.color.black_trans),
                        PorterDuff.Mode.SRC_OVER
                    );

                    being_selected.visibility = View.VISIBLE


                   bookListAdapter.notifyDataSetChanged();
                    numSelected!!.setText("${bookListAdapter.bookSelectedList.size} Selected");

                }
                else {
                    bookListAdapter.bookSelectedList.remove(book.product.id);
                    Glide.with(cover.context)
                        .load(imageName)
                        .apply(RequestOptions().override(500, 600))
                        .into(cover)
                    cover.clearColorFilter()
                    being_selected.visibility = View.GONE
                    bookListAdapter.notifyDataSetChanged();
                    numSelected!!.setText("${bookListAdapter.bookSelectedList.size} Selected");
                }
            }
        }

        numSelected!!.setText("${bookListAdapter.bookSelectedList.size} Selected");
        val collectionName = intent.getStringExtra("name")
        submitBtn!!.setOnClickListener{
            println(bookListAdapter.bookSelectedList);
            val replyIntent = Intent()
            replyIntent.putExtra(
                "collection",
                CollectionReading(
                    collectionName!!,
                    bookListAdapter.bookSelectedList
                )
            );
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }

    }
}