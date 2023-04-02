package com.example.x_comic.views.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.x_comic.R
import com.example.x_comic.adapters.FragmentAdapter
import com.google.android.material.tabs.TabLayout


class Library : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        var viewPager: ViewPager = view.findViewById(R.id.view_pager);

        var tabLayout: TabLayout = view.findViewById(R.id.tab_layout);

        val fragmentAdapter = FragmentAdapter(requireActivity().supportFragmentManager);
        fragmentAdapter.addFragment(Reading(),"Recent Reading");
        fragmentAdapter.addFragment(Collection(), "Collection");

        viewPager.adapter = fragmentAdapter;
        tabLayout.setupWithViewPager(viewPager);


        return view
    }


}