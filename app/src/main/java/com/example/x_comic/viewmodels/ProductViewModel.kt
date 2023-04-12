package com.example.x_comic.viewmodels

import android.util.Log
import android.widget.Toast
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

        val database = Firebase.database
        val ref = database.reference.child("book").child("1")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val product = dataSnapshot.getValue(Product::class.java)!!

                Log.d("TAG", product.chapters.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
            }
        })

        return products
    }
}