package com.example.x_comic.views.main.fragments

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.beust.klaxon.Klaxon
import com.example.x_comic.R
import com.example.x_comic.adapters.CategoryAdapter
import com.example.x_comic.adapters.ExploreGridBookAdapter
import com.example.x_comic.models.Category
import com.example.x_comic.models.Product
import com.example.x_comic.viewmodels.CategoryViewModel
import com.example.x_comic.viewmodels.ProductViewModel
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager


class Explore : Fragment() {


//    val category_list: MutableList<String> = mutableListOf("Romance","Poetry","Science Fiction","Teen Fiction","Short Story","Mystery","Adventure","Thriller","Horror","Humor","LGBT+","Non Fiction","Fanfiction","Historical Fiction","Contemporary Lit","Diverse Lit","Fantasy","Paranormal","New Adult")
    var btnLength: TextView? = null;
    var btnStatus: TextView? = null;
    var searchBtn: ImageButton? = null;
    var layoutExpand2 : ExpandableRelativeLayout? = null;
    var layoutExpand: ExpandableRelativeLayout? = null;

    var categoryView : RecyclerView? = null;

    var myFilter = com.example.x_comic.models.Filter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        categoryView = view.findViewById(R.id.category_list);

        searchBtn = view.findViewById(R.id.searchBtn);
        val layoutManager = FlexboxLayoutManager(context);
        var adapter = CategoryAdapter(ArrayList<Category>());
        var categoryViewModel: CategoryViewModel
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

     

        categoryViewModel.getAll()
            .observe(this, Observer { categories ->
                run {
                    adapter = CategoryAdapter(categories);

                    categoryView!!.adapter = adapter;

                    layoutManager!!.flexWrap = FlexWrap.WRAP;
                    layoutManager!!.flexDirection = FlexDirection.ROW;
                    layoutManager!!.alignItems = AlignItems.FLEX_START;
                    categoryView!!.layoutManager = layoutManager;

                    myFilter.listCate = adapter.getAllItem()
                }
            })


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
            hideKeyboard(view);
            val fragment = Search();

            //val oldFragment = view.findViewById<FrameLayout>(R.id.exploreLayout);
            //oldFragment.removeAllViews();

            var searchEditText = view.findViewById<EditText>(R.id.searchEditText)
            var keyword = searchEditText.text.toString()

            val bundle = Bundle()
            bundle.putString("keyword", keyword)
            bundle.putString("filter", Klaxon().toJsonString(myFilter))

            // Pass the bundle to the fragment
            fragment.arguments = bundle

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
                checkBox2.isChecked = false;
                checkBox3.isChecked = false;
                checkBox4.isChecked = false;
                myFilter.chapterRange = "1-10"
            }
        }

        checkBox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false;
                checkBox3.isChecked = false;
                checkBox4.isChecked = false;
                checkBox5.isChecked = false;
                myFilter.chapterRange = "10-20"
            }
        }

        checkBox3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false;
                checkBox2.isChecked = false;
                checkBox4.isChecked = false;
                checkBox5.isChecked = false;
                myFilter.chapterRange = "20-50"
            }
        }

        checkBox4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false;
                checkBox2.isChecked = false;
                checkBox3.isChecked = false;
                checkBox5.isChecked = false;
                myFilter.chapterRange = "50+"
            }
        }

        checkBox5.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox1.isChecked = false;
                checkBox2.isChecked = false;
                checkBox3.isChecked = false;
                checkBox4.isChecked = false;
                myFilter.chapterRange = "all"
            }
        }


        checkBox6.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
                myFilter.bookStatus = "Completed"
            }
        }

        checkBox7.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
                myFilter.bookStatus = "Ongoing"
            }
        }

        checkBox8.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
                myFilter.bookStatus = "Ongoing"
            }
        }

        checkBox9.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox10.isChecked = false;
                myFilter.bookStatus = "Ongoing"
            }
        }

        checkBox10.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox6.isChecked = false;
                checkBox7.isChecked = false;
                checkBox8.isChecked = false;
                checkBox9.isChecked = false;
                myFilter.bookStatus = "All"
            }
        }


        view.findViewById<EditText>(R.id.searchEditText).setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(view)
            }
        })

        view.findViewById<EditText>(R.id.searchEditText).setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                hideKeyboard(view)
                val fragment = Search();

                //val oldFragment = view.findViewById<FrameLayout>(R.id.exploreLayout);
                //oldFragment.removeAllViews();
                var searchEditText = view.findViewById<EditText>(R.id.searchEditText)
                var keyword = searchEditText.text.toString()
                val bundle = Bundle()
                bundle.putString("keyword", keyword)
                bundle.putString("filter", Klaxon().toJsonString(myFilter))
                fragment.arguments = bundle
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                //transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.detach(Explore());
                transaction.remove(Explore());
                transaction.replace(R.id.exploreLayout, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
                return@OnKeyListener true
            }
            false
        })
//        val bookListSlideShow: MutableList<Product> = mutableListOf()
//        val gridView = view.findViewById<RecyclerView>(R.id.gridView)
//        val exploreGridBookAdapter = ExploreGridBookAdapter(requireActivity(), bookListSlideShow)
//        gridView.adapter = exploreGridBookAdapter
//        gridView.layoutManager = GridLayoutManager(requireContext(), 2)
//        var productViewModel: ProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
//        productViewModel.getAllBook { books ->
//            run {
//                bookListSlideShow.clear()
//                for (book in books.children) {
//                    val product = book.getValue(Product::class.java)
//                    if (product != null && !product.hide) {
//                        bookListSlideShow.add(product)
//                    }
//                }
//                exploreGridBookAdapter.notifyDataSetChanged()
//            }
//        }
        return view
    }
    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }
}