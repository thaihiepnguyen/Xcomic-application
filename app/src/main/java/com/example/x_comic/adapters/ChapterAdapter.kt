package com.example.x_comic.adapters

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Order
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.views.read.ReadBookActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChapterAdapter(
    private val context: Activity,
    private val chapters: List<Chapter>
) : ArrayAdapter<Chapter>(context, R.layout.list_chapter_item_layout, chapters) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.list_chapter_item_layout, null, true)
        val chapterTV = rowView.findViewById(R.id.tvChapterName) as TextView
        chapterTV.text = chapters[position].name
        val dateTV = rowView.findViewById(R.id.tvDate) as TextView
        dateTV.text = chapters[position].date_update
        val lock_icon = rowView.findViewById(R.id.lock_icon) as ImageView
        val unlock_icon = rowView.findViewById(R.id.unlock_icon) as ImageView
        unlock_icon.setVisibility(View.GONE);


        val uid = FirebaseAuthManager.getUser()?.uid
        val cid = chapters[position].id_chapter
        val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val ordersRef = database.getReference("orders")
        val uidQuery = ordersRef.orderByChild("uid").equalTo(uid)
        uidQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // check if the uid query returned any results
                if (dataSnapshot.exists()) {
                    // an Order object with matching uid value already exists in the database
                    // loop through the results to check for matching cid values
                    for (orderSnapshot in dataSnapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        if (order?.cid == cid) {
                            chapters[position]._lock = false
                            lock_icon.setVisibility(View.GONE);
                            unlock_icon.setVisibility(View.VISIBLE);
                            return
                        }
                    }
                    // no Order object with matching cid value for the matching uid exists in the database
                    // handle the case here
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // handle the error case here
            }
        })
        if (!chapters[position]._lock){
            lock_icon.setVisibility(View.GONE);
        }
        return rowView
    }
}
