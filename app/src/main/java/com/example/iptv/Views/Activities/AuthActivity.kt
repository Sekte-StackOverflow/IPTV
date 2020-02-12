package com.example.iptv.Views.Activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.iptv.R
import com.example.iptv.Views.Fragments.ChangePassFragment
import com.example.iptv.Views.Fragments.LoginFragment
import com.example.iptv.Views.Fragments.RegisterFragment
import com.example.iptv.api.service.AppKey

class AuthActivity : AppCompatActivity(),
    LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener,
    ChangePassFragment.OnFragmentInteractionListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val phase = intent.getStringExtra(AppKey.ACTIVITY_KEY().AUTH_ACT)
        when (phase) {
            AppKey.FRAGMENT_KEY().LOGIN_F -> loadFragment(LoginFragment())
            AppKey.FRAGMENT_KEY().REGISTER_F -> loadFragment(RegisterFragment())
            AppKey.FRAGMENT_KEY().CHANG_PASS_F -> loadFragment(ChangePassFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_auth_id, fragment)
            .commit()
    }

    override fun onFragmentInteraction(uri: Uri) {  }
    override fun forgetPassword() {
        loadFragment(ChangePassFragment())
    }

    override fun isLogin() {

    }
}
