package com.example.iptv.Views.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly

import com.example.iptv.R
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class LoginFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn.setOnClickListener {
            var emailValidation = false
            var passValidation = false

            if (TextUtils.isEmpty(login_email_phone.text.toString().trim())) {
                login_emailLayout.error = "Email or Phone is Empty!"
                login_email_phone.isFocusable = true
                login_email_phone.requestFocus()
                emailValidation = false
            } else {
                login_emailLayout.isErrorEnabled = false
                emailValidation = true
            }

            if (TextUtils.isEmpty(login_password.text.toString().trim())) {
                login_passLayout.error = "Please check please your password"
                login_password.isFocusable = true
                login_password.requestFocus()
                passValidation = false
            } else {
                login_passLayout.isErrorEnabled = false
                passValidation = true
            }

            if (emailValidation && passValidation) {
                // example
                if (isNumeric(login_email_phone.text.toString())) {
                    if (login_email_phone.text!!.equals("08123456789")
                        && login_password.text!!.equals("12345678")) {
                        Toast.makeText(context, "Login Success Phone", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (login_email_phone.text!!.equals("Rifaul@gmail.com")
                        && login_password.text!!.equals("12345678")) {
                        Toast.makeText(context, "Login Success email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        login_forgetPass.setOnClickListener{
            listener!!.forgetPassword()
        }
    }

    fun isNumeric(str: String): Boolean {
        return str.isDigitsOnly()
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
        fun forgetPassword()
        fun isLogin()
    }

}
