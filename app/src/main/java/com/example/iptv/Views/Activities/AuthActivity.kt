package com.example.iptv.Views.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.iptv.Models.User
import com.example.iptv.Models.UserReg
import com.example.iptv.R
import com.example.iptv.ViewModels.AuthViewModel
import com.example.iptv.ViewModels.SessionViewModel
import com.example.iptv.Views.Fragments.ChangePassFragment
import com.example.iptv.Views.Fragments.LoginFragment
import com.example.iptv.Views.Fragments.RegisterFragment
import com.example.iptv.api.APIClient
import com.example.iptv.api.SessionManagement
import com.example.iptv.api.service.AppKey
import org.json.JSONObject
import org.json.JSONStringer

class AuthActivity : AppCompatActivity(),
    LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener,
    ChangePassFragment.OnFragmentInteractionListener
{

    private lateinit var  sessionViewModel: SessionViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        sessionViewModel = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        sessionViewModel.init(applicationContext)
        authViewModel.init()
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

    override fun isLogin() {

    }

    override fun login(username: String, password: String, type: String) {
        authViewModel.authUser(username, password).observe(this, Observer {
                res ->
            if (res != null) {
                if (res.response.status == "200" && res.response.message.toLowerCase() == "success") {
                    val user = if (type == "email") {
                        User(username, "", password)
                    } else {
                        User("", username, password)
                    }
                    Log.d(AppKey.ACTIVITY_KEY().AUTH_ACT, "Login Success: $username")
                    sessionViewModel.createUser(user)
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                } else {
                    Log.d(AppKey.ACTIVITY_KEY().AUTH_ACT, "Login Failed, form fill are wrong")
                    Toast.makeText(applicationContext, "Login Failed, Please Check your email and password!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(AppKey.ACTIVITY_KEY().AUTH_ACT, "response Null")
            }
        })
    }

    override fun newAccount(user: UserReg) {
        authViewModel.newUser(user).observe(this, Observer {
            res ->
            if (res != null) {
                if (res.response.status == "200" && res.response.message.toLowerCase() == "success") {
                    Log.d(AppKey.ACTIVITY_KEY().AUTH_ACT, "Register Success")
                    Toast.makeText(applicationContext, "Register Success, Please goto Login", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                } else {
                    Log.d(AppKey.ACTIVITY_KEY().AUTH_ACT, "Register Failed")
                    Toast.makeText(applicationContext, "Register Failed, Try Again", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(AppKey.ACTIVITY_KEY().AUTH_ACT, "response Null")
            }
        })
    }

    override fun changePassword() {

    }
}
