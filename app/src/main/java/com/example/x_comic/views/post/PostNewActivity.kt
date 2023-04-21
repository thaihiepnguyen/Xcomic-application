package com.example.x_comic.views.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.CategoryAdapter
import com.example.x_comic.models.Category
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.*
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import java.io.IOException

class PostNewActivity : AppCompatActivity() {

    var categoryView : RecyclerView? = null;
    var REQUEST_CODE_PICK_IMAGE = 1111
    var REQUEST_CODE_PICK_CHAPTER = 2222
    var fileNameCover : String = ""
    var age : Int = 0
    var _user : User = User()
    var curBook : Product = Product()
    lateinit var chapterListAdapter : ChaptersAdapter
    var is_new = true

    var categoryList : ArrayList<Category> = ArrayList()

    var chapterList: MutableList<Chapter> = mutableListOf(
        Chapter("Chapter 1"),
        Chapter("Chapter 2"),
        Chapter("Chapter 3"),
        Chapter("Chapter 4"),
        Chapter("Chapter 5"),
        Chapter("Chapter 6"),
    )
    var customChapterListView: RecyclerView? = null;

    val ageRanges = listOf(
        "0-2 years" to "Board books and picture books" to 2,
        "3-5 years" to "Picture books and early readers" to 5,
        "6-8 years" to "Chapter books and easy readers" to 8,
        "9-12 years" to "Middle-grade novels" to 12,
        "13+ years" to "Young adult and adult novels" to 13,
        "18+ years" to "Adult novels" to 18
    )

    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var chapterViewModel: ChapterViewModel



    class ChaptersAdapter (private val chapters: MutableList<Chapter>) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>() {
        var onItemClick: ((chapter: Chapter, position: Int) -> Unit)? = null
        var onButtonClick: ((Chapter) -> Unit)? = null
        inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
            val name = listItemView.findViewById(R.id.tvChapterName) as TextView
            val dateUpdate = listItemView.findViewById(R.id.tvDate) as TextView

            init {
                listItemView.setOnClickListener { onItemClick?.invoke(chapters[adapterPosition], adapterPosition) }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            // Inflate the custom layout
            val chapterView = inflater.inflate(R.layout.chapter_status, parent, false)
            // Return a new holder instance
            return ViewHolder(chapterView)
        }

        override fun getItemCount(): Int {
            return chapters.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Get the data model based on position
            val c: Chapter = chapters.get(position)
            // Set item views based on your views and data model

            holder.name.text = chapters[position].name

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        chapterViewModel = ViewModelProvider(this).get(ChapterViewModel::class.java)

        setContentView(R.layout.activity_post_new)

        // Category
        val layoutManager = FlexboxLayoutManager(this);
        categoryView = findViewById(R.id.category_list);
        categoryViewModel.getAll()
            .observe(this, Observer { categories ->
                run {
                    val adapter = CategoryAdapter(categories);
                    categoryView!!.adapter = adapter

                    layoutManager!!.flexWrap = FlexWrap.WRAP;
                    layoutManager!!.flexDirection = FlexDirection.ROW;
                    layoutManager!!.alignItems = AlignItems.FLEX_START;
                    categoryView!!.layoutManager = layoutManager;

                    categoryList = adapter!!.getAllItem()
                }
            })

        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {
            userViewModel.callApi(uid)
                .observe(this, Observer { user ->
                    run {
                        _user = user
                    }
                })
        }

        // Spinner Age
        val ageSpinner = findViewById<Spinner>(R.id.ageSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ageRanges.map { it.first.first })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ageSpinner.adapter = adapter
        ageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedAgeRange = ageRanges[position]
                val recommendedReadingLevel = selectedAgeRange.second
                age = selectedAgeRange.second
                // TODO: Chọn tuổi
                Toast.makeText(applicationContext, "Recommended reading level: $recommendedReadingLevel", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val addCoverBtn = findViewById<Button>(R.id.btnNewCover)
        addCoverBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        val nextButton = findViewById<Button>(R.id.btnNext)
        nextButton.setOnClickListener {
            //TODO: Save book
//            postBook()
            finish()
        }

        val newChapterButton = findViewById<Button>(R.id.btnNewChapter)
        newChapterButton.setOnClickListener {
            // Save book
            if (is_new) {
                postBook()
                is_new = false
            }
            curBook?.let {
                val intent = Intent(this, NewChapterActivity::class.java)
                intent.putExtra(Chapter.MESSAGE1, curBook.id)
                startActivityForResult(intent, REQUEST_CODE_PICK_CHAPTER)
            }
        }

        // Chapter
        getChapter()
    }

    private fun getChapter() {
        chapterViewModel.getAllChapterOfBook(curBook.id)
            .observe(this, Observer { chapters ->
                run {
                    customChapterListView = findViewById(R.id.listViewChapter) as RecyclerView;
                    chapterListAdapter = ChaptersAdapter(chapters);
                    customChapterListView!!.adapter = chapterListAdapter;
                    customChapterListView!!.layoutManager = LinearLayoutManager(this);
                    val itemDecoration: RecyclerView.ItemDecoration =
                        DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                    customChapterListView?.addItemDecoration(itemDecoration)

                    chapterListAdapter!!.onItemClick = {chapter, position ->
                        // DO SOMETHING

                    }
                }
            })
    }

    private fun postBook () {
        val book = Product()
        book.author = _user.penname
        book.title = findViewById<EditText>(R.id.etTitle).text.toString()
        book.tiny_des = findViewById<EditText>(R.id.etDescription).text.toString()
        book.status = findViewById<Switch>(R.id.sStatus).isChecked
        book.hide = findViewById<Switch>(R.id.sIsHide).isChecked
        book.age = age
        book.cover = fileNameCover
        book.categories = categoryList

        curBook= book
        productViewModel.addProduct(book)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            // Lay ten file cua anh
            val filename: String = imageUri?.let { uri ->
                val cursor = contentResolver.query(uri, null, null, null, null)
                val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor?.moveToFirst()
                nameIndex?.let { cursor?.getString(it) }
            } ?: "unknown_filename"
            fileNameCover = filename
            // Lưu ảnh vào profile
            saveImageToProfile(imageUri, filename)
        }

        if (requestCode == REQUEST_CODE_PICK_CHAPTER && resultCode == RESULT_OK && data != null) {
            val reply = data!!.getSerializableExtra(Chapter.MESSAGE2) as Chapter
            chapterViewModel.addChapter(reply)

            getChapter()
            chapterListAdapter?.notifyDataSetChanged()

        }
    }

    private fun saveImageToProfile(imageUri: Uri?, filename : String) {
        try {
            val uid = FirebaseAuthManager.auth.uid
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            if (uid != null) {
                productViewModel.uploadCover(filename, bitmap, findViewById(R.id.ivCover))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}