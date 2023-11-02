package com.example.medicapp_vip.view.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HS_adapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    val pages = ArrayList<Fragment>()
    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    fun addFragment(frag: Fragment){
        pages.add(frag)
    }

}