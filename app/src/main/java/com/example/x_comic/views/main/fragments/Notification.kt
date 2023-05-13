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
import com.example.x_comic.models.*
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.OrderViewModel
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
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
//        val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/")
//        val ordersRef = database.getReference("orders")
//        val uidQuery = ordersRef.orderByChild("uid").equalTo(uid)
        var userViewModel = UserViewModel()
        var productViewModel = ProductViewModel()
        var orderViewModel = OrderViewModel()

        orderViewModel.getOrdersByUid(uid!!) {dataSnapshot ->
            run {
                if (dataSnapshot.exists()) {
                    listNoti.clear()
                    for (orderSnapshot in dataSnapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        val title = "You purchased a chapter"
                        var message = ""
                        productViewModel.getAllChapter(order!!.bid) {snapshots->
                            run {
                                for (snapshot in snapshots.children) {
                                    if (snapshot.exists()) {
                                        var chapter = snapshot.getValue(Chapter::class.java)
                                        if (chapter!!.id_chapter.equals(order.cid)) {
                                            productViewModel.getBookById(chapter!!.id_book) {book ->
                                                run {
                                                    message = book.title + "\n"+ chapter!!.name + "\nat " + order.time
                                                    val total = "$" + order.cost
                                                    var noti = Notification(title,message, total)
                                                    Log.d("TEST", noti.message)
                                                    listNoti.add(noti)
                                                }
                                                notiAdapter.notifyDataSetChanged()
                                            }
                                        }
                                    }
                                    notiAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                        notiAdapter.notifyDataSetChanged()
                    }

                } else {

                }
            }
        }
        return view
    }


}