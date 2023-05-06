package com.example.x_comic.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.x_comic.R
import com.example.x_comic.models.Feedback
import com.example.x_comic.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ms.square.android.expandabletextview.ExpandableTextView


class FeedbackAdapter (
    private  var context: Activity,
    private var feedbackList: MutableList<Feedback>,
) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>()
{
    var onItemClick: ((Feedback) -> Unit)? = null
    //var context: Context? = null;
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var fbTextView = listItemView.findViewById(R.id.expand_text_view) as ExpandableTextView;
        var ratingBar = listItemView.findViewById(R.id.ratingBar) as RatingBar;
        var feedback_author = listItemView.findViewById(R.id.feedback_author) as TextView;
        var avatarImageView = listItemView.findViewById(R.id.avatarImageView) as ImageView;
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        //context = parent.context;
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.feedback_item_layout, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return feedbackList.size;
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedback = feedbackList.get(position);
        val fbTextView = holder.fbTextView;
        val feedback_author = holder.feedback_author
        val ratingBar = holder.ratingBar;
        val avatarImageView = holder.avatarImageView;
        fbTextView.text = feedback.feedback;
        ratingBar.rating = feedback.rating.toFloat()

        val databaseRef = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        val userRef = databaseRef.child("users").child(feedback.uid)

        val userListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get User object and use the values to update the UI
                val user = dataSnapshot.getValue(User::class.java)
                if (user!=null){
                    if (user.avatar != ""){
                        val avatarUrl: String = user!!.avatar
                        // Load the avatar into your ImageView using your preferred image loading library
                        Glide.with(context).load(avatarUrl).into(avatarImageView)
                        if(user.full_name!="no data yet") feedback_author.text = user.full_name
                    }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        userRef.addValueEventListener(userListener)


    }

}