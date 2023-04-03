package com.example.x_comic.views.main.fragments

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.x_comic.R
import com.example.x_comic.adapters.CategoryAdapter
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager




class Explore : Fragment() {


    val category_list: MutableList<String> = mutableListOf("Romance","Poetry","Science Fiction","Teen Fiction","Short Story","Mystery","Adventure","Thriller","Horror","Humor","LGBT+","Non Fiction","Fanfiction","Historical Fiction","Contemporary Lit","Diverse Lit","Fantasy","Paranormal","New Adult")
    var btnLength: TextView? = null;
    var btnStatus: TextView? = null;
    var searchBtn: ImageButton? = null;
    var layoutExpand2 : ExpandableRelativeLayout? = null;
    var layoutExpand: ExpandableRelativeLayout? = null;

    var categoryView : RecyclerView? = null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        categoryView = view.findViewById(R.id.category_list);
        val adapter = CategoryAdapter(category_list);
        searchBtn = view.findViewById(R.id.searchBtn);
        categoryView!!.adapter = adapter;
        val layoutManager = FlexboxLayoutManager(context);
        layoutManager!!.flexWrap = FlexWrap.WRAP;
        layoutManager!!.flexDirection = FlexDirection.ROW;
        layoutManager!!.alignItems = AlignItems.FLEX_START;


        categoryView!!.layoutManager = layoutManager;

        btnLength = view.findViewById(R.id.length);
        btnStatus = view.findViewById(R.id.status);

        layoutExpand = view.findViewById(R.id.expandableLayout);
        layoutExpand2 = view.findViewById(R.id.expandableLayout2);
        layoutExpand2!!.collapse();

        btnStatus!!.setOnClickListener{
            layoutExpand2!!.toggle();
            if (layoutExpand2!!.isExpanded){
                btnStatus!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevrondown,0)
            }
            else {
                btnStatus!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevronup,0)

            }
        }

        layoutExpand!!.collapse();
        btnLength!!.setOnClickListener{

            layoutExpand!!.toggle();
            if (layoutExpand!!.isExpanded){
                btnLength!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevrondown,0)
            }
            else {
                btnLength!!.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.chevronup,0)

            }
        }


        searchBtn!!.setOnClickListener{

            val fragment = Search();

            //val oldFragment = view.findViewById<FrameLayout>(R.id.exploreLayout);
            //oldFragment.removeAllViews();


            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            //transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.detach(Explore());
            transaction.remove(Explore());
            transaction.replace(R.id.exploreLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()


        }

        val checkBox1: CheckBox = view.findViewById(R.id.checkBox1);
        val checkBox2: CheckBox = view.findViewById(R.id.checkBox2);
        val checkBox3: CheckBox = view.findViewById(R.id.checkBox3);
        val checkBox4: CheckBox = view.findViewById(R.id.checkBox4);
        val checkBox5: CheckBox = view.findViewById(R.id.checkBox5);

        val checkBox6: CheckBox = view.findViewById(R.id.checkBox6);
        val checkBox7: CheckBox = view.findViewById(R.id.checkBox7);
        val checkBox8: CheckBox = view.findViewById(R.id.checkBox8);
        val checkBox9: CheckBox = view.findViewById(R.id.checkBox9);
        val checkBox10: CheckBox = view.findViewById(R.id.checkBox10);


        checkBox1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox5.isChecked = false;
            }
        }

        checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox5.isChecked = false;
            }
        }

        checkBox3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox5.isChecked = false;
            }
        }

        checkBox4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox5.isChecked = false;
            }
        }

        checkBox5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false;
                checkBox2.isChecked = false;
                checkBox3.isChecked = false;
                checkBox4.isChecked = false;
            }
        }


        checkBox6.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
            }
        }

        checkBox7.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
            }
        }

        checkBox8.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
            }
        }

        checkBox9.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
            }
        }

        checkBox10.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox6.isChecked = false;
                checkBox7.isChecked = false;
                checkBox8.isChecked = false;
                checkBox9.isChecked = false;
            }
        }


        return view
    }

}