package com.example.iptv.Views.Fragments


import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.iptv.BuildConfig

import com.example.iptv.R
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_product_detail.*

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {
    private val NO_UPDATE = "No Update Available"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        about_appVersion.text = "v ${BuildConfig.VERSION_NAME}"
        about_checkUpdate.setOnClickListener {
            Toast.makeText(context, NO_UPDATE, Toast.LENGTH_SHORT).show()
        }
    }


}
