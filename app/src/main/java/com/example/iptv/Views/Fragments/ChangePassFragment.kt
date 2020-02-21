package com.example.iptv.Views.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.iptv.Models.User

import com.example.iptv.R
import com.example.iptv.ViewModels.SessionViewModel
import kotlinx.android.synthetic.main.fragment_change_pass.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChangePassFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class ChangePassFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sessionViewModel = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        sessionViewModel.init(activity!!.applicationContext)
        return inflater.inflate(R.layout.fragment_change_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionViewModel.isLoginLive().observe(this, Observer{
                t -> setEmail(t)
        })
    }

    private fun setEmail(isLogin: Boolean) {
        if (isLogin) {
            sessionViewModel.getUser().observe(this, Observer {
                    t ->  user = t
                if (t.email != "") {
                    change_pass_Email.setText(t.email)
                } else {
                    change_pass_Email.setText(t.phone)
                }
            })
        }
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
        fun changePassword()
    }

}
