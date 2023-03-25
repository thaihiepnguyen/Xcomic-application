package com.example.x_comic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val bookList: MutableList<BookSneek> = mutableListOf(
        BookSneek("How to Burn The Bad Boy","alsophanie",R.drawable.bookcover),
        BookSneek("Temporarily","bbiboo123",R.drawable.book_cover_1),
        BookSneek("Rome Is Us","ann_beyond",R.drawable.book_cover_2),
        BookSneek("Fool Man","landyshere",R.drawable.book_cover_3),
        BookSneek("The Mind Of A Leader","vivianneee",R.drawable.book_cover_4),
        BookSneek("The Light Beyond The Garden Wall","pixiequinn",R.drawable.book_cover_5),
        BookSneek("The Secret At The Joneses","rhjulxie",R.drawable.book_cover_6),
        BookSneek("The Victim's Picture","gizashey",R.drawable.book_cover_7)

    )
    var customSlideView: RecyclerView? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        customSlideView = view.findViewById(R.id.listView);
        val adapter = ListAdapterSlideshow(bookList);
        customSlideView!!.adapter = adapter;
        customSlideView!!.layoutManager = LinearLayoutManager(this.context,RecyclerView.HORIZONTAL, false);
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}