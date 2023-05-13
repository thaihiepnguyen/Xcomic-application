package com.example.x_comic.views.main.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.x_comic.R
import com.example.x_comic.adapters.CollectionAdapter
import com.example.x_comic.models.*
import com.example.x_comic.views.main.AddCollectionBookActivity
import com.example.x_comic.views.main.CollectionActivity
import com.example.x_comic.views.read.ReadBookActivity
import kotlin.random.Random


class Collection : Fragment() {
    private var collectionName: String = ""

    var listCollection: MutableList<CollectionReading> = mutableListOf();
    var btnAddCollection: Button? = null;
    var adapter: CollectionAdapter? = null;
    var collectionBook: RecyclerView? = null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_collection, container, false)

        collectionBook = view.findViewById(R.id.collectionBook);

        btnAddCollection = view.findViewById(R.id.btn_add_collection);

        btnAddCollection!!.setOnClickListener{
            val dialog = CollectionDialogFragment()
            dialog.show(getFragmentManager()!!,"dbchau10");

            dialog.setFragmentResultListener("1"){ key, bundle ->
                if (key == "1"){

                    collectionName =  bundle.getString("1")!!

                    val intent = Intent(context, AddCollectionBookActivity::class.java)
                    intent.putExtra("name", collectionName);
                    startActivityForResult(intent, 123)
                }
            }


        }



        println(listCollection);

       adapter = CollectionAdapter(listCollection);

        collectionBook!!.adapter = adapter;

        collectionBook!!.layoutManager = GridLayoutManager(this.context, 3)

        adapter!!.onItemClick = { collection ->

            val intent = Intent(getActivity(), CollectionActivity::class.java)
            startActivityForResult(intent, 123)

        }

        fun checkNumberRule(num: Int): Boolean {
            var curr = 0 // start with 0
            var diff = 3 // initialize the difference between consecutive numbers to 3
            while (curr < num) {
                curr += diff // add the current difference to the previous number
                diff = if (diff == 3) 4 else 3 // switch between adding 3 and 4 to the previous number
            }
            return curr == num // if the final number is equal to the input number, it satisfies the rule
        }

        val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
          return if (checkNumberRule(position)) 2 else 1 // Make every third item span 2 columns

            }
        }
        (collectionBook!!.layoutManager as GridLayoutManager).spanSizeLookup = spanSizeLookup



        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 123) {
            if (resultCode === Activity.RESULT_OK) {
                val reply = data!!.getSerializableExtra("collection") as? CollectionReading;
                if (reply != null) {
                    listCollection.add(reply);
                    adapter!!.notifyDataSetChanged();

                }
            }
        }
    }




}


class CollectionDialogFragment : DialogFragment() {

    private lateinit var mInput: EditText
    private lateinit var mActionOk: TextView
    private lateinit var mActionCancel: TextView




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_collection_fragment, container, false)
        mActionCancel = view.findViewById(R.id.action_cancel)
        mActionOk = view.findViewById(R.id.action_ok)
        mInput = view.findViewById(R.id.input)

        mActionCancel.setOnClickListener {

            dialog?.dismiss()
        }

        mActionOk.setOnClickListener {

            val input = mInput.text.toString()
            val bundle = Bundle()

            bundle.putString("1", input)
            setFragmentResult("1", bundle)

            dismiss()
            dialog?.dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
        }
    }
}