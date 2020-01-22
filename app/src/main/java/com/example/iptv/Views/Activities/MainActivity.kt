package com.example.iptv.Views.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.iptv.R
import com.example.iptv.Views.Adapters.PagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerAdapter = PagerAdapter(supportFragmentManager, tabs.tabCount)
        view_pager.adapter = pagerAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(this)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(main_activity_container.id, fragment)
            .commit()
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        view_pager.currentItem = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) { }

    override fun onTabSelected(tab: TabLayout.Tab) {
        view_pager.currentItem = tab.position
    }
}
