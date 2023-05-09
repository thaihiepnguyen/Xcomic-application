package com.example.x_comic.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.x_comic.R
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.UserViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AccountsAdapter (
    private var accounts: MutableList<User>
) : RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {
    var onItemClick: ((account: User, position: Int) -> Unit)? = null
    var onButtonClick: ((User) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val avatar = listItemView.findViewById(R.id.ivAvatarAccount) as ImageView
        val name = listItemView.findViewById(R.id.tvNameAccount) as TextView
        val dob = listItemView.findViewById(R.id.tvDOBAccount) as TextView
        val email = listItemView.findViewById(R.id.tvEmailAccount) as TextView
        val btnBlock = listItemView.findViewById(R.id.btnBlockAccount) as Button

        init {
            listItemView.setOnClickListener { onItemClick?.invoke(accounts[adapterPosition], adapterPosition) }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val accountView = inflater.inflate(R.layout.account_list, parent, false)
        // Return a new holder instance
        return ViewHolder(accountView)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val a: User = accounts.get(position)
        // Set item views based on your views and data model
        holder.name.text = accounts[position].full_name
        holder.dob.text = accounts[position].dob
        holder.email.text = accounts[position].email

        if (accounts[position].hide)
            holder.btnBlock.text = "UNBLOCK"
        else
            holder.btnBlock.text = "BLOCK"

        holder.btnBlock.setOnClickListener {
            val database = FirebaseDatabase.getInstance("https://x-comic-e8f15-default-rtdb.asia-southeast1.firebasedatabase.app")
            val db = database.getReference("users")
            db
                .child(accounts[position].id)
                .child("hide")
                .setValue(!accounts[position].hide)
                .addOnSuccessListener {
                    // Call notifyItemChanged to rebind this ViewHolder
                    notifyItemChanged(position)
                }
            accounts[position].hide = !accounts[position].hide;
            if (accounts[position].hide)
                holder.btnBlock.text = "UNBLOCK"
            else
                holder.btnBlock.text = "BLOCK"
        }

//        val storage = FirebaseStorage.getInstance()
//        val imageName = accounts[position].id + ".png" // Replace with your image name
//        val imageRef = storage.reference.child("users/$imageName")
//        imageRef.getBytes(Long.MAX_VALUE)
//            .addOnSuccessListener { bytes -> // Decode the byte array into a Bitmap
//                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                // Set the Bitmap to the ImageView
//                holder.avatar.setImageBitmap(bitmap)
//
//            }.addOnFailureListener {
//                // Handle any errors
//            }
        Glide.with(holder.avatar.context)
            .load(accounts[position].avatar)
            .apply(RequestOptions().override(250, 250))
            .circleCrop()
            .into(holder.avatar)
    }

    public fun setFilterList(list: MutableList<User>) {
        this.accounts = list
        notifyDataSetChanged()
    }
}
