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
import com.example.x_comic.models.Product
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ProductViewModel : ViewModel() {
    val database = Firebase.database
    val db = database.getReference("book")
    private val _products = MutableLiveData<ArrayList<Product>>()
    private val _productsCompleted = MutableLiveData<ArrayList<Product>>()
    private val _productsLatest = MutableLiveData<ArrayList<Product>>()
    // TODO: biến này để truyền sang Activity khác
    val productsLiveData: LiveData<ArrayList<Product>>
        get() = _products
    val productsCompletedLiveData: LiveData<ArrayList<Product>>
        get() = _productsCompleted

    val productLatestLiveData: LiveData<ArrayList<Product>>
        get() = _productsLatest


    fun getAllBook():MutableLiveData<ArrayList<Product>>{
        // TODO: Kiểm tra là chỉ khi _products.value == null. Ý là mình chỉ chạy dòng ở dưới 1 lần thôi
        // lần đầu tiên khi _products.value còn là null
        if (_products.value == null) {
            // tạo thread mới.
            db.limitToFirst(5).addValueEventListener(object : ValueEventListener {
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
            db.limitToFirst(5).addValueEventListener(object : ValueEventListener {
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

    fun uploadCover(filename: String, bitmap: Bitmap, imgCov: ImageView){
        val storage = FirebaseStorage.getInstance()
        val fileName = "${filename}"
        val storageRef = storage.reference.child("book_cover/$fileName")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                Glide.with(imgCov.context)
                    .load(downloadUrl)
                    .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(150)))
                    .into(imgCov)
            }


        }.addOnFailureListener { exception ->
            // Tải lên ảnh thất bại
            exception.printStackTrace()
        }
    }

    fun getCover(bookID: String, imgCov: ImageView) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("book_cover/$bookID.png")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val downloadUrl = uri.toString()

            changeCov(downloadUrl, bookID)
            Glide.with(imgCov.context)
                .load(downloadUrl)
                .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(150)))
                .into(imgCov)
        }.addOnFailureListener {

        }
    }

    fun changeCov(cover: String, bookID: String) {
        val database = Firebase.database
        if (bookID != null) {
            database.reference
                .child("book")
                .child(bookID)
                .child("cover")
                .setValue(cover)
        }
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
}

