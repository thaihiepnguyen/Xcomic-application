package com.example.x_comic.views.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.NotificationAdapter
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Feedback
import com.example.x_comic.models.Order
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Notification : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        val listNoti = ArrayList<com.example.x_comic.models.Notification>()
        var notiAdapter = NotificationAdapter(requireActivity(),listNoti)
        val list_noti = view.findViewById<ListView>(R.id.list_noti)
        list_noti.adapter = notiAdapter

        val uid = FirebaseAuthManager.getUser()?.uid
        val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ordersRef = database.getReference("orders")
        val uidQuery = ordersRef.orderByChild("uid").equalTo(uid)
        uidQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    listNoti.clear()
                    for (orderSnapshot in dataSnapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        val title = "You purchased a chapter"
                        var message = ""
                        val query = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("book").child(order!!.bid).child("chapter").orderByChild("id_chapter")
                            .equalTo(order.cid).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        var chapter = snapshot.getValue(Chapter::class.java)
                                        message = chapter!!.name + "at" + order.time
                                        val total = "$" + order.cost
                                        var noti = com.example.x_comic.models.Notification(title,message, total)
                                        listNoti.add(noti)
                                    }

                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Log.i("FEEDBACK-ERROR", error.toString())
                                }
                            })
                    }

                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // handle the error case here
            }
        })
        return view
    }


}