package com.example.x_comic.views.collection

import android.app.Activity
import android.content.Intent
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
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.main.fragments.listCollection
import jp.wasabeef.glide.transformations.BlurTransformation

class EditCollectionActivity : AppCompatActivity() {

    var collectionName: TextView? = null;
    var storyNumber: TextView? = null;
    var numSelected: TextView? = null;
    var customBookListView: RecyclerView? = null;

    var btnDelete: ImageButton? = null;
    var btnAdd: ImageButton? = null;
    var btnClose: ImageButton? = null;
    var btnSave: ImageButton? =null;
    private lateinit var productViewModel: ProductViewModel
    private lateinit var bookListAdapter: CollectionBookListAdd

    private lateinit var cover : ImageView;
    private lateinit var thumbnail : ImageView;
    private val collectionBook: MutableList<ProductCheck> = mutableListOf();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_collection)

        collectionName = findViewById(R.id.collection_name);
        storyNumber = findViewById(R.id.story_number);
        btnDelete = findViewById(R.id.btnDelete);
        btnAdd = findViewById(R.id.btnAdd);
        btnClose = findViewById(R.id.btnClose);
        btnSave = findViewById(R.id.btnSave);

        numSelected = findViewById(R.id.num_selected);
        cover = findViewById(R.id.cover) as ImageView;
        thumbnail = findViewById(R.id.cover_thumbnail) as ImageView;


        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)




        customBookListView = findViewById(R.id.collectionBookList);
        bookListAdapter = CollectionBookListAdd(collectionBook);
        collectionBook.clear();
        customBookListView!!.adapter = bookListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)

        val position = intent.getIntExtra("position",0);
        val collection = intent!!.getSerializableExtra("collection") as? CollectionReading;
        if (collection!=null) {
            collectionName!!.setText(collection?.name);
            val storyText = if (collection?.bookList?.size == 1) "Story" else "Stories"
            storyNumber!!.text = "${collection?.bookList?.size.toString()} $storyText"
            for (i in collection.bookList){
                if (i!=null){
                    productViewModel.getBookById(i){
                            product ->
                        run {
                            if ( product!=null && !product.hide) {
                                if (collection.bookList.indexOf(i) == 0) {

                                    Glide.with(cover.context)
                                        .load(product.cover)
                                        .placeholder(R.drawable.empty_image)
                                        .apply(
                                            RequestOptions.bitmapTransform(
                                                BlurTransformation(
                                                    50,
                                                    3
                                                )
                                            )
                                        )
                                        .into(cover)


                                    Glide.with(thumbnail.context)
                                        .load(product.cover)
                                        .placeholder(R.drawable.empty_image)
                                        .apply(RequestOptions().override(500, 600))
                                        .into(thumbnail)


                                }
                                collectionBook.add(ProductCheck(product, false));
                                bookListAdapter.notifyDataSetChanged();
                            }
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
                        .placeholder(R.drawable.empty_image)
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
                        .placeholder(R.drawable.empty_image)
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
                if (collectionBook.size > 0) {
                    Glide.with(cover.context)
                        .load((collectionBook[0].product.cover))
                        .placeholder(R.drawable.empty_image)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                        .into(cover)


                    Glide.with(thumbnail.context)
                        .load((collectionBook[0].product.cover))
                        .placeholder(R.drawable.empty_image)
                        .apply(RequestOptions().override(500, 600))
                        .into(thumbnail)
                }
            else {

                    Glide.with(cover.context)
                        .load(R.drawable.empty_image)
                        .placeholder(R.drawable.empty_image)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                        .into(cover)


                    Glide.with(thumbnail.context)
                        .load(R.drawable.empty_image)
                        .placeholder(R.drawable.empty_image)
                        .apply(RequestOptions().override(500, 600))
                        .into(thumbnail)
                }

            val storyText = if (collectionBook.size == 1) "Story" else "Stories"
            storyNumber!!.text = "${collectionBook.size} $storyText"
                    bookListAdapter.bookSelectedList.clear();
                    print(collectionBook)
            numSelected!!.setText("${bookListAdapter.bookSelectedList.size} Selected");
                    bookListAdapter.notifyDataSetChanged();

        }


        btnAdd!!.setOnClickListener{
            val intent = Intent(this, AddBookEditCollectionActivity::class.java)
            intent.putExtra("collection", CollectionReading("",collectionBook.map { it -> it.product.id }.toMutableList()));
            startActivityForResult(intent,345);
        }


        btnClose!!.setOnClickListener{
            finish();
        }

        btnSave!!.setOnClickListener{
            FirebaseAuthManager.auth.uid?.let {
                val temp : MutableList<String> = mutableListOf();

                for (i in collectionBook){
                    temp.add(i.product.id);

                }
                productViewModel.updateBookListCollection(it,position,temp);

                val replyIntent = Intent()
                replyIntent.putExtra(
                    "collection",
                    CollectionReading(
                        "",
                        temp
                    )
                );
                setResult(Activity.RESULT_OK, replyIntent)

                finish();
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 345) {
            if (resultCode === Activity.RESULT_OK) {
                val reply = data!!.getSerializableExtra("collection") as? CollectionReading;
                if (reply != null) {
                    for (i in reply.bookList){
                        productViewModel.getBookById(i){
                            book -> run{
                            if ( book!=null && !book.hide) {
                                collectionBook.add(ProductCheck(book,false));

                                val storyText = if (collectionBook.size == 1) "Story" else "Stories"
                                storyNumber!!.text = "${collectionBook.size} $storyText"

                                Glide.with(cover.context)
                                    .load((collectionBook[0].product.cover))
                                    .placeholder(R.drawable.empty_image)
                                    .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                                    .into(cover)


                                Glide.with(thumbnail.context)
                                    .load((collectionBook[0].product.cover))
                                    .placeholder(R.drawable.empty_image)
                                    .apply(RequestOptions().override(500, 600))
                                    .into(thumbnail)
                                bookListAdapter.notifyDataSetChanged();
                            }

                        }
                        }
                    }

                }
            }
        }
    }
}