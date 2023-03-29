package com.example.x_comic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.Avatar

class AvatarListAdapter (
    private var avatarList: MutableList<Avatar>,
) : RecyclerView.Adapter<AvatarListAdapter.ViewHolder>()
{
    var onItemClick: ((Avatar) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        var avatar = listItemView.findViewById(R.id.avatar_picture) as ImageButton;
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
        username.setText(author.username);
        avatar.setImageResource(author.avatarPicture);

    }

}



