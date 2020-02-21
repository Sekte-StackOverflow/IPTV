package com.example.iptv.Views.Fragments


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.iptv.R
import com.example.iptv.ViewModels.SessionViewModel
import com.example.iptv.Views.Activities.AuthActivity
import com.example.iptv.api.service.AppKey
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*

/**
 * A simple [Fragment] subclass.
 */
class InfoFragment : Fragment() {
    private lateinit var sessionViewModel: SessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sessionViewModel = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        sessionViewModel.init(requireContext())
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info_email.isEnabled = true
        info_no.isEnabled = true
        btn_beriktnya.isEnabled = false

        sessionViewModel.isLoginLive().observe(this, Observer {
            isLogin ->
            if (isLogin) {
                sessionViewModel.getUser().observe(this, Observer {
                    user ->
                    if (user.email != "") {
                        info_email.setText(user.email)
                        info_email.isEnabled = false
                        info_no.isEnabled = false
                        btn_beriktnya.isEnabled = false
                    } else {
                        info_no.setText(user.phone)
                        info_no.isEnabled = false
                        info_email.isEnabled = false
                        btn_beriktnya.isEnabled = false
                    }
                })
            }
        })

        btn_beriktnya.isEnabled = !(TextUtils.isEmpty(info_no.text.toString().trim()) ||
                TextUtils.isEmpty(info_email.text.toString().trim()))

        info_no.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                info_email.isEnabled = s.isNullOrEmpty()
                if (switch1.isChecked) {
                    btn_beriktnya.isEnabled = !s.isNullOrEmpty()
                }
            }
        })

        switch1.setOnCheckedChangeListener{
            buttonView, isChecked ->
            if (info_email.text != null || info_no.text !== null) {
                if (info_email.text.toString() !== "" || info_no.text.toString() !== "" ) {
                    btn_beriktnya.isEnabled = isChecked
                } else btn_beriktnya.isEnabled = false
            } else btn_beriktnya.isEnabled = false
        }

        info_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                info_no.isEnabled = s.isNullOrEmpty()
                if (switch1.isChecked) {
                    btn_beriktnya.isEnabled = !s.isNullOrEmpty()
                }
            }
        })

        btn_beriktnya.setOnClickListener{
            val data: String = if (!TextUtils.isEmpty(info_no.text.toString().trim())) info_no.text.toString()
            else info_email.text.toString()

            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(AppKey.ACTIVITY_KEY().AUTH_ACT, AppKey.FRAGMENT_KEY().REGISTER_F)
            if (data.toLowerCase().contains("@")) {
                intent.putExtra("NOHP_EMAIL", data)
            } else {
                intent.putExtra("NOHP_EMAIL", "+62$data")
            }
            startActivity(intent)
        }

        sudah_punya_akun.setOnClickListener{
            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(AppKey.ACTIVITY_KEY().AUTH_ACT, AppKey.FRAGMENT_KEY().LOGIN_F)
            startActivity(intent)
        }

        policy_link.setOnClickListener{
            showPolcy()
        }
    }

    fun showPolcy() {
        val dialog = Dialog(requireContext())
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val dialogView = layoutInflater.inflate(R.layout.policy_layout, null)
        dialog.setContentView(dialogView)
        dialog.show()
    }



}
