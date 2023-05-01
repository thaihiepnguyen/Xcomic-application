package com.example.x_comic.adapters

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.Avatar
import com.example.x_comic.models.User
import com.example.x_comic.views.detail.DetailActivity
import com.example.x_comic.views.profile.AuthorProfileActivity
import com.beust.klaxon.Klaxon

class AvatarListAdapter (
    private var avatarList: MutableList<User>,
    private var iAvatarListAdapter: IAvatarListAdapter
) : RecyclerView.Adapter<AvatarListAdapter.ViewHolder>()
{
    var onItemClick: ((User) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var avatar = listItemView.findViewById(R.id.avatar_picture) as ImageView;
        var username = listItemView.findViewById(R.id.username) as TextView;
        init {
            listItemView.setOnClickListener {
                onItemClick?.invoke(avatarList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context;
        val inflater = LayoutInflater.from(context)
        var columnView =  inflater.inflate(R.layout.avatar, parent, false)
        return ViewHolder(columnView)

    }

    override fun getItemCount(): Int {
        return avatarList.size;
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val author = avatarList.get(position);
        val avatar = holder.avatar;
        val username = holder.username;
        username.setText(author.full_name);
        Glide.with(avatar.context)
            .load(author.avatar)
            .apply(RequestOptions().override(250, 250))
            .circleCrop()
            .into(avatar)

        holder.itemView.setOnClickListener {
            iAvatarListAdapter.onClickItemAuthor(author)
        }
    }

}



