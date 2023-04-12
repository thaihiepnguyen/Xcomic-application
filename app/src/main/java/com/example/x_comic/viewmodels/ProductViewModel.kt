package com.example.x_comic.viewmodels

import android.util.Log
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProductViewModel {
    fun getAll() : ArrayList<Product> {
        var products = ArrayList<Product>()

//        val database = Firebase.database
//        val ref = database.reference.child("categories").push()
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (snapshot in dataSnapshot.children) {
//                    val product = dataSnapshot.getValue(Product::class.java)
//                    if (product != null) {
//                        Log.d("PRODUCT", product.toString())
//                        products.add(product)
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                // Xử lý lỗi
//            }
//        })


        return products
    }
}