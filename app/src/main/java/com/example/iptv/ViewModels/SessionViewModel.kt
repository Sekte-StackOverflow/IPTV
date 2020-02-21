package com.example.iptv.ViewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.User
import com.example.iptv.api.SessionManagement

class SessionViewModel: ViewModel() {
    private lateinit var sessionManager: SessionManagement
    private var isLogin: MutableLiveData<Boolean> = MutableLiveData()
    private var userDetail: MutableLiveData<User> = MutableLiveData()
    private lateinit var context: Context

    fun init(context: Context) {
        sessionManager = SessionManagement()
        this.context = context
        sessionManager.init(context)
    }

    fun isLoginLive(): MutableLiveData<Boolean> {
        isLogin.value = sessionManager.isLoggedIn()
        return isLogin
    }

    fun getUser(): MutableLiveData<User> {
        userDetail.value = sessionManager.getUserDetail()
        return userDetail
    }

    fun logout() {
        sessionManager.logoutUser()
        isLogin.value = false
        Toast.makeText(context, "Logout Success", Toast.LENGTH_SHORT).show()
    }

    fun createUser(user: User) {
        sessionManager.createLoginSession(user.email, user.phone)
        userDetail.value = user
        isLogin.value = true
    }
}