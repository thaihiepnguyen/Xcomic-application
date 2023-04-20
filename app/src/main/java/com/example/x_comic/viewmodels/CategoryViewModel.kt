package com.example.x_comic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.x_comic.models.Category
import com.example.x_comic.models.Product
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CategoryViewModel : ViewModel() {
    private val database = Firebase.database
    val db = database.getReference("categories")
    private val _categories = MutableLiveData<ArrayList<Category>>()

    // TODO: biến này để bắn sang Activity khác
    val categoriesLiveData: LiveData<ArrayList<Category>>
        get() = _categories


    fun getAll():MutableLiveData<ArrayList<Category>> {
        // TODO: Ý là mình chỉ chạy dòng ở dưới 1 lần thôi
        // lần đầu tiên khi _categories.value còn là null
        if (_categories.value == null) {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val categories = ArrayList<Category>()

                    for (snapshot in dataSnapshot.children) {
                        val category = snapshot.getValue(Category::class.java)
                        if (category != null) {
                            categories.add(category)
                        }
                    }
                    _categories.value = categories
                    _categories.postValue(categories)

                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO: Xử lý lỗi, bỏ thread đi
                    db.removeEventListener(this)
                }
            })
        }
        return _categories
    }

    // TODO: code id tăng dần mệt quá bỏ qua nhe :)
    fun addCategory(name: String) {
        val category = Category()
        val newRef = db.push()
        val id = newRef.key

        if (id != null) {
            category.id = id
            category.name = name
        }

        newRef.setValue(category)
    }
}