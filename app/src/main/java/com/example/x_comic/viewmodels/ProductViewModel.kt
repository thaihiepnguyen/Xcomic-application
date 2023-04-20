package com.example.x_comic.viewmodels

import android.R.id
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.x_comic.models.Product
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class ProductViewModel : ViewModel() {
    val database = Firebase.database
    val db = database.getReference("book")
    private val _products = MutableLiveData<ArrayList<Product>>()
    private val _productsCompleted = MutableLiveData<ArrayList<Product>>()
    private val _productsLatest = MutableLiveData<ArrayList<Product>>()
    private val _productsPopular = MutableLiveData<ArrayList<Product>>()
    // TODO: biến này để truyền sang Activity khác
    val productsLiveData: LiveData<ArrayList<Product>>
        get() = _products
    val productsCompletedLiveData: LiveData<ArrayList<Product>>
        get() = _productsCompleted

    val productLatestLiveData: LiveData<ArrayList<Product>>
        get() = _productsLatest

    val productPopularLiveData: LiveData<ArrayList<Product>>
        get() = _productsPopular


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

        println("123"+_products)
        return _products
    }
    fun getCompletedBook():MutableLiveData<ArrayList<Product>>{
        if (_productsCompleted.value == null) {
            // tạo thread mới.
            db.limitToFirst(5).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products = ArrayList<Product>()

                    for (snapshot in dataSnapshot.children) {
                        val product = snapshot.getValue(Product::class.java)
                        if (product != null && product.status) {
                           products.add(product)

                        }
                    }
                    _productsCompleted.value = products
                    _productsCompleted.postValue(products)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                    db.removeEventListener(this)
                }
            })
        }
        return _productsCompleted
    }

    fun getPopularBook():MutableLiveData<ArrayList<Product>>{
        if (_productsPopular.value == null) {
            // tạo thread mới.
            db.orderByChild("favorite").limitToFirst(5).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products = ArrayList<Product>()

                    for (snapshot in dataSnapshot.children) {
                        val product = snapshot.getValue(Product::class.java)
                        if (product != null) {
                            products.add(product)
                        }
                    }
                    _productsPopular.value = products
                    _productsPopular.postValue(products)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                    db.removeEventListener(this)
                }
            })
        }
        return _productsPopular
    }

    fun getLatestBook():MutableLiveData<ArrayList<Product>>{
        if (_productsLatest.value == null) {
            // tạo thread mới.
            db.limitToLast(5).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products = ArrayList<Product>()

                    for (snapshot in dataSnapshot.children) {
                        val product = snapshot.getValue(Product::class.java)
                        if (product != null) {
                            products.add(product)
                        }
                    }
                    _productsLatest.value = products
                    _productsLatest.postValue(products)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                    db.removeEventListener(this)
                }
            })
        }
        return _productsLatest
    }


    fun addProduct(product: Product) {
        val newRef = db.push()
        val id = newRef.key

        if (id != null) {
            product.id = id
        }

        newRef.setValue(product)
    }

    fun addFavorite(product: Product) {
        val values: HashMap<String, Any> = HashMap()
        values["favorite"] = Random.nextLong(100,1000000);
        db.child(product.id).updateChildren(values)
    }
}

