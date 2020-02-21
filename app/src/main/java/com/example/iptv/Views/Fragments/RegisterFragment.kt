package com.example.iptv.Views.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.iptv.Models.User
import com.example.iptv.Models.UserReg

import com.example.iptv.R
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun newAccount(user: UserReg)
    }

}
