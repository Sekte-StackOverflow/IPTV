package com.example.iptv.ViewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.PublicIPAddress
import com.example.iptv.Models.User
import com.example.iptv.api.Repository.PublicIPRepo
import com.example.iptv.api.SessionManagement

class SessionViewModel: ViewModel() {
    private lateinit var sessionManager: SessionManagement
    private lateinit var repo: PublicIPRepo
    private var isLogin: MutableLiveData<Boolean> = MutableLiveData()
    private var userDetail: MutableLiveData<User> = MutableLiveData()
    private var ipPublic: MutableLiveData<PublicIPAddress> = MutableLiveData()
    private val dialog: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var context: Context

    fun init(context: Context) {
        sessionManager = SessionManagement()
        this.context = context
        sessionManager.init(context)
        repo = PublicIPRepo.newInstance()
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
        sessionManager.createLoginSession(user.id ,user.email, user.phone, user.profile)
        userDetail.value = user
        isLogin.value = true
    }

    fun getPublicIP(): LiveData<PublicIPAddress> {
        ipPublic = repo.getPubllicIP()
        return ipPublic
    }
}