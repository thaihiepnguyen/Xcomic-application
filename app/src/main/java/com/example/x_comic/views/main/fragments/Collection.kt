package com.example.x_comic.views.main.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.CollectionAdapter
import com.example.x_comic.models.*
import com.example.x_comic.viewmodels.FirebaseAuthManager
import com.example.x_comic.viewmodels.ProductViewModel
import com.example.x_comic.viewmodels.UserViewModel
import com.example.x_comic.views.collection.AddCollectionBookActivity
import com.example.x_comic.views.collection.CollectionActivity


var listCollection: MutableList<CollectionReading> = mutableListOf();
class Collection : Fragment() {
    private var collectionName: String = ""


    var btnAddCollection: Button? = null;
    var adapter: CollectionAdapter? = null;
    var collectionBook: RecyclerView? = null;
    private lateinit var userViewModel: UserViewModel
    private lateinit var productViewModel: ProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_collection, container, false)

        collectionBook = view.findViewById(R.id.collectionBook);

        btnAddCollection = view.findViewById(R.id.btn_add_collection);

        btnAddCollection!!.setOnClickListener{
            val dialog = CollectionDialogFragment("Create a Collection")
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


        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        val uid = FirebaseAuthManager.auth.uid;


        if (uid != null) {

            productViewModel.getAllCollection(uid) { booksID ->
                run {
                    listCollection.clear();

                    for (snapshot in booksID.children) {

                        var bookid = snapshot.getValue(CollectionReading::class.java)
                        if (bookid!=null) {
                            listCollection.add(bookid)
                            println(listCollection)
                            adapter!!.notifyDataSetChanged();
                        }

                    }

                    adapter!!.notifyDataSetChanged();


                }


            }

        }


        adapter!!.onItemClick = { collection ->


            val intent = Intent(context, CollectionActivity::class.java)
            intent.putExtra("collection", collection);
            println(listCollection.indexOf(collection))
            intent.putExtra("position", listCollection.indexOf(collection));
            startActivity(intent)

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
                    val uid = FirebaseAuthManager.auth.uid;
                    var productViewModel: ProductViewModel = ProductViewModel();
                    if (uid!=null) {
                        productViewModel.updateCollection(uid, listCollection);
                    }

                }
            }
        }


    }





}


class CollectionDialogFragment(var dialogName: String, var name: String= "") : DialogFragment() {

    private lateinit var mInput: EditText
    private lateinit var mActionOk: TextView
    private lateinit var mActionCancel: TextView
    private  lateinit var heading: TextView
    private  lateinit var input: EditText



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_collection_fragment, container, false)
        mActionCancel = view.findViewById(R.id.action_cancel)
        mActionOk = view.findViewById(R.id.action_ok)
        mInput = view.findViewById(R.id.input)
        heading = view.findViewById(R.id.heading)
        input = view.findViewById(R.id.input)
        heading.setText(dialogName)
        input.setText(name);

        mActionCancel.setOnClickListener {

            dialog?.dismiss()
        }

        mActionOk.setOnClickListener {
            var input: String="";
            if (mInput.text.toString()!="") {
               input = mInput.text.toString()
            }else {
               input = "Collection ${listCollection.size}"
            }


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