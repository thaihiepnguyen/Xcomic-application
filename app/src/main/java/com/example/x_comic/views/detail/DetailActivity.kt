package com.example.x_comic.views.detail

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.CategoryAdapter
import com.example.x_comic.adapters.ChapterAdapter
import com.example.x_comic.adapters.FeedbackAdapter
import com.example.x_comic.adapters.ListAdapterSlideshow
import com.example.x_comic.models.Feedback
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.main.fragments.*
import com.example.x_comic.views.purchase.PurchaseActivity
import com.example.x_comic.views.read.ReadBookActivity
import com.facebook.appevents.AppEventsLogger
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import jp.wasabeef.glide.transformations.BlurTransformation
import java.lang.Math.min
import java.lang.Math.round
import kotlin.math.roundToInt


class DetailActivity : AppCompatActivity() {
    private var _currentUser: User? = null
    // TODO: biến đồng bộ với firebase
    private var _currentBook: Product? = null
    var favourite = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var productViewModel: ProductViewModel
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        var userViewModel: UserViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        // TODO: nhanh hơn
//        var bundle = Bundle()
//        bundle = intent.extras!!
//
//        var bookData: Product = bundle.get("productKey") as Product;
        val stringData = intent.getStringExtra("book_data")
        val bookData = stringData?.let{ Klaxon().parse<Product>(it)}

        var title = findViewById(R.id.book_title) as TextView;
        var author = findViewById(R.id.book_author) as TextView;
        var cover = findViewById(R.id.book_cover) as ImageView;
        var view = findViewById(R.id.viewTextView) as TextView;
        var favorite = findViewById(R.id.favoriteTextView) as TextView;
        var chapter = findViewById(R.id.numOfChapterTextView) as TextView;

        var categoryView = findViewById(R.id.category_list) as RecyclerView
        var rest = findViewById(R.id.rest) as TextView;
        var status = findViewById(R.id.statusTV) as TextView;
        var age = findViewById(R.id.ageTV) as TextView;
        var descTextView = findViewById(R.id.descTextView) as TextView
        var ratingTextView = findViewById(R.id.ratingTV) as TextView;
        var chooseChapterBtn = findViewById(R.id.chooseChapterBtn) as Button;
        var favorBtn = findViewById(R.id.favorBtn) as Button

        val backCover = findViewById<ImageView>(R.id.background)
        title.text = bookData?.title
        author.text = bookData?.author
        favorite.text = bookData?.favorite.toString()

        val uid = FirebaseAuthManager.auth.uid
        userViewModel.callApi(uid!!).observe(this, Observer {
            user -> _currentUser = user
        })

        productViewModel.getBookById(bookData!!.id) { product ->
            // TODO: realtime
            _currentBook = product
            favourite = _currentUser?.let { _currentBook!!.islove(it.id) } == true
            if (favourite) {
                favorBtn.text = "-"

            } else {
                favorBtn.text = "ღ"
            }
        }
        // Get a reference to the Firebase Storage instance
//        val storage = FirebaseStorage.getInstance()
        val imageName = bookData?.cover // Replace with your image name
//        val imageRef = storage.reference.child("book/$imageName")
//        imageRef.getBytes(Long.MAX_VALUE)
//            .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
//                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//
//                // Set the Bitmap to the ImageView
//                cover.setImageBitmap(bitmap)
//
//
//                Glide.with(this)
//                    .load(bitmap)
//                    .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
//                    .into(backCover)
//            }.addOnFailureListener {
//                // Handle any errors
//            }
        Glide.with(cover.context)
            .load(imageName)
            .apply(RequestOptions().override(500, 600))
            .into(cover)
        Glide.with(this)
            .load(imageName)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
            .into(backCover)
        view.text = bookData?.view.toString()
        chapter.text = bookData?.chapters?.size.toString()
        if (bookData!!.status){
            status.text = "Completed"
        }else{
            status.text = "Ongoing"
        }
        age.text = bookData.age.toString() + "+"
        descTextView.text = bookData.tiny_des
        ratingTextView.text = bookData.rating.toString()

