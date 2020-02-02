package com.example.iptv.Views.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.iptv.R
import com.example.iptv.Views.Fragments.AboutFragment
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        actionBar?.show()
        actionBar?.title = "Second Activity"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        when(intent.getStringExtra("Fragment")) {
            "ABOUT_FRAGMENT" -> loadFragment(AboutFragment())
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.second_activity_id, fragment)
            .commit()
    }
}
