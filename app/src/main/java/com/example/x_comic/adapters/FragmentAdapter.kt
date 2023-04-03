package com.example.x_comic.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentAdapter(fragment: FragmentManager) : FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var fragmentList: MutableList<Fragment> = mutableListOf();
    var fragmentTitle: MutableList<String> = mutableListOf();


    override fun getCount(): Int {
        return fragmentList.size;
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment);
        fragmentTitle.add(title);
    }
}