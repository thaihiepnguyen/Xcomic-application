package com.example.x_comic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.x_comic.models.Product
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProductViewModel : ViewModel() {
    val database = Firebase.database
    val db = database.getReference("book")
    private val _products = MutableLiveData<ArrayList<Product>>()
    private val _productsCompleted = MutableLiveData<ArrayList<Product>>()

    // TODO: biến này để truyền sang Activity khác
    val productsLiveData: LiveData<ArrayList<Product>>
        get() = _products
    val productsCompletedLiveData: LiveData<ArrayList<Product>>
        get() = _productsCompleted


    fun getAllBook():MutableLiveData<ArrayList<Product>>{
        // TODO: Kiểm tra là chỉ khi _products.value == null. Ý là mình chỉ chạy dòng ở dưới 1 lần thôi
        // lần đầu tiên khi _products.value còn là null
        if (_products.value == null) {
            // tạo thread mới.
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products = ArrayList<Product>()

                    for (snapshot in dataSnapshot.children) {
                        val product = snapshot.getValue(Product::class.java)
                        if (product != null) {
                            products.add(product)
                        }
                    }
                    _products.value = products
                    _products.postValue(products)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                    db.removeEventListener(this)
                }
            })
        }
        return _products
    }
    fun getCompletedBook():MutableLiveData<ArrayList<Product>>{
        if (_productsCompleted.value == null) {
            // tạo thread mới.
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products2 = ArrayList<Product>()

                    for (snapshot in dataSnapshot.children) {
                        val product2 = snapshot.getValue(Product::class.java)
                        if (product2 != null && product2.status) {
                           products2.add(product2)
                        }
                    }
                    _productsCompleted.value = products2
                    _productsCompleted.postValue(products2)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                    db.removeEventListener(this)
                }
            })
        }
        return _productsCompleted
    }
    fun addProduct(product: Product) {
        val newRef = db.push()
        val id = newRef.key

        if (id != null) {
            product.id = id
        }

        newRef.setValue(product)
    }
}

