package com.example.iptv.api

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.iptv.Models.User
import com.example.iptv.Views.Activities.AuthActivity
import com.example.iptv.api.service.AppKey

class SessionManagement {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mContext: Context

    private val PRIVATE_MODE = 0

    private var PREF_NAME = "AUTH_EYEPLUS"
    private var IS_LOGIN = "IsLoggedIn"
//    private var KEY_NAME = "myName"
    private var KEY_EMAIL = "myEmail"
    private var KEY_PHONE = "myPhone"

    companion object {
        fun newInstance() = SessionManagement()
    }

    fun init(context: Context) {
        mContext = context
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    // create Login session
    fun createLoginSession(email: String, phone: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_PHONE, phone)
        editor.commit()
    }

    // check login session
    fun checkLogin() {
        if (!isLoggedIn()) {
            val intent = Intent(mContext, AuthActivity::class.java)
            intent.putExtra(AppKey.ACTIVITY_KEY().AUTH_ACT, AppKey.FRAGMENT_KEY().LOGIN_F)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(intent)
        }
    }

//     get Stored Session data
    fun getUserDetail(): User {
        val user = User(
            pref.getString(KEY_EMAIL, "")!!,
            pref.getString(KEY_PHONE, "")!!,
            ""
        )
        return user
    }

//    clear session
    fun logoutUser(){
        editor.clear()
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

}