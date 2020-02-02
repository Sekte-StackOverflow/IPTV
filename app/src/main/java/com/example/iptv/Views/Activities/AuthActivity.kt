package com.example.iptv.Views.Activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.iptv.R
import com.example.iptv.Views.Fragments.LoginFragment
import com.example.iptv.Views.Fragments.RegisterFragment

class AuthActivity : AppCompatActivity(),
    LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val phase = intent.getStringExtra("AUTH_PHASE")
        if (phase == "SIGN_IN") {
            loadFragment(LoginFragment())
        } else if(phase == "SIGN_UP") {
            loadFragment(RegisterFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_auth_id, fragment)
            .commit()
    }

    override fun onFragmentInteraction(uri: Uri) {  }
}
