package com.example.x_comic.views.main.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.BookReadingAdapter

import com.example.x_comic.models.BookReading

import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel



class Reading : Fragment() {

    var listReadingOffline: MutableList<BookReading> = mutableListOf()

    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    var listReading: MutableList<BookReading> = mutableListOf();

    var customOfflineBookList : RecyclerView? = null;
    var customOnlineBookList: RecyclerView? = null;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_reading, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        customOfflineBookList = view.findViewById(R.id.offline_books);
        customOnlineBookList = view.findViewById(R.id.online_books);
        var online_number = view.findViewById<TextView>(R.id.number_online)

        val adapter = BookReadingAdapter(listReadingOffline);

        val OnlineAdapter = BookReadingAdapter(listReading, true);


        customOfflineBookList!!.adapter = adapter;

        customOnlineBookList!!.adapter = OnlineAdapter;


        customOfflineBookList!!.layoutManager = GridLayoutManager(this.context,3);
        customOnlineBookList!!.layoutManager = GridLayoutManager(this.context,3);




        val uid = FirebaseAuthManager.auth.uid
        if (uid != null) {

            productViewModel.getAllReadingBook(uid) { booksID ->
                run {
                    listReading.clear();
                    var cnt: Int = 0


                    for (snapshot in booksID.children) {

                                var bookid = snapshot.getValue(com.example.x_comic.models.Reading::class.java)

                                productViewModel.getBookById(bookid!!.id_book) { bookInner ->
                                    run {

                                        if ( bookInner!=null && !bookInner.hide) {

                                            cnt++
                                            listReading.add(0,
                                                BookReading(
                                                    bookInner,
                                                    bookid.posChap,
                                                    bookid.numChap
                                                )

                                            )

                                            println(listReading);

                                      OnlineAdapter.notifyItemInserted(0);
                                           // OnlineAdapter.notifyDataSetChanged()
                                        }
                                   //     OnlineAdapter.notifyDataSetChanged()

                                    }
                                  //
                                    online_number.text = "${cnt} Stories"





                                }




                    }





                }


            }

        }




        return view
    }


}