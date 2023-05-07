package com.example.x_comic.views.admin

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.AccountsAdapter
import com.example.x_comic.models.Chapter
import com.example.x_comic.models.Product
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.post.PostNewActivity
import com.google.firebase.storage.FirebaseStorage

class AccountManagementActivity : AppCompatActivity() {

    var accountList: MutableList<User> = mutableListOf(
//        User("01", "Adam Jost", 23, "@drawable/avatar_1", "", "01/01/2000",
//            "account@gmail.com", 0, false, "", "", "","", 0),
//        User("01", "Adam Jost", 23, "@drawable/avatar_2", "", "01/01/2000",
//        "account@gmail.com", 0, false, "", "", "", "",0),
//        User("01", "Adam Jost", 23, "@drawable/avatar_3", "", "01/01/2000",
//            "account@gmail.com", 0, false, "", "", "", "",0),
//        User("01", "Adam Jost", 23, "@drawable/avatar_4", "", "01/01/2000",
//            "account@gmail.com", 0, false, "", "", "", "",0),
//        User("01", "Adam Jost", 23, "@drawable/avatar_5", "", "01/01/2000",
//            "account@gmail.com", 0, false, "", "", "", "",0),
//        User("01", "Adam Jost", 23, "@drawable/avatar_6", "", "01/01/2000",
//            "account@gmail.com", 0, false, "", "", "", "",0),
    )
    var customAccountListView: RecyclerView? = null;
    private lateinit var userViewModel: UserViewModel
    private lateinit var accountListAdapter : AccountsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContentView(R.layout.activity_account_management)

        customAccountListView = findViewById(R.id.accountManagement_list) as RecyclerView;
        accountListAdapter = AccountsAdapter(accountList);
        customAccountListView!!.adapter = accountListAdapter;
        customAccountListView!!.layoutManager = LinearLayoutManager(this);
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        customAccountListView?.addItemDecoration(itemDecoration)

        accountListAdapter!!.onItemClick = {chapter, position ->
            // DO SOMETHING
        }
        // Search View
        val searchView = findViewById<SearchView>(R.id.searchViewAccount)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Called when the user submits the query
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Called when the query text is changed by the user
                filterAccount(newText);
                return true
            }
        })

        // Back
        var btnBack = findViewById<ImageButton>(R.id.btnBackManageScreen)
        btnBack.setOnClickListener {
            finish()
        }

        userViewModel.getAllUser {users -> kotlin.run {
            accountList.clear()
            for (user in users.children) {
                val acc = user.getValue(User::class.java)
                if (acc != null) {
                    accountList.add(acc)
                }
            }
            accountListAdapter.notifyDataSetChanged()
        }
        }
    }

    private fun filterAccount(newText: String) {
        var filterList: MutableList<User> = mutableListOf()

        for (item in accountList) {
            if (item.full_name.toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(item)
            }
        }

        if (filterList.isEmpty()) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            accountListAdapter.setFilterList(filterList)
            accountListAdapter.notifyDataSetChanged()
        }
    }
}