package com.example.iptv.Views.Fragments

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.iptv.Models.AppDataSet
import com.example.iptv.Models.User
import com.example.iptv.Models.UserReg

import com.example.iptv.R
import com.example.iptv.ViewModels.myActivitiesViewModel
import com.example.iptv.api.service.AppKey
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegisterFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class RegisterFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var myActivities: myActivitiesViewModel
    private lateinit var profile: AppDataSet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myActivities = ViewModelProviders.of(this).get(myActivitiesViewModel::class.java)
        myActivities.init()
        myActivities.getAppUtil().observe(this, Observer {
            profile = it[0]
        })

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!activity?.intent?.getStringExtra("NOHP_EMAIL").isNullOrEmpty()) {
            val data = activity?.intent?.getStringExtra("NOHP_EMAIL")
            reg_phone_email.setText(data)
        }

        reg_btn.setOnClickListener{
            if (isValid()) {
                val username = reg_phone_email.text.toString().substringBefore("@")
                if (reg_phone_email.text.toString().contains("@")) {
                    val user = UserReg(
                        reg_phone_email.text.toString(),
                        "",
                        reg_pass.text.toString(),
                        "Android",
                        "Email",
                        username
                    )
                    listener!!.newAccount(user)
                } else {
                    val user = UserReg(
                        "",
                        reg_phone_email.text.toString(),
                        reg_pass.text.toString(),
                        "Android",
                        "Phone",
                        reg_phone_email.text.toString()
                    )
                    listener!!.newAccount(user)
                }
            } else {
                Log.d(AppKey.FRAGMENT_KEY().REGISTER_F, "Valid = false")
            }
        }

        click_terms.setOnClickListener {
            showPolcy()
        }
    }

    private fun isValid(): Boolean {
        var valid = false
        if (
            reg_phone_email.text.isNullOrEmpty() ||
            reg_pass.text.isNullOrEmpty() ||
            reg_pass2.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please Fill The Form", Toast.LENGTH_SHORT).show()
        } else if (!reg_agreement.isChecked) {
            Toast.makeText(requireContext(), "Please Check Agreement!",  Toast.LENGTH_SHORT).show()
        } else if (reg_pass.text.toString() != reg_pass2.text.toString()) {
            Toast.makeText(requireContext(), "Password Not Match", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(AppKey.FRAGMENT_KEY().REGISTER_F, "Register Form Valid")
            valid = true
        }
        return valid
    }

    fun showPolcy() {
        val dialog = Dialog(requireContext())
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.policy_layout)
        val text = dialog.window!!.decorView.findViewById<TextView>(R.id.text_policy)
        text.text = profile.sk
        dialog.show()
    }

    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun newAccount(user: UserReg)
    }

}
