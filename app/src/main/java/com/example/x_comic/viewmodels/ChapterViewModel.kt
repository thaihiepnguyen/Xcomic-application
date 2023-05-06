package com.example.x_comic.viewmodels

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Product
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ChapterViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app")
    val db = database.getReference("chapter")
    private val _chapters = MutableLiveData<ArrayList<Chapter>>()

    // TODO: biến này để truyền sang Activity khác
    val chaptersLiveData: LiveData<ArrayList<Chapter>>
        get() = _chapters

    fun getAllChapterOfBook(id_book : String):MutableLiveData<ArrayList<Chapter>>{

        if (_chapters.value == null) {
            // tạo thread mới.
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val chapters = ArrayList<Chapter>()

                    for (snapshot in dataSnapshot.children) {
                        val chapter = snapshot.getValue(Chapter::class.java)

                        if (chapter != null) {
                            chapters.add(chapter)
                        }
                    }
                    _chapters.value = chapters
                    _chapters.postValue(chapters)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                    db.removeEventListener(this)
                }
            })
        }
        return _chapters
    }

    fun addChapter(chapter: Chapter) {
        val newRef = db.push()
        val id = newRef.key

        if (id != null) {
            chapter.id_chapter = id
        }

        newRef.setValue(chapter)
    }
}

