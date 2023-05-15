package com.example.x_comic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.User

class AuthorListAdapter (
    private var avatarList: MutableList<User>
) : RecyclerView.Adapter<AuthorListAdapter.ViewHolder>()
{
    var onItemClick: ((User) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var avatar = listItemView.findViewById(R.id.avatar_picture) as ImageView;
        var username = listItemView.findViewById(R.id.username) as TextView;
        var email = listItemView.findViewById(R.id.email) as TextView;
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(avatarList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context;
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.avatar2, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return avatarList.size;
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val author = avatarList.get(position);
        val avatar = holder.avatar;
        val username = holder.username;
        val email= holder.email;
        username.setText(author.full_name);
        email.text = author.email
        Glide.with(avatar.context)
            .load(author.avatar)
            .apply(RequestOptions().override(250, 250))
            .circleCrop()
            .into(avatar)
    }

}
