package com.example.iptv.Views.Adapters

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.iptv.Views.Fragments.*

class PagerAdapter(fm: FragmentManager, private val countTabs: Int) :
    FragmentStatePagerAdapter(fm, countTabs) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MediaPlayerFragment()
            1 -> ProductFragment()
            2 -> SocialMediaFragment()
            3 -> InfoFragment()
            else -> null
        }!!
    }

    override fun getCount(): Int {
        return countTabs
    }
}