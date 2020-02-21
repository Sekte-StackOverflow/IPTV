package com.example.iptv.api.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iptv.Models.ApiResponse
import com.example.iptv.Models.User
import com.example.iptv.Models.UserReg
import com.example.iptv.api.APIClient
import com.example.iptv.api.service.UsersApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepo {
    private var api = APIClient().getAPI().create(UsersApiService::class.java)
    private var userAuth: MutableLiveData<ApiResponse> = MutableLiveData()
    private var mRes: MutableLiveData<MutableList<ApiResponse>> = MutableLiveData()

    companion object {
        fun newInstance() = AuthRepo()
    }

    fun authCheck(email: String, pass: String): MutableLiveData<ApiResponse> {
        val call = api.authentication(email, pass)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("Auth", t.message.toString())
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    val tmp = response.body()
                    userAuth.value = tmp
                }
            }
        })
        return userAuth
    }

    fun newUser(user: UserReg): MutableLiveData<ApiResponse> {
        val call = api.newUser(user)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("Auth", t.message.toString())
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    val tmp = response.body()
                    userAuth.value = tmp
                }
            }

        })
        return userAuth
    }


}