        val adapter = CategoryAdapter(bookData.categories);
        categoryView!!.adapter = adapter;
        val layoutManager = FlexboxLayoutManager(this);
        layoutManager!!.flexWrap = FlexWrap.WRAP;
        layoutManager!!.flexDirection = FlexDirection.ROW;
        layoutManager!!.alignItems = AlignItems.FLEX_START;
        categoryView!!.layoutManager = layoutManager;


        chooseChapterBtn.setText("☰ " +  bookData.chapters.size.toString()  +" Chapters")

        //button back to previous activity
        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        favorBtn.setOnClickListener{
            favourite = !favourite;
            if (favourite) {
                _currentUser!!.love(_currentBook!!)
                _currentBook!!.love(_currentUser!!)
                favorBtn.text = "-"
            }else {
                _currentUser!!.unLove(_currentBook!!)
                _currentBook!!.notlove(_currentUser!!)
                favorBtn.text = "ღ"
            }
            productViewModel.saveCurrentIsLove(_currentBook!!)
            userViewModel.saveHeartList(_currentUser!!)
        }
        //read
        val readBtn = findViewById<Button>(R.id.readBtn)
        readBtn.setOnClickListener {
            bookData.view += 1
            productViewModel.updateView(bookData.id, bookData.view)
            readingCurrentBook(bookData)
        }

        //open dialog choose chapter to read
        val chooseBtn = findViewById<Button>(R.id.chooseChapterBtn)
        chooseBtn.setOnClickListener {
            val dl = Dialog(this)
            dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dl.setContentView(R.layout.list_chapter_dialog)
            dl.show()
            var bgcover = dl.findViewById<ImageView>(R.id.background)
            var minicover = dl.findViewById<ImageView>(R.id.book)
            var minititle = dl.findViewById<TextView>(R.id.book_title)
            minititle.text = bookData.title
//            imageRef.getBytes(Long.MAX_VALUE)
//                .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
//                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//
//                    // Set the Bitmap to the ImageView
//                    minicover.setImageBitmap(bitmap)
//
//
//                    Glide.with(this)
//                        .load(bitmap)
//                        .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
//                        .into(bgcover)
//                }.addOnFailureListener {
//                    // Handle any errors
//                }
            Glide.with(minicover.context)
                .load(imageName)
                .apply(RequestOptions().override(500, 600))
                .into(minicover)
            Glide.with(this)
                .load(imageName)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                .into(bgcover)



            var chapterListView = dl.findViewById<ListView>(R.id.chapterListView)
            val adapter = ChapterAdapter(this, bookData.chapters)
            chapterListView.adapter = adapter
            chapterListView.setOnItemClickListener { adapterView, view, i, l ->
                //TODO: Code doc sach khi click vo chapter o day ne
                if(!bookData.chapters[i]._lock){
                    val intent = Intent(this, ReadBookActivity::class.java)
                    intent.putExtra("book",bookData)
                    intent.putExtra("id_chapter",bookData.chapters[i].id_chapter)
                    ActivityCompat.startActivityForResult(this, intent, 302, null)
                }else{
                    val intent = Intent(this, PurchaseActivity::class.java)
                    intent.putExtra("bookdata", Klaxon().toJsonString(bookData))
                    intent.putExtra("chapter", Klaxon().toJsonString(bookData.chapters[i]))
                    ActivityCompat.startActivityForResult(this, intent, 700, null)
                }

            }
        }
        //open dialog feedback and rating
        val ratingBtn = findViewById<Button>(R.id.ratingBtn)
        ratingBtn.setOnClickListener {
            val dl = Dialog(this)
            dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dl.setContentView(R.layout.dialog_fb_rating)
            dl.show()

            val ratingBar = dl.findViewById<RatingBar>(R.id.ratingBar)
            val ratingTextView = dl.findViewById<TextView>(R.id.ratingTextView)
            val submitBtn = dl.findViewById<Button>(R.id.submitBtn)
            val comment = dl.findViewById<EditText>(R.id.editText)

            ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
                ratingTextView.text = String.format("(%s)", fl)
            }

