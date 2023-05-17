package com.example.x_comic.views.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.adapters.*
import com.example.x_comic.models.Feedback
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.main.fragments.*
import com.example.x_comic.views.more.AllActivity
import com.example.x_comic.views.profile.AuthorProfileActivity
import com.example.x_comic.views.purchase.PurchaseActivity
import com.example.x_comic.views.read.ReadBookActivity
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
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlin.math.roundToInt


class DetailActivity : AppCompatActivity() {
    private var _currentUser: User? = null
    //Sync with firebase
    private var _currentBook: Product? = null
    private var _currentAuthor: User? = null
    private var favourite = false
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //Declare view model to get data
        val productViewModel: ProductViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        val userViewModel: UserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        //get data of book to display detail
        val stringData = intent.getStringExtra("book_data")
        var bookData:Product? = Product()
        val databaseReference = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("book").child(stringData!!)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookData = dataSnapshot.getValue(Product::class.java)
                if (bookData != null){
                    //declare member of ui
                    val title = findViewById<TextView>(R.id.book_title)
                    val author = findViewById<TextView>(R.id.book_author)
                    val cover = findViewById<ImageView>(R.id.book_cover)
                    val view = findViewById<TextView>(R.id.viewTextView)
                    val favorite = findViewById<TextView>(R.id.favoriteTextView)
                    val chapter = findViewById<TextView>(R.id.numOfChapterTextView)
                    val authorAvt = findViewById<ImageView>(R.id.avatar_picture)
                    val authorName = findViewById<TextView>(R.id.book_author)
                    val categoryView = findViewById<RecyclerView>(R.id.category_list)
                    val status = findViewById<TextView>(R.id.statusTV)
                    val age = findViewById<TextView>(R.id.ageTV)
                    val descTextView = findViewById<TextView>(R.id.descTextView)
                    val ratingTextView = findViewById<TextView>(R.id.ratingTV)
                    val chooseChapterBtn = findViewById<Button>(R.id.chooseChapterBtn)
                    val favorBtn = findViewById<Button>(R.id.favorBtn)
                    val backCover = findViewById<ImageView>(R.id.background)
                    val viewmore = findViewById<TextView>(R.id.view_more)
                    //assign data to view
                    title.text = bookData?.title
                    var name_book = findViewById<TextView>(R.id.name_book);
                    name_book.text = bookData?.title

                    viewmore.setOnClickListener {
                        nextAllActivity()
                    }
                    userViewModel.getUserById(bookData!!.author) {
                            user -> run {
                        _currentAuthor = user
                        author.text = user.penname
                        if (user.avatar != "") {
                            Glide.with(authorAvt.context)
                                .load(user.avatar)
                                .apply(RequestOptions().override(100, 100))
                                .circleCrop()
                                .into(authorAvt)
                        } else {
                            Glide.with(authorAvt.context)
                                .load(R.drawable.avatar)
                                .apply(RequestOptions().override(100, 100))
                                .circleCrop()
                                .into(authorAvt)
                        }

                    }
                    }
                    authorAvt.setOnClickListener {
                        _currentAuthor?.let { it1 -> nextAuthorProfileActivity(it1) }
                    }
                    authorName.setOnClickListener {
                        _currentAuthor?.let { it1 -> nextAuthorProfileActivity(it1) }
                    }

                    favorite.text = bookData!!.have_loved.size.toString()

                    //check book is in favorite list
                    val uid = FirebaseAuthManager.auth.uid
                    userViewModel.callApi(uid!!).observe(this@DetailActivity) { user ->
                        _currentUser = user
                    }
                    productViewModel.getBookById(bookData!!.id) { product ->
                        // TODO: realtime
                        _currentBook = product
                        favourite = _currentUser?.let { _currentBook!!.islove(it.id) } == true
                        if (favourite) {
                            favorBtn.text = "✗"

                        } else {
                            favorBtn.text = "ღ"
                        }
                    }

                    val imageName = bookData!!.cover
                    Glide.with(cover.context)
                        .load(imageName)
                        .apply(RequestOptions().override(500, 600))
                        .into(cover)
                    Glide.with(this@DetailActivity)
                        .load(imageName)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(50, 3)))
                        .into(backCover)

                    view.text = bookData!!.view.toString()

                    chapter.text = bookData!!.chapters.size.toString()

                    if (bookData!!.status){
                        status.text = "Completed"
                        status.backgroundTintList = ColorStateList.valueOf(
                                R.color.done
                            )

                    }else{
                        status.text = "Ongoing"
                        status.backgroundTintList = ColorStateList.valueOf(
                                R.color.golden
                            )

                    }

                    age.text = bookData!!.age.toString() + "+"

                    descTextView.text = bookData!!.tiny_des

                    ratingTextView.text = bookData!!.rating.toString()
                    //display categories of the book
                    val adapter = CategoryListAdapter(bookData!!.categories)
                    categoryView.adapter = adapter
                    val layoutManager = FlexboxLayoutManager(this@DetailActivity)
                    layoutManager.flexWrap = FlexWrap.WRAP
                    layoutManager.flexDirection = FlexDirection.ROW
                    layoutManager.alignItems = AlignItems.FLEX_START
                    categoryView.layoutManager = layoutManager

                    chooseChapterBtn.text = "☰ " +  bookData!!.chapters.size.toString()  +" Chapters"

                    //button back to previous activity
                    val backBtn = findViewById<ImageButton>(R.id.backBtn)
                    backBtn.setOnClickListener {
                        finish()
                    }



                    //process favorite feature
                    favorBtn.setOnClickListener{
                        favourite = !favourite
                        if (favourite) {
                            _currentUser!!.love(_currentBook!!)
                            _currentBook!!.love(_currentUser!!)
                            favorBtn.text = "✗"
                        }else {
                            _currentUser!!.unLove(_currentBook!!)
                            _currentBook!!.notlove(_currentUser!!)
                            favorBtn.text = "ღ"
                        }
                        productViewModel.saveCurrentIsLove(_currentBook!!)
                        userViewModel.saveHeartList(_currentUser!!)
                    }
                    //process read button
                    if (bookData!!.chapters.size!=0){
                        val readBtn = findViewById<Button>(R.id.readBtn)
                        readBtn.setOnClickListener {
                            bookData!!.view += 1
                            productViewModel.updateView(bookData!!.id, bookData!!.view)
                            readingCurrentBook(bookData!!)
                        }
                    }

                    //open dialog choose chapter to read
                    val chooseBtn = findViewById<Button>(R.id.chooseChapterBtn)
                    chooseBtn.setOnClickListener {
                        val dl = Dialog(this@DetailActivity)
                        dl.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dl.setContentView(R.layout.list_chapter_dialog)
                        dl.show()

                        val miniCover = dl.findViewById<ImageView>(R.id.book)
                        val miniTitle = dl.findViewById<TextView>(R.id.book_title)
                        miniTitle.text = bookData!!.title
                        Glide.with(miniCover.context)
                            .load(imageName)
                            .apply(RequestOptions().override(500, 600))
                            .into(miniCover)
                       
                        val chapterListView = dl.findViewById<ListView>(R.id.chapterListView)
                        val adapter = ChapterAdapter(this@DetailActivity, bookData!!.chapters)
                        chapterListView.adapter = adapter
                        chapterListView.setOnItemClickListener { adapterView, view, i, l ->
                            //Click and read book
                            if(!bookData!!.chapters[i]._lock){
                                val intent = Intent(this@DetailActivity, ReadBookActivity::class.java)
                                intent.putExtra("book",bookData!!.id)
                                intent.putExtra("id_chapter",bookData!!.chapters[i].id_chapter)
                                ActivityCompat.startActivityForResult(this@DetailActivity, intent, 302, null)

                            }else{
                                val intent = Intent(this@DetailActivity, PurchaseActivity::class.java)
                                intent.putExtra("book_data", bookData!!.id)
                                intent.putExtra("chapter", Klaxon().toJsonString(bookData!!.chapters[i]))
                                ActivityCompat.startActivityForResult(this@DetailActivity, intent, 700, null)
                            }

                        }
                    }
                    //open dialog feedback and rating
                    val ratingBtn = findViewById<Button>(R.id.ratingBtn)
                    ratingBtn.setOnClickListener {
                        val dl = Dialog(this@DetailActivity)
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
                                    val bid = bookData!!.id
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
                                                        feedback.let {
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
                                            Log.i("FEEDBACK-ERROR", error.toString())
                                        }
                                    })
                                }
                            }
                            dl.dismiss()
                        }
                    }
                    //display feedback and rating of the book
                    val feedbackList = mutableListOf<Feedback>()
                    val listFeedback = findViewById<RecyclerView>(R.id.listFeedback)
                    val fbAdapter = FeedbackAdapter(this@DetailActivity, feedbackList)
                    listFeedback.adapter = fbAdapter
                    listFeedback.layoutManager =
                        LinearLayoutManager(this@DetailActivity, RecyclerView.VERTICAL, false)
                    val feedbackRef = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("feedback")
                    val query = feedbackRef.orderByChild("bid").equalTo(bookData!!.id)
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
                            //solve NaN error
                            totalRating = if (!totalRating.isNaN())
                                ((totalRating * 100.0).roundToInt() / 100.0)
                            else
                                0.0
                            ratingTextView.text = totalRating.toString()
                            val bookRef = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("book").child(bookData!!.id)
                            bookRef.child("rating").setValue(totalRating)
                            fbAdapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                    val bookList: MutableList<Product> = mutableListOf()
                    val customSlideView: RecyclerView? = findViewById(R.id.listView)
                    val adapterSlide = ListAdapterSlideshow(this@DetailActivity, bookList)
                    customSlideView!!.adapter = adapterSlide
                    customSlideView.layoutManager = LinearLayoutManager(this@DetailActivity, RecyclerView.HORIZONTAL, false)
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
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if any
            }
        })


    }

    private fun readingCurrentBook(bookData: Product) {
        var flag = 0;
        for (c in 0 until bookData.chapters.size){
            if (!bookData.chapters[c]._lock){
                val reading = _currentUser!!.reading
                var id_chap = bookData.chapters[c].id_chapter
                for (i in reading)
                    if (i.id_book.equals(_currentBook!!.id)) {
                        for (j in _currentBook!!.chapters)
                            if (i.id_chapter.equals(j.id_chapter)) {
                                id_chap = j.id_chapter
                            }
                    }

                val intent = Intent(this, ReadBookActivity::class.java)
                intent.putExtra("book",bookData.id)
                intent.putExtra("id_chapter", id_chap)
                ActivityCompat.startActivityForResult(this, intent, 302, null)
                flag = 1
                break
            }
        }
        if (flag != 1){
            Toast.makeText(this, "All chapters locked\nPurchase required to read", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "If you have made a purchase, select chapter from the list", Toast.LENGTH_LONG).show()
        }
    }
    private fun nextAuthorProfileActivity(author: User) {
        val intent = Intent(this, AuthorProfileActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("authorKey", author)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    private fun nextAllActivity() {
        val intent = Intent(this, AllActivity::class.java)
        startActivity(intent)
    }
}