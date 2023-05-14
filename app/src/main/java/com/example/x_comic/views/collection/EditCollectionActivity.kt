package com.example.x_comic.views.collection

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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
import com.example.x_comic.models.CollectionReading
import com.example.x_comic.models.Product
import com.example.x_comic.models.ProductCheck
import com.example.x_comic.viewmodels.ProductViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class EditCollectionActivity : AppCompatActivity() {

    var collectionName: TextView? = null;
    var storyNumber: TextView? = null;
    var numSelected: TextView? = null;
    var customBookListView: RecyclerView? = null;

    var btnDelete: ImageButton? = null;
    private val collectionBook: MutableList<ProductCheck> = mutableListOf();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_collection)

        collectionName = findViewById(R.id.collection_name);
        storyNumber = findViewById(R.id.story_number);
        btnDelete = findViewById(R.id.btnDelete);

        numSelected = findViewById(R.id.num_selected);
        val cover = findViewById(R.id.cover) as ImageView;
        val thumbnail = findViewById(R.id.cover_thumbnail) as ImageView;

        var productViewModel: ProductViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)




        customBookListView = findViewById(R.id.collectionBookList);
        var bookListAdapter = CollectionBookListAdd(collectionBook);
        collectionBook.clear();
        customBookListView!!.adapter = bookListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)


        val collection = intent!!.getSerializableExtra("collection") as? CollectionReading;
        if (collection!=null) {
            collectionName!!.setText(collection?.name);
            storyNumber!!.setText(collection?.bookList!!.size.toString());
            for (i in collection.bookList){
                if (i!=null){
                    productViewModel.getBookById(i){
                            product ->
                        run {
                            if (collection.bookList.indexOf(i) == 0){

                                Glide.with(cover.context)
                                    .load(product.cover)
                                    .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                                    .into(cover)


                                Glide.with(thumbnail.context)
                                    .load(product.cover)
                                    .apply(RequestOptions().override(500, 600))
                                    .into(thumbnail)


                            }
                            collectionBook.add(ProductCheck(product,false));
                            bookListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                bookListAdapter.notifyDataSetChanged();
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


        btnDelete!!.setOnClickListener{

            val iterator = collectionBook.iterator()
            while (iterator.hasNext()) {
                val j = iterator.next()
                if (bookListAdapter.bookSelectedList.contains(j.product.id)) {
                    val currentIndex = collectionBook.indexOf(j)
                    iterator.remove()


                }
            }

                Glide.with(cover.context)
                    .load((collectionBook[0].product.cover))
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                    .into(cover)


                Glide.with(thumbnail.context)
                    .load((collectionBook[0].product.cover))
                    .apply(RequestOptions().override(500, 600))
                    .into(thumbnail)

                storyNumber!!.setText(collectionBook.size.toString());
                    bookListAdapter.bookSelectedList.clear();
                    print(collectionBook)

                    bookListAdapter.notifyDataSetChanged();

        }


    }
}