package com.example.x_comic.views.collection

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
import com.example.x_comic.adapters.CollectionBookListAdd
import com.example.x_comic.models.CollectionReading
import com.example.x_comic.models.Product
import com.example.x_comic.models.ProductCheck
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class AddBookEditCollectionActivity : AppCompatActivity() {

    private val bookList: MutableList<ProductCheck> = mutableListOf()
    private val bookList2: MutableList<ProductCheck> = mutableListOf()
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel

    var customBookListView: RecyclerView? = null;
    var customRecListView: RecyclerView? = null;
    var numSelected: TextView? = null;
    var submitBtn: ImageButton? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book_edit_collection)
        numSelected = findViewById(R.id.num_selected);
        submitBtn = findViewById(R.id.check);

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        customBookListView = findViewById(R.id.collectionBookList);
        customRecListView = findViewById(R.id.collectionBookList2)


        val bookListAdapter = CollectionBookListAdd(bookList);
        val recListAdapter = CollectionBookListAdd(bookList2);

        customBookListView!!.adapter = bookListAdapter;
        customRecListView!!.adapter = recListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)


        customRecListView!!.layoutManager = LinearLayoutManager(this);
        customRecListView?.addItemDecoration(itemDecoration)

        val collection = intent!!.getSerializableExtra("collection") as? CollectionReading;
        if (collection!=null) {

            val uid = FirebaseAuthManager.auth.uid
            if (uid != null) {

                productViewModel.getAllReadingBook(uid) { booksID ->
                    run {
                        bookList.clear();



                        for (snapshot in booksID.children) {

                            var bookid =
                                snapshot.getValue(com.example.x_comic.models.Reading::class.java)
                            if (!collection.bookList.contains(bookid!!.id_book)) {
                                productViewModel.getBookById(bookid!!.id_book) { bookInner ->
                                    run {

                                        if (bookInner != null && !bookInner.hide) {


                                            bookList.add(ProductCheck(bookInner, false))
                                            println(bookInner);

                                            bookListAdapter.notifyItemInserted(bookList.size);
                                            // OnlineAdapter.notifyDataSetChanged()
                                        }
                                        //     OnlineAdapter.notifyDataSetChanged()

                                    }
                                    //


                                }
                            }


                        }
                        for (it in bookList){
                            if (bookList2.contains(it)){
                                bookList2.remove(it)
                            }
                        }

                        bookListAdapter.notifyDataSetChanged();


                    }
                    productViewModel.getPopularBook(){
                            books ->
                        run {
                            bookList2.clear()

                            for (book in books.children) {
                                val product = book.getValue(Product::class.java)
                                if (product != null && !product.hide &&  !collection.bookList.contains(product.id)) {


                                    bookList2.add(ProductCheck(product, false))

                                    recListAdapter.notifyItemInserted(bookList2.size);

                                }
                            }

                            for (it in bookList){
                                if (bookList2.contains(it)){
                                    bookList2.remove(it)
                                }
                            }
                            recListAdapter.notifyDataSetChanged();
                        }

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
                    numSelected!!.setText("${bookListAdapter.bookSelectedList.size + recListAdapter.bookSelectedList.size} Selected");

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
                    numSelected!!.setText("${bookListAdapter.bookSelectedList.size + recListAdapter.bookSelectedList.size} Selected");
                }
            }
        }
            recListAdapter.onItemClick = {
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
                        numSelected!!.setText("${bookListAdapter.bookSelectedList.size + recListAdapter.bookSelectedList.size} Selected");

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
                        numSelected!!.setText("${bookListAdapter.bookSelectedList.size + recListAdapter.bookSelectedList.size} Selected");
                    }
                }
            }

        numSelected!!.setText("${bookListAdapter.bookSelectedList.size + recListAdapter.bookSelectedList.size} Selected");

        submitBtn!!.setOnClickListener{
            val temp: MutableList<String> = mutableListOf()
           temp.addAll(bookListAdapter.bookSelectedList)
            temp.addAll(recListAdapter.bookSelectedList);
            println(bookListAdapter.bookSelectedList);
            val replyIntent = Intent()
            replyIntent.putExtra(
                "collection",
                CollectionReading(
                    "",
                    temp
                )
            );
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }
}}