            submitBtn.setOnClickListener {
                if (FirebaseAuthManager.auth.currentUser != null) {
                    val currentUser = FirebaseAuthManager.getUser()
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        val bid = bookData.id

                        // Check if the user has already left feedback for the book/comic
                        val feedbackRef = Firebase.database("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("feedback")
                        val query = feedbackRef.orderByChild("uid").equalTo(uid)
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var flag = 0
                                if (snapshot.exists()) {
                                    // If there is existing feedback, update it with the new rating and feedback
                                    for (feedbackSnapshot in snapshot.children) {
                                        val feedback = feedbackSnapshot.getValue(Feedback::class.java)
                                        if (feedback?.bid == bid) {
                                            feedback?.let {
                                                it.rating = ratingBar.rating.toDouble()
                                                it.feedback = comment.text.toString()
                                                feedbackSnapshot.ref.setValue(feedback)
                                                flag = 1
                                            }
                                        }
                                    }
                                }
                                if (flag == 0){
                                    // If there is no existing feedback, push a new feedback
                                    val newFeedback = Feedback(
                                        id = "", // leave the ID empty to generate a unique key in Firebase
                                        uid = uid,
                                        bid = bid,
                                        rating = ratingBar.rating.toDouble(),
                                        feedback = comment.text.toString() // replace with the user's feedback
                                    )
                                    val feedbackKey = feedbackRef.push().key
                                    feedbackKey?.let { key ->
                                        newFeedback.id =
                                            key // assign the generated key to the Feedback object
                                        feedbackRef.child(key).setValue(newFeedback)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle any errors here
                            }
                        })
                    }
                }
                dl.dismiss()
            }
        }

        val feedbackList = mutableListOf<Feedback>()

        val listFeedback = findViewById<RecyclerView>(R.id.listFeedback)
        val fbAdapter = FeedbackAdapter(this, feedbackList)
        listFeedback.adapter = fbAdapter
        listFeedback.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        val feedbackRef = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("feedback")
        val query = feedbackRef.orderByChild("bid").equalTo(bookData.id)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackList.clear()
                var totalRating = 0.0
                for (data in snapshot.children) {
                    val feedback = data.getValue(Feedback::class.java)
                    if (feedback != null) {
                        feedbackList.add(feedback)
                        totalRating += feedback.rating
                    }
                }
                totalRating /= feedbackList.size
                // TODO: Xu ly NaN lai
                if (!totalRating.isNaN())
                    totalRating = ((totalRating * 100.0).roundToInt() / 100.0)
                else
                    totalRating = 0.0
                ratingTextView.text = totalRating.toString()
                val bookRef = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("book").child(bookData.id)
                bookRef.child("rating").setValue(totalRating)
                fbAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val bookList: MutableList<Product> = mutableListOf()
        var customSlideView: RecyclerView? = null;
        customSlideView = findViewById(R.id.listView);
        val adapterSlide = ListAdapterSlideshow(this, bookList);
        customSlideView!!.adapter = adapterSlide;
        customSlideView!!.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        productViewModel.getAllBook { books ->
            run {
                for (book in books.children) {
                    val product = book.getValue(Product::class.java)
                    if (product != null) {
                        bookList.add(product)
                    }
                }
                adapterSlide.notifyDataSetChanged()
            }
        }
//            .observe(this, Observer { products ->
//                run {
//                    bookList.clear()
//                    println(products)
//                    bookList.addAll(products)

//                }
//            })
    }

    private fun readingCurrentBook(bookData: Product) {
        var reading = _currentUser!!.reading
        var id_chap = bookData.chapters[0].id_chapter
        for (i in reading)
            if (i.id_book.equals(_currentBook!!.id)) {
                for (j in _currentBook!!.chapters)
                    if (i.id_chapter.equals(j.id_chapter)) {
                        id_chap = j!!.id_chapter
                    }
            }

        val intent = Intent(this, ReadBookActivity::class.java)
        intent.putExtra("book",bookData)
        intent.putExtra("id_chapter", id_chap)
        ActivityCompat.startActivityForResult(this, intent, 302, null)
    }
}