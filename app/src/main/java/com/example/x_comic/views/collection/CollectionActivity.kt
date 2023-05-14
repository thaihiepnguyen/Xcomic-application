package com.example.x_comic.views.collection

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.CollectionBookList
import com.example.x_comic.models.CollectionReading
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.views.main.fragments.CollectionDialogFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import jp.wasabeef.glide.transformations.BlurTransformation


var tempCollection: MutableList<CollectionReading> = mutableListOf();
var collectionName: TextView? = null;
class CollectionActivity : AppCompatActivity() {



    var customBookListView: RecyclerView? = null;
    lateinit var collection: CollectionReading
    lateinit var bookListAdapter: CollectionBookList
    var storyNumber: TextView? = null;
    lateinit var productViewModel: ProductViewModel
    private val collectionBook: MutableList<Product> = mutableListOf();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val cover = findViewById(R.id.cover) as ImageView;
        val thumbnail = findViewById(R.id.cover_thumbnail) as ImageView;
        val btnOption = findViewById<ImageButton>(R.id.btnOption)
        val btnReturn = findViewById<ImageButton>(R.id.btnReturn);
        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collectionName = findViewById(R.id.collection_name);
        storyNumber = findViewById(R.id.story_number);
        val position = intent!!.getIntExtra("position",0);



        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        setSupportActionBar(toolbar)
        customBookListView = findViewById(R.id.collectionBookList);
        bookListAdapter = CollectionBookList(collectionBook);
        collectionBook.clear();
        customBookListView!!.adapter = bookListAdapter;

        customBookListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customBookListView?.addItemDecoration(itemDecoration)

        collection = intent!!.getSerializableExtra("collection") as CollectionReading;
        if (collection!=null) {
            collectionName!!.setText(collection?.name);
            val storyText = if (collection?.bookList?.size == 1) "Story" else "Stories"
            storyNumber!!.text = "${collection?.bookList?.size.toString()} $storyText"
            for (i in collection.bookList){
                if (i!=null){
                    productViewModel.getBookById(i){
                            product ->
                        run {
                            if (collection.bookList.indexOf(i) == 0){

                                Glide.with(cover.context)
                                    .load(product.cover)
                                    .placeholder(R.drawable.empty_image)
                                    .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                                    .into(cover)


                                Glide.with(thumbnail.context)
                                    .load(product.cover)
                                    .placeholder(R.drawable.empty_image)
                                    .apply(RequestOptions().override(500, 600))
                                    .into(thumbnail)


                            }
                            collectionBook.add(product);
                            bookListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                bookListAdapter.notifyDataSetChanged();
            }
        }




        FirebaseAuthManager.auth.uid?.let {
            tempCollection.clear();
            productViewModel.getAllCollection(it) {

                    collect ->
                run {
                    for (snapshot in collect.children) {

                        var bookid = snapshot.getValue(CollectionReading::class.java)
                        if (bookid != null) {
                            tempCollection.add(bookid);
                        }
                    }
                }
            }
        }








        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout?.totalScrollRange ?: 0
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = collectionName!!.text // Set an empty title when fully collapsed
                    btnOption.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                        R.color.black))

                    btnReturn.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                            R.color.black))

                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = "" // Set your desired title when fully expanded
                    btnOption.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                            R.color.white))

                    btnReturn.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this@CollectionActivity,
                            R.color.white))

                    isShow = false
                }
            }
        })



        btnReturn.setOnClickListener{
            finish();
        }
        btnOption.setOnClickListener { v ->
            showPopup(v,position, collection!!.name);
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 246){
            if (resultCode === Activity.RESULT_OK) {
                val reply = data!!.getSerializableExtra("collection") as? CollectionReading;
                if (reply != null) {
                    collectionBook.clear();
                    for (i in reply.bookList){
                        productViewModel.getBookById(i){
                            product ->
                            run {

                                if (product!=null){
                                    collectionBook.add(product);
                                    bookListAdapter.notifyDataSetChanged();


                                }
                            }
                        }
                    }

                    bookListAdapter.notifyDataSetChanged();
                    val storyText = if (collection?.bookList?.size == 1) "Story" else "Stories"
                    storyNumber!!.text = "${collection?.bookList?.size.toString()} $storyText"


                }
            }

        }
    }

    private fun showPopup(v: View, position: Int, name: String="") {

        println(position)
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.option_collection_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete -> {


                    var bookViewModel: ProductViewModel = ProductViewModel();
                    tempCollection.removeAt(position);
                    FirebaseAuthManager.auth.uid?.let {
                        bookViewModel.updateCollection(it, tempCollection);
                    }


                    finish();
                    true
                }
                R.id.rename -> {
                    val dialog = CollectionDialogFragment("Rename a Collection",name)
                    dialog.show(supportFragmentManager,"dbchau10");

                    dialog.setFragmentResultListener("1") { key, bundle ->
                        if (key == "1") {

                            val txt = bundle.getString("1")!!
                            tempCollection[position].name = txt;

                            collectionName?.setText(txt);

                            var bookViewModel: ProductViewModel = ProductViewModel()
                            FirebaseAuthManager.auth.uid?.let {
                                bookViewModel.updateCollection(it, tempCollection);
                            }



                        }
                    }
                    true
                }
                R.id.edit -> {
                    val intent = Intent(this, EditCollectionActivity::class.java)
                    intent.putExtra("collection", collection)
                    intent.putExtra("position",position);
                    startActivityForResult(intent,246)

                    true
                }
                else -> true
            }
        }
        popup.show()
    }



}