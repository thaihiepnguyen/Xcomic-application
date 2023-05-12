package com.example.x_comic.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Order
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.models.Notification
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationAdapter(
    private val context: Activity,
    private val noti: List<Notification>
) : ArrayAdapter<Notification>(context, R.layout.notification_layout_item, noti) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.notification_layout_item, null, true)
        val notification_title = rowView.findViewById(R.id.notification_title) as TextView
        notification_title.text = noti[position].title
        val notification_message = rowView.findViewById(R.id.notification_message) as TextView
        notification_message.text = noti[position].message
        val total = rowView.findViewById(R.id.total) as TextView
        total.text = noti[position].total.toString()

        return rowView
    }
}