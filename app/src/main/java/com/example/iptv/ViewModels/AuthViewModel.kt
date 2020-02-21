package com.example.iptv.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iptv.Models.ApiResponse
import com.example.iptv.Models.User
import com.example.iptv.Models.UserReg
import com.example.iptv.api.Repository.AuthRepo

class AuthViewModel : ViewModel() {
    private lateinit var liveUser: MutableLiveData<ApiResponse>
    private lateinit var repo: AuthRepo

    fun init() {
        repo = AuthRepo.newInstance()
    }

    fun authUser(email: String, pass: String): LiveData<ApiResponse> {
        liveUser = repo.authCheck(email, pass)
        return liveUser
    }

    fun newUser(user: UserReg): LiveData<ApiResponse> {
        liveUser = repo.newUser(user)
        return liveUser
    }

}