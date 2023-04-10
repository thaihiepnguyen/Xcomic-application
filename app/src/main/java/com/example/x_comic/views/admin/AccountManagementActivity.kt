package com.example.x_comic.views.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.User
import com.example.x_comic.views.post.PostNewActivity

class AccountManagementActivity : AppCompatActivity() {

    var accountList: MutableList<User> = mutableListOf(
        User("01", "Adam Jost", 23, "@drawable/avatar_1", "", "01/01/2000",
            "account@gmail.com", 0, false, "", "", "", 0),
        User("01", "Adam Jost", 23, "@drawable/avatar_2", "", "01/01/2000",
        "account@gmail.com", 0, false, "", "", "", 0),
        User("01", "Adam Jost", 23, "@drawable/avatar_3", "", "01/01/2000",
            "account@gmail.com", 0, false, "", "", "", 0),
        User("01", "Adam Jost", 23, "@drawable/avatar_4", "", "01/01/2000",
            "account@gmail.com", 0, false, "", "", "", 0),
        User("01", "Adam Jost", 23, "@drawable/avatar_5", "", "01/01/2000",
            "account@gmail.com", 0, false, "", "", "", 0),
        User("01", "Adam Jost", 23, "@drawable/avatar_6", "", "01/01/2000",
            "account@gmail.com", 0, false, "", "", "", 0),
    )
    var customAccountListView: RecyclerView? = null;

    class AccountsAdapter (private val accounts: MutableList<User>) : RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {
        var onItemClick: ((account: User, position: Int) -> Unit)? = null
        var onButtonClick: ((User) -> Unit)? = null
        inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
            val avatar = listItemView.findViewById(R.id.ivAvatarAccount) as ImageView
            val name = listItemView.findViewById(R.id.tvNameAccount) as TextView
            val dob = listItemView.findViewById(R.id.tvDOBAccount) as TextView
            val email = listItemView.findViewById(R.id.tvEmailAccount) as TextView

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
//            holder.avatar.setImageResource(accounts[position].avatar.toInt())
            holder.avatar.setImageResource(R.drawable.avatar_1)

            holder.name.text = accounts[position].full_name
            holder.dob.text = accounts[position].dob
            holder.email.text = accounts[position].email
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        customAccountListView = findViewById(R.id.accountManagement_list) as RecyclerView;
        val accountListAdapter = AccountManagementActivity.AccountsAdapter(accountList);
        customAccountListView!!.adapter = accountListAdapter;
        customAccountListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customAccountListView?.addItemDecoration(itemDecoration)

        accountListAdapter!!.onItemClick = {chapter, position ->
            // DO SOMETHING
        }
    }
}