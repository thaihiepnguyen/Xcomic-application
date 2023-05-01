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
        ratingBar.rating = feedback.rating

        val databaseRef = FirebaseDatabase.getInstance().reference
        val userRef = databaseRef.child("users").child(feedback.uid)

        val userListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get User object and use the values to update the UI
                val user = dataSnapshot.getValue(User::class.java)
                if (user!=null){
                    if (user.avatar != ""){
                        val avatarUrl: String = user!!.avatar
                        // Load the avatar into your ImageView using your preferred image loading library
                        // TODO: Code ở dưới có bug
                        //    java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
                        //        at com.bumptech.glide.manager.RequestManagerRetriever.assertNotDestroyed(RequestManagerRetriever.java:348)
                        //        at com.bumptech.glide.manager.RequestManagerRetriever.get(RequestManagerRetriever.java:148)
                        //        at com.bumptech.glide.manager.RequestManagerRetriever.get(RequestManagerRetriever.java:181)
                        //        at com.bumptech.glide.Glide.with(Glide.java:813)
                        //        at com.example.x_comic.adapters.FeedbackAdapter$onBindViewHolder$userListener$1.onDataChange(FeedbackAdapter.kt:68)
                        //        at com.google.firebase.database.core.ValueEventRegistration.fireEvent(ValueEventRegistration.java:75)
                        //        at com.google.firebase.database.core.view.DataEvent.fire(DataEvent.java:63)
                        //        at com.google.firebase.database.core.view.EventRaiser$1.run(EventRaiser.java:55)
                        //        at android.os.Handler.handleCallback(Handler.java:751)
                        //        at android.os.Handler.dispatchMessage(Handler.java:95)
                        //        at android.os.Looper.loop(Looper.java:154)
                        //        at android.app.ActivityThread.main(ActivityThread.java:6119)
                        //        at java.lang.reflect.Method.invoke(Native Method)
                        //        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:886)
                        //        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:776)
                        Glide.with(context).load(avatarUrl).into(avatarImageView)
                        feedback_author.text = user.full_name
                    }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        userRef.addValueEventListener(userListener)


    }

}