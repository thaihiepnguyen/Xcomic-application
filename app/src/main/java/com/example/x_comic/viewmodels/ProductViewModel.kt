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
import com.example.x_comic.adapters.ListAdapterSlideshow
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

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

    inline fun getAllBook(crossinline callback: (DataSnapshot)->Unit){
        // tạo thread mới.
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
                db.removeEventListener(this)
            }
        })
    }

    fun getAuthBook(_user : User):MutableLiveData<ArrayList<Product>>{
        // TODO: Kiểm tra là chỉ khi _products.value == null. Ý là mình chỉ chạy dòng ở dưới 1 lần thôi
        // lần đầu tiên khi _products.value còn là null
        if (_products.value == null) {
            // tạo thread mới.
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products = ArrayList<Product>()

                    for (snapshot in dataSnapshot.children) {
                        val product = snapshot.getValue(Product::class.java)
                        if (product != null && _user.penname == product.author) {
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

    // code mới
    inline fun getCompletedBook(crossinline callback: (DataSnapshot)->Unit){
        // tạo thread mới.
        db.limitToFirst(5).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
                db.removeEventListener(this)
            }
        })
    }

    inline fun getPopularBook(crossinline callback: (DataSnapshot)->Unit){
        // tạo thread mới.
        db.orderByChild("favorite").limitToFirst(5).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
                db.removeEventListener(this)
            }
        })
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

    inline fun getLatestBook(crossinline callback: (DataSnapshot)->Unit){
        // tạo thread mới.
        db.limitToLast(5).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                callback(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
                db.removeEventListener(this)
            }
        })
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

    fun updateProduct(product: Product) {
        val database = Firebase.database
        database.reference
            .child("book")
            .child(product.id)
            .setValue(product)
    }

    fun addFavorite(product: Product) {
        val values: HashMap<String, Any> = HashMap()
        values["favorite"] = Random.nextLong(100,1000000);
        db.child(product.id).updateChildren(values)
    }
    fun saveCurrentIsLove(book: Product) {
        db.child(book.id)
            .child("have_loved")
            .setValue(book.have_loved)
    }

    inline fun getAllBookIsLoved(uid: String, crossinline callback: (DataSnapshot)->Unit) {
        Firebase.database.getReference("users").child(uid).child("heart_list").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(task.result)
            } else {

            }
        }
    }

    inline fun getBookById(bookid: String, crossinline callback: (Product)->Unit) {
        val ref = db.child(bookid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var book: Product = dataSnapshot.getValue(Product::class.java)!!
                callback(book)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // TODO: Xử lý lỗi, bỏ thread đi
                db.removeEventListener(this)
            }
        })
    }
}

