package com.example.iptv.Views.Fragments


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.iptv.R
import com.example.iptv.Views.Activities.AuthActivity
import com.example.iptv.api.service.AppKey
import kotlinx.android.synthetic.main.fragment_info.*

/**
 * A simple [Fragment] subclass.
 */
class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_beriktnya.isEnabled = !(TextUtils.isEmpty(info_no.text.toString().trim()) ||
                TextUtils.isEmpty(info_email.text.toString().trim()))

        info_no.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                info_email.isEnabled = s.isNullOrEmpty()
                btn_beriktnya.isEnabled = !s.isNullOrEmpty()
            }
        })

        info_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                info_no.isEnabled = s.isNullOrEmpty()
                btn_beriktnya.isEnabled = !s.isNullOrEmpty()
            }
        })




        btn_beriktnya.setOnClickListener{
            val data: String = if (!TextUtils.isEmpty(info_no.text.toString().trim())) info_no.text.toString()
            else info_email.text.toString()

            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(AppKey.ACTIVITY_KEY().AUTH_ACT, AppKey.FRAGMENT_KEY().REGISTER_F)
            intent.putExtra("NOHP_EMAIL", "+62$data")
            startActivity(intent)
        }

        sudah_punya_akun.setOnClickListener{
            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(AppKey.ACTIVITY_KEY().AUTH_ACT, AppKey.FRAGMENT_KEY().LOGIN_F)
            startActivity(intent)
        }
    }


}
